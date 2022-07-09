package unipi.samuele.calugi.voxelgo.room.capture;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;

/**
 * La classe CaptureDao, (DAO = data access object) fornisce una collezione di metodi che eseguono operazioni
 * sul database sotto forma di query.
 *
 * Questa classe tiene traccia di tutte le possibili query che l'applicazione può fare sulla tabella delle Catture.
 *
 * @see "https://developer.android.com/training/data-storage/room/accessing-data"
 */
@Dao
public interface CaptureDao {

    // Restitusisce la lista di tutte le catture che l'utente ha effettuato
    @Query("SELECT * FROM captures")
    LiveData<List<Capture>> getAllCaptures();
}
