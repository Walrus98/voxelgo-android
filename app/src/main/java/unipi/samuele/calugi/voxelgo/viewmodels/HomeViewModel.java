package unipi.samuele.calugi.voxelgo.viewmodels;

import android.app.Application;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import unipi.samuele.calugi.voxelgo.room.CapturedCollectiblesRepository;
import unipi.samuele.calugi.voxelgo.room.capture.CaptureRepository;
import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;
import unipi.samuele.calugi.voxelgo.room.collectible.CollectibleRepository;
import unipi.samuele.calugi.voxelgo.threads.DownloadThread;

public class HomeViewModel extends AndroidViewModel {

    /**
     * La classe HomeViewModel viene istanziata all'avvio del FragmentHome e rimane in memoria per tutto il ciclo di vita del Fragment, ovvero finché
     * non viene distrutta. L'istanza del ViewModel rimane in vita anche quando l'utente esegue operazioni come la rotazione dello schermo o il cambio
     * di tema (modalità scura o chiara). Questo non solo permette di alleggerire il numero query effettuate sul database, me permette di tenere in
     * memoria la lista dei collezionabili anche quando è necessario ricreare la view.
     *
     * @see "https://developer.android.com/topic/libraries/architecture/viewmodel"
     */

    // Lista di tutti i collezionabili che l'utente può catturare. Viene restituita sotto forma di LiveData, in questo
    // modo il FragmentHome può osservare tramite Observer i cambiamenti sulla lista. Per esempio se durante il  ciclo di vita
    // del FragmentHome il Thread secondario scarica nuovi collezionabili e li inserisce all'interno del database, questi possono
    // essere visti (tramite observer) dal FragmentHome e può subito mostrarli a schermo senza dover ricreare la view da capo
    private final LiveData<List<Collectible>> allCollectibles;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        // Tramite la classe Repository di Room, eseguo una query che restituisce l'insieme di tutti i collezionabili
        // che l'utente può catturare. Il risultato della query viene restituito sotto forma di una lista di LiveData
        CollectibleRepository collectibleRepository = CollectibleRepository.getInstance(application);
        allCollectibles = collectibleRepository.getAllCollectibles();
    }

    /**
     * Metodo che restituisce la lista di collezionabili sotto forma di LiveData
     */
    public LiveData<List<Collectible>> getAllCollectibles() {
        return allCollectibles;
    }
}
