package unipi.samuele.calugi.voxelgo.room.collectible;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * La classe CollectibleDao, (DAO = data access object) fornisce una collezione di metodi che eseguono operazioni
 * sul database sotto forma di query.
 *
 * Questa classe tiene traccia di tutte le possibili query che l'applicazione può fare sulla tabella dei Collezionabili.
 *
 * @see "https://developer.android.com/training/data-storage/room/accessing-data"
 */

@Dao
public abstract class CollectibleDao {

    // Inserisce un nuovo colelzionabile all'interno della tabella dei collezionabili
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(Collectible collectible);

    // Restitusisce la lista di tutti i collezionabili che l'utente può catturare
    @Query("SELECT * FROM collectibles")
    abstract LiveData<List<Collectible>> getAllCollectibles();
}
