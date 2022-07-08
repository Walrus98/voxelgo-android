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

    private static VoxelRoomDatabase appDatabase;
    private static ExecutorService executor;

    public abstract CollectibleDao collectibleDao();
    public abstract CaptureDao captureDao();
    public abstract CapturedCollectiblesDao capturedCollectiblesDao();

    public static synchronized VoxelRoomDatabase getInstance(Context context) {
        if (appDatabase == null) {

            executor = Executors.newCachedThreadPool();

            appDatabase = Room.databaseBuilder(
                    context.getApplicationContext(),
                    VoxelRoomDatabase.class,
                    DATABASE_NAME
            ).fallbackToDestructiveMigration().build();
        }
        return appDatabase;
    }

    public ExecutorService getExecutor() {
        return executor;
    }
}