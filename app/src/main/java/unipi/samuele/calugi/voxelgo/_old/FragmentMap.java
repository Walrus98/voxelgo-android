//package unipi.samuele.calugi.voxelgo.fragments;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.fragment.app.Fragment;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MapStyleOptions;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//
//
//import unipi.samuele.calugi.voxelgo.R;
//
//public class FragmentMap extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
//
//    private GoogleMap map;
//    private FusedLocationProviderClient fusedLocationProviderClient;
//    private Location currentLocation;
////    private MarkerHandler markerHandler;
//
//    private Location lastKnownLocation;
//    private CameraPosition cameraPosition;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (!checkPermission()) {
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
//        }
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_map, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
//        supportMapFragment.getMapAsync(this);
//    }
//
//    @Override
//    @SuppressLint("MissingPermission")
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        this.map = googleMap;
//
//        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        map.setBuildingsEnabled(true);
//        map.setIndoorEnabled(false);
//        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireActivity(), R.raw.mapstyle));
//
//        map.getUiSettings().setCompassEnabled(true);
//        map.getUiSettings().setMyLocationButtonEnabled(false);
//        map.getUiSettings().setZoomControlsEnabled(false);
//        map.getUiSettings().setZoomGesturesEnabled(false);
//        map.getUiSettings().setTiltGesturesEnabled(true);
//
//        if (checkPermission()) {
//            setupMap();
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private void setupMap() {
//        Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
//        locationResult.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                lastKnownLocation = location;
//                if (lastKnownLocation != null) {
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                        new LatLng(lastKnownLocation.getLatitude(),
//                        lastKnownLocation.getLongitude()),
//                        20)
//                    );
//                    map.setMyLocationEnabled(true);
//                }
//            }
//        });
//    }
//
//
////    @SuppressLint("MissingPermission")
////    private void setupCameraToCurrentPosition() {
////
////        map.setMyLocationEnabled(true);
////
////        Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
////        locationResult.addOnSuccessListener(new OnSuccessListener<Location>() {
////            @Override
////            public void onSuccess(Location location) {
////                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
////                CameraPosition cameraPosition = new CameraPosition.Builder()
////                        .target(latLng)     // Sets the center of the map to
////                        .zoom(20)           // Sets the zoom
//////                            .bearing(40)        // Sets the orientation of the camera to east
//////                            .tilt(90)           // Sets the tilt of the camera to 30 degrees
////                        .build();           // Creates a CameraPosition from the builder
////                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
////
//////                spawnMarkers(location);
////            }
////        });
////    }
//
//
////    private void spawnMarkers() {
////
////        double lat = lastKnownLocation.getLatitude();
////        double lng = lastKnownLocation.getLongitude();
////
////        LatLng latLng = new LatLng(lat, lng);
////        MarkerOptions markerOptions = new MarkerOptions();
////        markerOptions.position(latLng);
////        markerOptions.title("Marker");
////        map.addMarker(markerOptions);
////    }
//
////    private void spawnMarkers(Location location)  {
////        markerHandler = MarkerHandler.getInstance(location);
////        List<MarkerOptions> markerOptions = markerHandler.getMarkers();
////
////        for (MarkerOptions markerOption: markerOptions) {
////            map.addMarker(markerOption);
////        }
////    }
//
//    @Override
//    public boolean onMarkerClick(@NonNull Marker marker) {
//
//        if (marker.getTitle().equals("Marker")) {
//            marker.remove();
//        }
//        return false;
//    }
//
//    private boolean checkPermission() {
//        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
//    }
//
////        List<LatLng> coordinates = viewModel.getCoordinates(currentLocation);
////
////        for (LatLng coordinate : coordinates) {
////            MarkerOptions markerOptions = new MarkerOptions();
////            markerOptions.position(coordinate);
////            markerOptions.title("Marker");
////            googleMap.addMarker(markerOptions);
////        }
//
//
////        MarkerOptions markerOptions = new MarkerOptions();
////        markerOptions.position(latLng);
////        googleMap.addMarker(markerOptions);
////
////        viewModel.getMarkerList();
//
//
//}
//
////                @Override
////                public void onSuccess(Location location) {
////                    mLocation = location;
////                    if (location != null) {
////                        supportMapFragment.getMapAsync(FragmentMap.this);
////                    }
////                }
////            });
//
////        LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
////
////
////        CameraPosition cameraPosition = new CameraPosition.Builder()
////                .target(latLng) // Sets the center of the map to
////                .zoom(20)                   // Sets the zoom
////                .bearing(mLocation.getBearing()) // Sets the orientation of the camera to east
////                .tilt(90)    // Sets the tilt of the camera to 30 degrees
////                .build();    // Creates a CameraPosition from the builder
////        map.animateCamera(CameraUpdateFactory.newCameraPosition(
////                cameraPosition));
////
////
//////        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
////        map.addMarker(new MarkerOptions().position(latLng).title("User"));
//
//
////    @Override
////    public void onLocationChanged(@NonNull Location location) {
////        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
////        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
////        mMap.animateCamera(cameraUpdate);
////        locationManager.removeUpdates(this);
////    }
//
//
//// Add a marker in Sydney and move the camera
////        LatLng sydney = new LatLng(-34, 151);
////        googleMap.addMarker(new MarkerOptions()
////                .position(sydney)
////                .title("Marker in Sydney"));
////        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
//
//// Async map
////        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
////            @Override
////            public void onMapReady(GoogleMap googleMap) {
////                // When map is loaded
////                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
////                    @Override
////                    public void onMapClick(LatLng latLng) {
////                        // When clicked on map
////                        // Initialize marker options
////                        MarkerOptions markerOptions=new MarkerOptions();
////                        // Set position of marker
////                        markerOptions.position(latLng);
////                        // Set title of marker
////                        markerOptions.title(latLng.latitude+" : "+latLng.longitude);
////                        // Remove all marker
////                        googleMap.clear();
////                        // Animating to zoom the marker
////                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
////                        // Add marker on map
////                        googleMap.addMarker(markerOptions);
////                    }
////                });
////            }
////        });
//// Return view
//
////        @SuppressLint("MissingPermission")
////        private void getCurrentLocation() {
////            Task<Location> task = client.getLastLocation();
////            task.addOnSuccessListener(new OnSuccessListener<Location>() {
////                @Override
////                public void onSuccess(Location location) {
////                    mLocation = location;
////                    if (location != null) {
////                        supportMapFragment.getMapAsync(FragmentMap.this);
////                    }
////                }
////            });
////        }
//
