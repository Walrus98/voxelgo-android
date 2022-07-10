package unipi.samuele.calugi.voxelgo.handlers;

import android.app.Application;
import android.location.Location;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import unipi.samuele.calugi.voxelgo.R;
import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;
import unipi.samuele.calugi.voxelgo.utils.VoxelUtils;

import unipi.samuele.calugi.voxelgo.fragments.FragmentMap;

import static unipi.samuele.calugi.voxelgo.utils.VoxelUtils.*;

public class MarkerHandler {

    /**
     * La classe MarkerHandler si occupa di gestire la generazione, lo spawn e la cattura dei collezionabili, rappresentati
     * all'interno della mappa come Marker.
     *
     * Per tenere in memoria la posizione dei Marker sulla mappa anche quando l'utente cambia schermata, muovendosi per esempio fra
     * i vari fragment dell'applicazione, è stato deciso di implementare una classe di tipo singleton. In questo modo la classe MarkerHandler
     * rimane in memoria e non viene distrutta dal GarbageCollector finché l'utente (o il sistema) non decide di terminare l'applicazione.
     *
     * @see FragmentMap
     */

    // Singleton pattern
    private static MarkerHandler markerHandler;

    public static MarkerHandler getInstance(Application application) {
        if (markerHandler == null) {
            markerHandler = new MarkerHandler(application);
        }
        return markerHandler;
    }

    // Lista di MarkerOptions che verranno poi mostrati a schermo dal FragmentMap
    private final List<MarkerOptions> markerOptionsList;
    private final Random random;

    // Posizione di un Marker
    private final Location markerLocation;
    // Posizione dell'utente
    private Location userLocation;

    // CollectiblesHandler è la classe che si occupa di interrogare il database per conto del MarkerHandler.
    // Questa classe, tramite l'utilizzo di repository, si occupa di:
    //      - inserire le nuove catture dei collezionabili all'interno del database
    //      - restiture un collezionabile casuale al MarkerHandler da far spwanare all'interno della mappa
    private final CollectiblesHandler collectiblesHandler;

    private MarkerHandler(Application application) {
        collectiblesHandler = new CollectiblesHandler(application);
        markerOptionsList = new ArrayList<>();
        markerLocation = new Location("");
        random = new Random();

        // Genero {MARKER_SPAWN_AMOUNT} di MarkerOptions e li inserisco all'interno della lista markerOptionsList
        for (int i = 0; i < MARKER_SPAWN_AMOUNT; i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptionsList.add(markerOptions);
        }
    }

    /**
     * Metodo che si occupa di aggiornare la posizione dei collezionabili ogni volta che la posizione dell'utente cambia.
     * @see FragmentMap con il metodo getMarkersLocation()
     */
    public void setUserLocation(Location currentLocation) {
        // Memorizzo la posizione attuale dell'utente
        this.userLocation = currentLocation;
        // Aggiorno la posizione dei Marker
        updateMarkers();
    }

    /**
     * Metodo che aggiorna la posizione dei Marker, spawnandoli attorno al giocatore
     */
    private void updateMarkers() {
        // Itero la lista di MarkerOptions definita all'interno del costruttore
        for (MarkerOptions markerOptions : markerOptionsList) {
            // Prendo la posizione dei MarkerOptions
            LatLng markerPosition = markerOptions.getPosition();

            // Se la posizione è null, significa che è la prima volta che vado a spawnare i Marker attorno al giocatore.
            if (markerPosition == null) {
                // Spawno casualmente un Marker attorno alla posizione dell'utente
                respawnMarker(markerOptions);
                continue;
            }

            // Se la posizione è diversa da null, significa che i marker sono già stati spawnati almeno una volta.
            // Calcolo la distanza fra la posizione del Marker e la posizione dell'utente
            markerLocation.setLongitude(markerPosition.longitude);
            markerLocation.setLatitude(markerPosition.latitude);

            // Se la distanza fra il Marker e l'utente è maggiore di {MARKER_SPAWN_DISTANCE}, significa che quel Marker
            // è ad una distanza molto lontana rispetto a quella dell'utente, quindi devo spawnarlo nuovamente
            float distanceInMeters = userLocation.distanceTo(markerLocation);
            if (distanceInMeters > VoxelUtils.MARKER_SPAWN_DISTANCE) {
                respawnMarker(markerOptions);
            }
        }
    }

    /**
     * Metodo che si occupa di spawnare i Marker attorno all'utente.
     *
     * Invece di andare ad aggiungere e rimuovere elementi dalla lista ogni volta che la posizione dell'utente cambia,
     * utilizzo gli stessi riferimenti in memoria dei MarkerOptions istanziati all'interno del costruttore e vado a modificare i loro parametri.
     *
     * Questo favorisce un consumo minore di risorse e uno spreco minore di batteria poiché:
     *      - il Garbage Collector non deve eliminare le istanze di MarkerOptions rimosse dalla lista
     *      - non è necessario creare tutte le volte una nuova istanza della classe MarkerOptions
     */
    private void respawnMarker(MarkerOptions markerOptions) {
        // Genero delle coordinate casuali attorno al giocatore
        double randomLat = ((double) random.nextInt(70 + 10) - 30) / 100000;
        double randomLgt = ((double) random.nextInt(70 + 10) - 30) / 100000;

        // Chiedo alla CollectiblesHandler di fornirmi un collezionabile casuale
        Collectible collectible = collectiblesHandler.getRandomCollectible(random);

        // Assegno al MarkerOptions la sua nuova posizione di latitudine e longitudine
        LatLng latLng = new LatLng(userLocation.getLatitude() + randomLat, userLocation.getLongitude() + randomLgt);
        markerOptions.position(latLng);
        // Assegno un nome al Marker
        markerOptions.title(collectible.getCollectibleName());

        // In base alla rarità del Collezionabile assegno un icona di colore differente al Marker
        Collectible.RarityType rarityType = collectible.getCollectibleRarity();
        switch (collectible.getCollectibleRarity()) {
            case COMMON:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_common));
                break;
            case RARE:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_rare));
                break;
            case ULTRARARE:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_ultrarare));
                break;
        }

        // Assegno una descrizione al Marker contente il tipo di rarità
        markerOptions.snippet(rarityType.toString());
    }

    /**
     * Metodo utilizzato catturare un Marker, viene invocato dalla classe FragmentMap
     *
     * @see FragmentMap con il metodo onInfoWindowClick()
     */
    public boolean removeMarker(Marker marker) {
        // Prendo la posizione del Marker
        double markerLatitude = marker.getPosition().latitude;
        double markerLongitude = marker.getPosition().longitude;

        // Calcolo la distanza fra il marker e l'utente
        markerLocation.setLatitude(markerLatitude);
        markerLocation.setLongitude(markerLongitude);
        float distanceInMeters = userLocation.distanceTo(markerLocation);

        // Se la distanza fra il Marker e l'utente è minore di {MARKER_COLLECT_DISTANCE}, significa che quel Marker
        // è abbastanza vicino per essere catturato, quindi deve essere rimosso
        if (distanceInMeters <= MARKER_COLLECT_DISTANCE) {
            // Prendo il MarkerOptions corrispettivo al Marker
            MarkerOptions markerOptions = getMarkerOptionFromMarker(marker);
            // Attraverso il collectiblesHandler inserisco la cattura all'interno del database
            collectiblesHandler.submitNewCapture(markerOptions);
            // aggiorno la posizione del marker catturato, spawnandolo attorno al giocatore e cambiando collezionabile
            respawnMarker(markerOptions);
            return true;
        }
        return false;
    }

    /**
     * Metodo che restiuisce il MarkerOptions del Marker passato per parametro.
     * Viene utilizzato dal metodo removeMarker()
     */
    private MarkerOptions getMarkerOptionFromMarker(Marker marker) {
        // Itero la lista di MarkerOptions
        for (MarkerOptions markerOptions : markerOptionsList) {
            // Se il MarkerOptions ha la stessa posizione del Marker
            if (markerOptions.getPosition().equals(marker.getPosition())) {
                // Allora restituisco quel MarkerOptions
                return markerOptions;
            }
        }
        throw new IllegalArgumentException("Non esiste nessun Marker associato ad un MarkerOptions!");
    }

    /**
     * Restituisce la lista di MarkerOptions, quest'ultima viene utilizzata per mostrare graficamente
     * i Marker sulla mappa.
     *
     * @see FragmentMap con il metodo getMarkersLocation()
     */
    public List<MarkerOptions> getMarkers() {
        return markerOptionsList;
    }
}
