package unipi.samuele.calugi.voxelgo.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import unipi.samuele.calugi.voxelgo.R;
import unipi.samuele.calugi.voxelgo.fragments.infoadapters.CollectibleInfoWindowAdapter;
import unipi.samuele.calugi.voxelgo.utils.VoxelUtils;
import unipi.samuele.calugi.voxelgo.handlers.MarkerHandler;

public class FragmentMap extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnInfoWindowCloseListener { // GoogleMap.OnMarkerClickListener {

    private boolean locationPermissionGranted = false;
    private boolean isViewInfoClicked = false;

    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private MarkerHandler markerHandler;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private MutableLiveData<Location> lastKnownLocation;
    private LocationCallback locationCallback;

    private View viewInfoAdapter;
    private List<Marker> markerList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        markerHandler = MarkerHandler.getInstance(requireActivity().getApplication());
        lastKnownLocation = new MutableLiveData<>();
        markerList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        if (mapFragment != null) mapFragment.getMapAsync(this);
    }


    @Override
    public void onPause() {
        super.onPause();

        if (locationPermissionGranted) fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        markerHandler.destroy();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        updateLocationUI();
        getLocationPermission();
        getDeviceLocation();
        getMarkersLocation();
    }

    private void updateLocationUI() {

        map.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);
        map.setOnInfoWindowCloseListener(this);

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setBuildingsEnabled(false);
        map.setIndoorEnabled(false);

        if (isDarkModEnabled()) {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireActivity(), R.raw.mapstyle_dark));
        } else {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireActivity(), R.raw.mapstyle_light));
        }

        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);

        map.setInfoWindowAdapter(new CollectibleInfoWindowAdapter(requireContext()));
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {

        if (locationPermissionGranted) {

            map.setMyLocationEnabled(true);

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(100);
            locationRequest.setSmallestDisplacement(2);
            locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    for (Location location : locationResult.getLocations()) {
                        Location currentLocation = lastKnownLocation.getValue();
                        if (currentLocation == null) {
                            lastKnownLocation.postValue(location);
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20));
                            continue;
                        }
                        if (!isViewInfoClicked && currentLocation.distanceTo(location) > VoxelUtils.CAMERA_DISTANCE_METERS) {
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20));
                        }
                        if (!isViewInfoClicked && currentLocation.distanceTo(location) > VoxelUtils.MARKER_UPDATE_DISTANCE) {
                            lastKnownLocation.postValue(location);
                        }
                    }
                }
            };
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }

    }

    private void getMarkersLocation() {
        lastKnownLocation.observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location currentLocation) {
                Log.d("VoxelGO ->", "Posizione dell'utente aggiornata");

                for (Marker marker : markerList) {
                    marker.remove();
                }
                markerList.clear();

                markerHandler.setUserLocation(currentLocation);
                for (MarkerOptions markerOptions : markerHandler.getMarkers()) {
                    Marker marker = map.addMarker(markerOptions);
                    markerList.add(marker);
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        isViewInfoClicked = true;
        marker.showInfoWindow();
        return false;
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        boolean captured = markerHandler.removeMarker(marker);
        if (captured) {
            markerList.remove(marker);
            marker.remove();
            Log.d("VoxelGO ->", "L'utente ha catturato il collezionabile " + marker.getTitle());
        }
    }

    @Override
    public void onInfoWindowClose(@NonNull Marker marker) {
        isViewInfoClicked = false;
        restoreCurrentLocation();
    }

    @SuppressLint("MissingPermission")
    private void restoreCurrentLocation() {
        Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
        locationResult.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null && !isViewInfoClicked) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20));
                }
            }
        });
    }


    private final ActivityResultLauncher<String> permissionResult = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                Log.d("VoxelGO ->", "Richiesta di permessi di geolocalizzazione accetata");
                mapFragment.getMapAsync(FragmentMap.this);
            } else {
                Log.d("VoxelGO ->", "Richiesta di permessi di geolocalizzazione rifiutata");

                new AlertDialog.Builder(requireActivity())
                        .setTitle(requireContext().getText(R.string.dialog_title_no_permission_received))
                        .setMessage(requireContext().getText(R.string.dialog_content_no_permission_received))
                        .setPositiveButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    });

    private void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {;
            locationPermissionGranted = true;
            Log.d("VoxelGO ->", "Permessi di geolocalizzazione concessi");
        } else {
            Log.d("VoxelGO ->", "Invio richiesta di permessi di geolocalizzazione");
            permissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private boolean isDarkModEnabled() {
        int nightModeFlags = requireContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}
