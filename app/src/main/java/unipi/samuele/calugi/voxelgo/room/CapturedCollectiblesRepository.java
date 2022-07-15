package unipi.samuele.calugi.voxelgo.room;


import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;

import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;

public class CapturedCollectiblesRepository {

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
    private static CapturedCollectiblesRepository testRepository;

    public static CapturedCollectiblesRepository getInstance(Application application) {
        if (testRepository == null) {
            testRepository = new CapturedCollectiblesRepository(application);
        }
        return testRepository;
    }

    // Riferimento al DAO istanziato nel database
    private final CapturedCollectiblesDao capturedCollectiblesDao;

    // Lista di tutti i collezionabili che l'utente ha catturato
    private final LiveData<List<Collectible>> allCapturedCollectibles;

    // Executor utilizzato per le operazioni sul database in maniera concorrente
    private final Executor executor;

    public CapturedCollectiblesRepository(Application application) {
        // Prendo l'istanza del database
        VoxelRoomDatabase database = VoxelRoomDatabase.getInstance(application);
        // Prendo il riferimento al DAO
        capturedCollectiblesDao = database.capturedCollectiblesDao();

        // Prendo la lista di tutti i collezionabili sotto forma di LiveData
        allCapturedCollectibles = capturedCollectiblesDao.getAllCapturedCollectibles();

        // Prendo il riferimento all'executor istanziato sul database
        executor = database.getExecutor();
    }

    /**
     * Metodo per inserire nuovi collezionabili catturati all'interno del database
     */
    public void insertCapture(String collectibleName, String data, String location) {
        executor.execute(() -> capturedCollectiblesDao.insertCapture(collectibleName, data, location));
    }

    /**
     * Metodo che restituisce la lista di collezionabili catturati sotto forma di LiveData
     */
    public LiveData<List<Collectible>> getAllCapturedCollectibles() {
        return allCapturedCollectibles;
    }
}