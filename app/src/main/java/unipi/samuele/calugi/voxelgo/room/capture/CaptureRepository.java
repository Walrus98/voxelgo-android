package unipi.samuele.calugi.voxelgo.room.capture;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import unipi.samuele.calugi.voxelgo.room.VoxelRoomDatabase;
import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;

public class CaptureRepository {

    private static CaptureRepository captureRepository;

    public static CaptureRepository getInstance(Application application) {
        if (captureRepository == null) {
            captureRepository = new CaptureRepository(application);
        }
        return captureRepository;
    }

    private final LiveData<List<Capture>> allCaptures;

    private CaptureRepository(Application application) {
        VoxelRoomDatabase database = VoxelRoomDatabase.getInstance(application);
        CaptureDao captureDao = database.captureDao();
        allCaptures = captureDao.getAllCaptures();
    }

    public LiveData<List<Capture>> getAllCaptures() {
        return allCaptures;
    }
}
