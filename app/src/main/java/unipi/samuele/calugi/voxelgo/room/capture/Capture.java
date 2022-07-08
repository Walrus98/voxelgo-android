package unipi.samuele.calugi.voxelgo.room.capture;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;

@Entity(
    tableName = "captures",
    foreignKeys = {@ForeignKey(entity = Collectible.class,
            parentColumns = "collectible_id",
            childColumns = "collectible_id",
            onDelete = ForeignKey.CASCADE)
    },
    indices = {@Index(value = {"collectible_id"})}
)
public class Capture {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "capture_id")
    private int captureID;

    @ColumnInfo(name = "collectible_id")
    private final int collectibleID;

    @ColumnInfo(name = "capture_date")
    private final String captureDate;

    @ColumnInfo(name = "capture_location")
    private final String captureLocation;

    public Capture(int collectibleID, String captureDate, String captureLocation) {
        this.collectibleID = collectibleID;
        this.captureDate = captureDate;
        this.captureLocation = captureLocation;
    }

    public void setCaptureID(int captureID) {
        this.captureID = captureID;
    }

    public int getCaptureID() {
        return captureID;
    }

    public int getCollectibleID() {
        return collectibleID;
    }

    public String getCaptureDate() {
        return captureDate;
    }

    public String getCaptureLocation() {
        return captureLocation;
    }
}
