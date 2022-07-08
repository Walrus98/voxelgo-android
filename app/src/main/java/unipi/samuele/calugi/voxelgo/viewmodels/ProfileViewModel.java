package unipi.samuele.calugi.voxelgo.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import unipi.samuele.calugi.voxelgo.room.CapturedCollectiblesRepository;
import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;

public class ProfileViewModel extends AndroidViewModel {

    private final LiveData<List<Collectible>> capturedCollectibles;

    public ProfileViewModel(@NonNull Application application) {
        super(application);

        CapturedCollectiblesRepository repository = CapturedCollectiblesRepository.getInstance(application);
        capturedCollectibles = repository.getAllCapturedCollectibles();
    }

    public LiveData<List<Collectible>> getAllCapturedCollectibles() {
        return capturedCollectibles;
    }
}
