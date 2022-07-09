package unipi.samuele.calugi.voxelgo.room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import static unipi.samuele.calugi.voxelgo.utils.VoxelUtils.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import unipi.samuele.calugi.voxelgo.room.capture.Capture;
import unipi.samuele.calugi.voxelgo.room.capture.CaptureDao;
import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;
import unipi.samuele.calugi.voxelgo.room.collectible.CollectibleDao;
import unipi.samuele.calugi.voxelgo.utils.VoxelUtils;

@Database(entities = {Collectible.class, Capture.class}, version = DATABASE_VERSION, exportSchema = false)
public abstract class VoxelRoomDatabase extends RoomDatabase {

    // Singleton design pattern
    private static VoxelRoomDatabase appDatabase;

    // Executor utilizzato dalle Repository per eseguire le query sul database
    private static ExecutorService executor;

    // Dao contentente le query sulla tabella dei collezionabili
    public abstract CollectibleDao collectibleDao();

    // Dao contentente le query sulla tabella dei catture
    public abstract CaptureDao captureDao();

    // Dao contente le query sulle tabelle dei collezionabili e delle catture
    public abstract CapturedCollectiblesDao capturedCollectiblesDao();

    // Singleton design pattern
    public static synchronized VoxelRoomDatabase getInstance(Context context) {
        if (appDatabase == null) {

            // Istanzio un ThreadPool Executor
            executor = Executors.newCachedThreadPool();

            // Creo il database
            appDatabase = Room.databaseBuilder(
                    context.getApplicationContext(),
                    VoxelRoomDatabase.class,
                    DATABASE_NAME
            ).fallbackToDestructiveMigration().build();
        }
        return appDatabase;
    }

    /**
     * Restituisce l'executor, viene utilizzato dalle Repository
     */
    public ExecutorService getExecutor() {
        return executor;
    }
}