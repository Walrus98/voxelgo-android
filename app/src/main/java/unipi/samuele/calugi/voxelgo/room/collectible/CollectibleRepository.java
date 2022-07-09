package unipi.samuele.calugi.voxelgo.room.collectible;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;

import unipi.samuele.calugi.voxelgo.room.VoxelRoomDatabase;

public class CollectibleRepository {

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
     *  In questo modo è possibile avere più repository distinte che possono inviare query in maniera concorrente allo stesso database. L'atomicità
     *  dei dati è rispettata grazie all'utilizzo dei LiveData.
     *
     * @see VoxelRoomDatabase
     */

    private static CollectibleRepository collectibleRepository;

    public static CollectibleRepository getInstance(Application application) {
        if (collectibleRepository == null) {
            collectibleRepository = new CollectibleRepository(application);
        }
        return collectibleRepository;
    }

    // Riferimento al DAO istanziato nel database
    private final CollectibleDao collectibleDao;

    // Lista di tutti i collezionabili che l'utente può catturare
    private final LiveData<List<Collectible>> allCollectibles;

    // Executor utilizzato per le operazioni sul database in maniera concorrente
    private final Executor executor;

    public CollectibleRepository(Application application) {
        // Prendo l'istanza del database
        VoxelRoomDatabase database = VoxelRoomDatabase.getInstance(application);

        // Prendo il riferimento al DAO
        collectibleDao = database.collectibleDao();
        // Prendo la lista di tutti i collezionabili sotto forma di LiveData
        allCollectibles = collectibleDao.getAllCollectibles();
        // Prendo il riferimento all'executor istanziato sul database
        executor = database.getExecutor();
    }

    /**
     * Metodo per inserire nuovi collezionabili all'interno del database
     */
    public void insert(Collectible collectible) {
        executor.execute(() -> collectibleDao.insert(collectible));
    }

    /**
     * Metodo che restituisce la lista di collezionabili sotto forma di LiveData
     */
    public LiveData<List<Collectible>> getAllCollectibles() {
        return allCollectibles;
    }
}
