package unipi.samuele.calugi.voxelgo.room;


import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;

import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;

public class CapturedCollectiblesRepository {

    private static CapturedCollectiblesRepository testRepository;

    public static CapturedCollectiblesRepository getInstance(Application application) {
        if (testRepository == null) {
            testRepository = new CapturedCollectiblesRepository(application);
        }
        return testRepository;
    }

    private final CapturedCollectiblesDao capturedCollectiblesDao;
    private final LiveData<List<Collectible>> allCapturedCollectibles;

    private final Executor executor;

    public CapturedCollectiblesRepository(Application application) {
        VoxelRoomDatabase database = VoxelRoomDatabase.getInstance(application);
        capturedCollectiblesDao = database.capturedCollectiblesDao();
        allCapturedCollectibles = capturedCollectiblesDao.getAllCapturedCollectibles();

        executor = database.getExecutor();
    }

    public void insertCapture(String collectibleName, String data, String location) {
        executor.execute(() -> capturedCollectiblesDao.insertCapture(collectibleName, data, location));
    }

    public LiveData<List<Collectible>> getAllCapturedCollectibles() {
        return allCapturedCollectibles;
    }
}