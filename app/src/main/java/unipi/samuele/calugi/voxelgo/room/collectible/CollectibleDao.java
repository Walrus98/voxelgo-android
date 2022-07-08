package unipi.samuele.calugi.voxelgo.room.collectible;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class CollectibleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract void insert(Collectible collectible);

    @Query("SELECT * FROM collectibles")
    abstract LiveData<List<Collectible>> getAllCollectibles();
}
