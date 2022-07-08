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

public class HomeViewModel extends AndroidViewModel {

    private final LiveData<List<Collectible>> allCollectibles;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        CollectibleRepository collectibleRepository = CollectibleRepository.getInstance(application);
        allCollectibles = collectibleRepository.getAllCollectibles();
    }

    public LiveData<List<Collectible>> getAllCollectibles() {
        return allCollectibles;
    }
}

//    public void insert(Collectible collectible) {
//        repository.insert(collectible);
//    }
//
//    public void update(Collectible collectible) {
//        repository.update(collectible);
//    }
//
//    public void delete(Collectible collectible) {
//        repository.delete(collectible);
//    }
//
//    public void deleteAllCollectibles() {
//        repository.deleteAllCollectibles();
//    }
