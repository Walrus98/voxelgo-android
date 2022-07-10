package unipi.samuele.calugi.voxelgo.handlers;


import android.app.Application;

import androidx.lifecycle.Observer;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import unipi.samuele.calugi.voxelgo.room.CapturedCollectiblesRepository;
import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;
import unipi.samuele.calugi.voxelgo.room.collectible.CollectibleRepository;

public class CollectiblesHandler implements Observer<List<Collectible>> {

    /**
     * CollectiblesHandler è la classe che si occupa di interrogare il database per conto del MarkerHandler.
     * Questa classe, tramite l'utilizzo di repository, si occupa di:
     *      - inserire le nuove catture dei collezionabili all'interno del database
     *      - restiture un collezionabile casuale al MarkerHandler da far spwanare all'interno della mappa
     *
     * @see MarkerHandler
     */

    // Repository dei collezionabili catturati
    private final CapturedCollectiblesRepository capturedCollectiblesRepository;
    // Lista dei collezionabili
    private List<Collectible> collectibleList;

    public CollectiblesHandler(Application application) {
        // Prendo il riferimento in memoria delle repository
        CollectibleRepository collectibleRepository = CollectibleRepository.getInstance(application);
        capturedCollectiblesRepository = CapturedCollectiblesRepository.getInstance(application);

        // Osservo la lista di collezionabili
        collectibleRepository.getAllCollectibles().observeForever(this);
    }

    /**
     * Ogni volta che si inseriscono nuovi collezionabili all'interno del database, aggiorno il contenuto della lista.
     *
     * Questo è utile perché se l'utente si sposta nella schermata della mappa mentre il thread
     * secondario sta ancora inserendo i collezionabili all'interno al database, il MarkerHandler spawnerebbe attorno
     * all'utente soltanto i collezionabili fino ad allora caricati. Grazie all'observer, è quindi possibile aggiornare
     * il contenuto della a tempo di runtime.
     */
    @Override
    public void onChanged(List<Collectible> collectibles) {
        collectibleList = collectibles;
    }

    /**
     * Metodo utilizzato per restituire un collezionabile casuale. Viene utilizzato dal MarkerHandler
     *
     * @see MarkerHandler
     */
    public Collectible getRandomCollectible(Random random) {
        return collectibleList.get(random.nextInt(collectibleList.size()));
    }

    /**
     * Metodo che viene utilizzato per inserire una nuova cattura all'interno del database
     */
    public void submitNewCapture(MarkerOptions markerOptions) {
        String collectibleName = markerOptions.getTitle();
        String captureDate = Calendar.getInstance().getTime().toString();
        String collectibleLocation = markerOptions.getPosition().toString();

        capturedCollectiblesRepository.insertCapture(collectibleName, captureDate, collectibleLocation);
    }
}
