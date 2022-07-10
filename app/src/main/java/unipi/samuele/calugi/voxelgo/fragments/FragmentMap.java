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

    /**
     * Fragment che mostra a schermo la mappa in cui l'utente può catturare i collezionabili (implementata con le API di GoogleMaps).
     *
     * I collezionabili vengono spawnati in maniera randomica attorno al giocatore e la loro posizione rimane in memoria finché
     * l'applicazione non viene chiusa. Quando l'utente si muove nella mappa, dopo una certa distanza, i collezionabili più lontani vengono
     * rimossi e vengono nuovamante spwanati attorno al giocatore.
     *
     * L'utente non ha la possibilità di muovere la telecamera, ma essa è bloccata sulla posizione del giocatore e segue tutti i suoi spostamenti
     *
     * I collezionabili sono rappresentati sotto forma di Marker e vengono gestiti dalla classe MarkerHandler
     *
     * @see MarkerHandler
     */

    // Variabile utilizzata per controlla se l'utente ha conferito i permessi di geolocalizzazione
    private boolean locationPermissionGranted = false;
    // Variabile utilizzata per controllare se l'utente ha la schermata di un marker aperta o meno
    private boolean isViewInfoClicked = false;

    // Mappa implementata con le API di Google Maps
    private GoogleMap map;
    // Fragment che contiene la mappa
    private SupportMapFragment mapFragment;
    // Classe che si occupa di gestire la generazione, lo spawn e la cattura dei Marker
    private MarkerHandler markerHandler;
    // Classe fornita da Google per ottenere la posizione dell'utente, sono necessari i permessi di geolocalizzazione
    private FusedLocationProviderClient fusedLocationProviderClient;
    // LiveData che viene utilizzato per memorizzare l'ultima posizione del giocatore
    private MutableLiveData<Location> lastKnownLocation;
    // Callback che viene invocata ogni volta che la posizione dell'utente cambia
    private LocationCallback locationCallback;
    // Lista di Marker da far visualizzare all'interno della mappa
    private List<Marker> markerList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Istanzio il MarkerHandler
        markerHandler = MarkerHandler.getInstance(requireActivity().getApplication());
        // Istanzio il LiveData
        lastKnownLocation = new MutableLiveData<>();
        // Istanzio la lista
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

        // Prendo il riferimento del Fragment della mappa
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        // Istanzio il fusedLocationProvider, questa classe offerta da google forisce un insieme di metodi per ottenere la posizione dell'utente
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Chiamo il metodo onMapReady()
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
        // Prendo il riferimento all'istanza della mappa di GoogleMap
        map = googleMap;

        // Modifico l'aspetto, le impostazioni e registro i listener della mappa
        updateLocationUI();
        // Controllo i permessi di geolocalizzazione. Se non presenti, viene mostrata la richiesta di autorizzazione a schermo
        getLocationPermission();
        // Se l'utente ha conferito i permessi di geolocalizzazione, prendo la posizione dell'utente
        getDeviceLocation();
        // Se l'utente ha conferito i permessi di geolocalizzazione, spawno i marker attorno all'utente
        getMarkersLocation();
    }

    /**
     * Metodo utilizzato per modificare il l'aspetto e le impostazioni della mappa e per registrare i vari listener
     */
    private void updateLocationUI() {

        // Registro i listener
        map.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);
        map.setOnInfoWindowCloseListener(this);

        // Imposto lo stile della mappa (normale, visuale da satellite, etc)
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Rimuovo il rendering degli edifici in 3D
        map.setBuildingsEnabled(false);
        // Rimuovo l'accesso interno agli edifici
        map.setIndoorEnabled(false);

        // In base al tipo di modalità che l'utente sta utilizzando (darkmode o lightmode), modifico il colore della mappa
        if (isDarkModEnabled()) {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireActivity(), R.raw.mapstyle_dark));
        } else {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireActivity(), R.raw.mapstyle_light));
        }

        // Nascondo la bussola su schermo
        map.getUiSettings().setCompassEnabled(false);
        // Disabilito le gesture della mappa
        map.getUiSettings().setAllGesturesEnabled(false);
        // Rimuovo il pulsante per posizionare la telecamera sull'utente, poiché la telecamera segue sempre la sua posizione
        map.getUiSettings().setMyLocationButtonEnabled(false);
        // Rimuovo la toolbar per visualizzare il Marker su GoogleMap
        map.getUiSettings().setMapToolbarEnabled(false);
        // Modifico la view del menù che viene aperto quando l'utente clicca su un Marker
        map.setInfoWindowAdapter(new CollectibleInfoWindowAdapter(requireContext()));
    }

    /**
     * Metodo utilizzato per prendere la posizione dell'utente
     */
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        // Se l'utente ha conferito i permessi di geolocalizzazione
        if (locationPermissionGranted) {
            // Faccop comparire la posizione dell'utente sulla mappa
            map.setMyLocationEnabled(true);

            // Registro la richiesta di ottenere la posizione dell'utente
            LocationRequest locationRequest = LocationRequest.create();
            // Chiedo la posizione dell'utente ogni secondo
            locationRequest.setInterval(1000);
            // Se il sistema ha dovuto calcolare la posizione dell'utente per un'altra applicazione,
            // allora la acquisisco anche io in maniera passiva. Questo controllo viene fatto ogni 100ms
            locationRequest.setFastestInterval(100);
            // Chiedo la posizione dell'utente solo se si è spostato di almeno 2 metri
            locationRequest.setSmallestDisplacement(2);
            // Imposto la precisione della posizione dell'utente a HIGH ACCURACY
            locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);

            // Callback che viene eseguita ogni volta che la posizione dell'utente cambia secondo i parametri stabiliti precedentemente
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    // Ogni volta che la posizione dell'utente cambia
                    for (Location location : locationResult.getLocations()) {
                        // Prendo l'ultima posizione dell'utente che ho memorizzato all'interno di lastKnownLocation
                        Location currentLocation = lastKnownLocation.getValue();
                        // Se quest'ultimo è null, significa che l'utente ha appena aperto la schermata della mappa e quindi è
                        // la prima volta che acquisco la sua posizione.
                        if (currentLocation == null) {
                            // Memorizzo la posizione corrente dell'utente all'interno di lastKnownLocation.
                            // Questo invoca la callback onChanged() dell'observer @see getMarkersLocation();
                            lastKnownLocation.postValue(location);
                            // Sposto la telecamera sulla posizione dell'utente
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20));
                            continue;
                        }
                        // Se la posizione dell'utente è diversa da null, significa che ho già memorizzato in passato la sua posizione. Quindi
                        // devo controllare la distanza fra la nuova posizione e quella precedente.
                        // Se la distanza fra la vecchia posizione e quella nuova è maggiore di CAMERA_DISTANCE_METERS metri e l'utente non ha nessuna
                        // schermata di menù di un Marker aperta, allora sposto la telecamera sulla nuova posizione dell'utente
                        if (!isViewInfoClicked && currentLocation.distanceTo(location) > VoxelUtils.CAMERA_DISTANCE_METERS) {
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20));
                        }
                        // Se la distanza fra la vecchia posizione e quella nuova è maggiore di MARKER_UPDATE_DISTANCE metri e l'utente non nessuna
                        // schermata di menù di un Marker aperta, allora aggiorno la posizione dell'utente memorizzata all'interno di lastKnownLocation
                        if (!isViewInfoClicked && currentLocation.distanceTo(location) > VoxelUtils.MARKER_UPDATE_DISTANCE) {
                            // Memorizzo la posizione corrente dell'utente all'interno di lastKnownLocation.
                            // Questo invoca la callback onChanged() dell'observer @see getMarkersLocation();
                            lastKnownLocation.postValue(location);
                        }
                    }
                }
            };
            // Attraverso il fusedLocationProviderClient, registro la callback da invocare ogni volta che la posizione dell'utente cambia
            // Questa callback viene eseguita in Loop su un Thread secondario
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }

    }

    /**
     * Metodo utilizzato per spawnare i Marker attorno all'utente
     */
    private void getMarkersLocation() {
        // Ogni volta che l'utente si sposta di una certa quantità di metri rispetto all'ultima posizione salvata, il valore all'interno di
        // lastKnownLocation viene modificato (@see onLocationResult()). Poichè quest'ultimo è un LiveData, può essere osservato da un Osservatore.
        // Quindi ogni volta che la posizione dell'utente cambia, viene invocato il metodo onChanged() dell'observer.
        lastKnownLocation.observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location currentLocation) {
                Log.d("VoxelGO ->", "Posizione dell'utente aggiornata");

                // Rimuovo graficamente i Marker dei collezionabili sulla mappa
                for (Marker marker : markerList) {
                    marker.remove();
                }
                markerList.clear();

                // Notifico al MarkerHandler che la posizione dell'utente è cambiata, quindi
                // quest'ultimo può calcolarsi la nuova posizione dei marker
                markerHandler.setUserLocation(currentLocation);

                // Mostro a schermo i nuovi Marker sulla mappa
                for (MarkerOptions markerOptions : markerHandler.getMarkers()) {
                    Marker marker = map.addMarker(markerOptions);
                    markerList.add(marker);
                }
            }
        });
    }

    /**
     * Metodo che viene invocato quando l'utente clicca su un Marker
     */
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        isViewInfoClicked = true;
        marker.showInfoWindow();
        return false;
    }

    /**
     * Metodo che viene invocato quando l'utente clicca sul menù a comparsa del Marker, ovvero quando
     * sta cercando di catturare un collezionabile
     */
    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        // L'utente prova a catturare il collezionabile
        boolean captured = markerHandler.removeMarker(marker);
        // Se è abbastanza vicino
        if (captured) {
            // Rimuovo graficamente dalla mappa il Marker del collezionabile catturato
            markerList.remove(marker);
            marker.remove();
            Log.d("VoxelGO ->", "L'utente ha catturato il collezionabile " + marker.getTitle());
        }
    }

    /**
     * Metodo che viene chiamato quando l'utente chiude il menù a comparsa dei Marker
     */
    @Override
    public void onInfoWindowClose(@NonNull Marker marker) {
        isViewInfoClicked = false;
        // Sposto la telecamera sulla posizione dell'utente
        restoreCurrentLocation();
    }

    /**
     * Metodo che viene utilizzato per posizionare la telecamera sull'utente dopo che ha chiuso
     * un menù a comparsa di un Marker
     */
    @SuppressLint("MissingPermission")
    private void restoreCurrentLocation() {
        // Chiedo al fusedLocationProviderClient l'ultima posizione dell'utente registrata
        Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
        // Su Thread secondario sposto la telecamera sulla posizione dell'utente
        locationResult.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null && !isViewInfoClicked) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20));
                }
            }
        });
    }

    /**
     * Metodo che viene chiamato quando l'utente decide di assegnare o meno i permessi di geolocalizzazione.
     * Se quest'ultimi sono stati assegnati, allora è possibile invocare il metodo getDeviceLocation().
     * Se quest'ultimi non sono stati assegnati, allora viene mostrato a schermo un Dialog contente un messaggio di errore
     * che spiega all'utente che non sarà possibile catturare i collezionabili senza l'autorizzazione dei permessi.
     */
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

    /**
     * Metodo che viene utilizzato per controllare se l'utente ha conferito i permessi di geolocalizzazione
     * all'applicazione. Se questi ultimi non sono stati assegnati, viene mostrata la richiesta di autorizzazione a schermo
     */
    private void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {;
            locationPermissionGranted = true;
            Log.d("VoxelGO ->", "Permessi di geolocalizzazione concessi");
        } else {
            Log.d("VoxelGO ->", "Invio richiesta di permessi di geolocalizzazione");
            permissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Metodo utilizzato per controlalre il tipo di modalità che l'utente sta utilizzando (darkmode o lightmode)
     * viene utilizzato all'interno del updateLocationUI();
     *
     * @see updateLocationUI()
     */
    private boolean isDarkModEnabled() {
        int nightModeFlags = requireContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}
