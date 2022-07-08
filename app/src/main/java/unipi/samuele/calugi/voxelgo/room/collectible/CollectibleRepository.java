package unipi.samuele.calugi.voxelgo.room.collectible;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;

import unipi.samuele.calugi.voxelgo.room.VoxelRoomDatabase;

public class CollectibleRepository {

    private static CollectibleRepository collectibleRepository;

    public static CollectibleRepository getInstance(Application application) {
        if (collectibleRepository == null) {
            collectibleRepository = new CollectibleRepository(application);
        }
        return collectibleRepository;
    }

    private final CollectibleDao collectibleDao;
    private final LiveData<List<Collectible>> allCollectibles;
    private final Executor executor;

    public CollectibleRepository(Application application) {
        VoxelRoomDatabase database = VoxelRoomDatabase.getInstance(application);
        collectibleDao = database.collectibleDao();
        allCollectibles = collectibleDao.getAllCollectibles();

        executor = database.getExecutor();
    }

    public void insert(Collectible collectible) {
        executor.execute(() -> collectibleDao.insert(collectible));
    }

    public LiveData<List<Collectible>> getAllCollectibles() {
        return allCollectibles;
    }


//    public void update(Collectible collectible) {
//        VoxelRoomDatabase.executor.execute(() -> collectibleDao.update(collectible));
//    }
//
//    public void delete(Collectible collectible) {
//        VoxelRoomDatabase.executor.execute(() -> collectibleDao.delete(collectible));
//    }
//
//    public void deleteAllCollectibles(Collectible collectible) {
//        VoxelRoomDatabase.executor.execute(collectibleDao::deleteAllCollectibles);
//    }


//    public int getCollectibleIDByName(String collectibleName) {
//        return collectibleDao.getCollectibleIDByName(collectibleName);
//    }
}

//    public int getCollectibleIDByName(String collectibleName) throws ExecutionException, InterruptedException {
//        ExecutorService executor = VoxelRoomDatabase.executor;
//        Future<Integer> future = executor.submit(new Callable<Integer>() {
//            @Override
//            public Integer call() throws Exception {
//                return collectibleDao.getCollectibleIDByName(collectibleName);
//            }
//        });
//        executor.awaitTermination(1L, TimeUnit.SECONDS);
//        return future.get();
//    }