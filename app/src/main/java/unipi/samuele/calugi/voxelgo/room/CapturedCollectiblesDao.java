package unipi.samuele.calugi.voxelgo.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import unipi.samuele.calugi.voxelgo.room.capture.Capture;
import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;

import unipi.samuele.calugi.voxelgo.handlers.MarkerHandler;

/**
 * La classe CapturedCollectiblesDao, (DAO = data access object) fornisce una collezione di metodi che eseguono operazioni
 * sul database sotto forma di query.
 *
 * Questa classe tiene traccia di tutte le possibili query che l'applicazione può fare sulla tabella { Collezionabili u Catture }.
 *
 * @see "https://developer.android.com/training/data-storage/room/accessing-data"
 */
@Dao
public abstract class CapturedCollectiblesDao {

    // Restituisce una lista di tutti i collezionabili catturati dall'utente.
    // Seleziona la tabella dei collezionabili unita a quella delle catture e restituisce le entry che hanno lo stesso collectible_id
    @Query("SELECT collectibles.* FROM collectibles, captures WHERE collectibles.collectible_id = captures.collectible_id")
    abstract LiveData<List<Collectible>> getAllCapturedCollectibles();

    // Restituisce l'id del collezionabile in base al nome, viene invocato dal metodo insertCapture()
    @Query("SELECT collectible_id FROM collectibles WHERE collectible_name LIKE :collectibleName")
    abstract int getCollectibleIDByName(String collectibleName);

    // Inserisce una cattura all'interno della tabella delle catture, viene invocato dal metodo insertCapture()
    @Insert
    abstract void insert(Capture capture);

    /**
     * Metodo utilizzato per inserire un collezionabile catturato all'interno del database. Questo è il metodo
     * che viene chiamato dalla classe MarkerHandler tramite Repository. In questo modo il metodo insertCapture()
     * viene eseguito su thread secondario evitando di bloccare il Thread UI.
     *
     * Senza questo metodo, la classe MarkerHandler avrebbe dovuto:
     *      - eseguire la query getCollectibleIDByName() per ottenere l'id del collezionabile che l'utente ha catturato
     *      - aspettare il risultato della query fatta su thread secondario bloccando quindi il ThreadUI (attraverso meccanismi
     *        come Future o awaitTermination()
     *      - eseguire la query insert() per inserire la cattura all'interno del database
     *
     * Grazie a questo metodo la classe MarkerHandler può quindi inserire una nuova cattura all'interno del database
     * senza preoccuparsi di bloccare l'esecuzione del thread principale. L'annotazione @Transaction serve per indicare a
     * a Room che il metodo insertCapture() contiene più query al suo intero.
     *
     * @see MarkerHandler
     * @see CapturedCollectiblesRepository
     */
    @Insert
    @Transaction
    public void insertCapture(String collectibleName, String data, String location) {
        int collectibleID = getCollectibleIDByName(collectibleName);
        insert(new Capture(collectibleID, data, location));
    }
}