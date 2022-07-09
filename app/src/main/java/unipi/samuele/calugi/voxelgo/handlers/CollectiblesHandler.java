package unipi.samuele.calugi.voxelgo.handlers;


import android.app.Application;

import androidx.lifecycle.Observer;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import unipi.samuele.calugi.voxelgo.room.CapturedCollectiblesRepository;
import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;
import unipi.samuele.calugi.voxelgo.room.collectible.CollectibleRepository;

public class CollectiblesHandler implements Observer<List<Collectible>> {

    private final CollectibleRepository collectibleRepository;
    private final CapturedCollectiblesRepository capturedCollectiblesRepository;
    private List<Collectible> collectibleList;

    public CollectiblesHandler(Application application) {

        collectibleRepository = CollectibleRepository.getInstance(application);
        capturedCollectiblesRepository = CapturedCollectiblesRepository.getInstance(application);

        collectibleRepository.getAllCollectibles().observeForever(this);
    }

    @Override
    public void onChanged(List<Collectible> collectibles) {
        collectibleList = collectibles;
    }

    public Collectible getRandomCollectible(Random random) {
        return collectibleList.get(random.nextInt(collectibleList.size()));
    }

    public void submitNewCapture(MarkerOptions markerOptions) {
        String collectibleName = markerOptions.getTitle();
        String captureDate = Calendar.getInstance().getTime().toString();
        String collectibleLocation = markerOptions.getPosition().toString();

        capturedCollectiblesRepository.insertCapture(collectibleName, captureDate, collectibleLocation);
    }

    public void destroyObserver()  {
        collectibleRepository.getAllCollectibles().removeObserver(this);
    }
}
