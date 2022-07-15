package unipi.samuele.calugi.voxelgo.room.capture;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import unipi.samuele.calugi.voxelgo.room.VoxelRoomDatabase;

public class CaptureRepository {

    /**
     * Poichè tutte le operazioni eseguite sul database (quindi tramite l'utilizzo di un DAO) devono essere fatte su Thread secondari, così da
     * non bloccare l'esecuzione del ThreadUI, è oppurtono definire una classe Repository.
     *
     * La classe Repository si occupa di fornire l'insieme di tutte le operazioni che possono essere fatte sul database tramite DAO.
     * In questo modo è facile eseguire tutte le operazioni in modo concorrente:
     *
     *  - la classe Repository è di tipo singleton, quindi viene istanziata una sola volta e viene preso il suo riferimento in memoria dalle
     *  altre classi quando è necessario fare operazioni sul database.
     *  - tutte le query da eseguire tramite DAO vengono inviate sotto farma di Runnable ad un Executor, quest'ultimo dichiarato e istanziato nella
     *  classe VoxelRoomDatabase.
     *
     *  In questo modo è possibile avere più repository distinte che possono inviare query in maniera concorrente allo stesso database.
     *
     * @see VoxelRoomDatabase
     */

    // Singleton pattern
    private static CaptureRepository captureRepository;

    public static CaptureRepository getInstance(Application application) {
        if (captureRepository == null) {
            captureRepository = new CaptureRepository(application);
        }
        return captureRepository;
    }

    // Lista di tutte le catture effettuate dall'utente
    private final LiveData<List<Capture>> allCaptures;

    private CaptureRepository(Application application) {
        VoxelRoomDatabase database = VoxelRoomDatabase.getInstance(application);

        // Prendo il riferimento al DAO istanziato nel database
        CaptureDao captureDao = database.captureDao();
        // Prendo la lista di tutti i collezionabili sotto forma di LiveData
        allCaptures = captureDao.getAllCaptures();
    }

    /**
     * Metodo che restituisce la lista di tutte le catture fatte dall'utente sotto forma di LiveData
     */
    public LiveData<List<Capture>> getAllCaptures() {
        return allCaptures;
    }
}
