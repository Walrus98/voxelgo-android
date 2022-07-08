package unipi.samuele.calugi.voxelgo.room.capture;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;

@Dao
public interface CaptureDao {

    @Query("SELECT * FROM captures")
    LiveData<List<Capture>> getAllCaptures();
}
