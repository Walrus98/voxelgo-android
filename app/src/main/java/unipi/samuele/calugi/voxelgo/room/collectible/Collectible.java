package unipi.samuele.calugi.voxelgo.room.collectible;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import unipi.samuele.calugi.voxelgo.threads.DownloadThread;

/**
 * Classe dei Collezionabili, viene utilizzata:
 *
 * da Room per l'inserimento di nuovi collezionabili all'interno del database
 * @see CollectibleDao
 * @see CollectibleRepository
 *
 * dall'interfaccia DownloadThread per deserializzare la risposta HTTP inviata dal server sotto forma di json
 * @see DownloadThread
 *
 * +----------------------------+-------------------------+---------------------------+-------------------+--------------------+
 * |       collectible_id       |    collectible_name     |     collectible_model     | collectible_image | collectible_rarity |
 * +----------------------------+-------------------------+---------------------------+-------------------+--------------------+
 * | id del collezionabile      | nome del collezionabile | url del collezionabile    | url dell'immagine | rarità del modello |
 * | usato come chiave primaria |                         | contentente il modello 3D | di anteprima del  |                    |
 * | con autoincrement          |                         | usato dalla webview       | modello           |                    |
 * +----------------------------+-------------------------+---------------------------+-------------------+--------------------+
 *
 */

@Entity(
    tableName = "collectibles",
    indices = @Index(value = {"collectible_id"}, unique = true)
)
public class Collectible {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "collectible_id")
    private int collectibleID;

    @ColumnInfo(name = "collectible_name")
    private final String collectibleName;

    @ColumnInfo(name = "collectible_model")
    private final String collectibleModel;

    @ColumnInfo(name = "collectible_image")
    private final String collectibleImage;

    @ColumnInfo(name = "")
    private final RarityType collectibleRarity;

    public Collectible(String collectibleName, String collectibleModel, String collectibleImage, RarityType collectibleRarity) {
        this.collectibleName = collectibleName;
        this.collectibleModel = collectibleModel;
        this.collectibleImage = collectibleImage;
        this.collectibleRarity = collectibleRarity;
    }

    public void setCollectibleID(int collectibleID) {
        this.collectibleID = collectibleID;
    }

    public int getCollectibleID() {
        return collectibleID;
    }

    public String getCollectibleName() {
        return collectibleName;
    }

    public String getCollectibleModel() {
        return collectibleModel;
    }

    public String getCollectibleImage() {
        return collectibleImage;
    }

    public RarityType getCollectibleRarity() {
        return collectibleRarity;
    }

    public enum RarityType {
        COMMON,
        RARE,
        ULTRARARE
    }
}