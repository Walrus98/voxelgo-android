package unipi.samuele.calugi.voxelgo.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import unipi.samuele.calugi.voxelgo.room.CapturedCollectiblesRepository;
import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;

public class ProfileViewModel extends AndroidViewModel {

    // Lista di tutti i collezionabili che l'utente ha catturato. Viene restituita sotto forma di LiveData, in questo
    // modo il FragmentProfile può osservare tramite Observer i cambiamenti sulla lista.
    private final LiveData<List<Collectible>> capturedCollectibles;

    public ProfileViewModel(@NonNull Application application) {
        super(application);

        // Tramite la classe Repository di Room, eseguo una query che restituisce l'insieme di tutti i collezionabili
        // che l'utente ha catturato. Il risultato della query viene restituito sotto forma di una lista di LiveData
        CapturedCollectiblesRepository repository = CapturedCollectiblesRepository.getInstance(application);
        capturedCollectibles = repository.getAllCapturedCollectibles();
    }

    /**
     * Metodo che restituisce la lista di collezionabili catturati sotto forma di LiveData
     */
    public LiveData<List<Collectible>> getAllCapturedCollectibles() {
        return capturedCollectibles;
    }
}
