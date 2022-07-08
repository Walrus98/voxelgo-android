package unipi.samuele.calugi.voxelgo.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import unipi.samuele.calugi.voxelgo.room.capture.Capture;
import unipi.samuele.calugi.voxelgo.room.capture.CaptureDao;
import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;
import unipi.samuele.calugi.voxelgo.room.collectible.CollectibleDao;

@Dao
public abstract class CapturedCollectiblesDao {

    @Query("SELECT collectibles.* FROM collectibles, captures WHERE collectibles.collectible_id = captures.collectible_id")
    abstract LiveData<List<Collectible>> getAllCapturedCollectibles();

    @Query("SELECT collectible_id FROM collectibles WHERE collectible_name LIKE :collectibleName")
    abstract int getCollectibleIDByName(String collectibleName);

    @Insert
    abstract void insert(Capture capture);

    @Insert
    @Transaction
    public void insertCapture(String collectibleName, String data, String location) {
        int collectibleID = getCollectibleIDByName(collectibleName);
        insert(new Capture(collectibleID, data, location));
    }
}