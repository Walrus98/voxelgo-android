package unipi.samuele.calugi.voxelgo.utils;

public class VoxelUtils {

    public static final String NOTIFICATION_NAME = "VoxelGO";
    public static final String NOTIFICATION_CHANNEL = "voxelgo_channel";

    public static final String DATABASE_NAME = "voxelgo";
    public static final int DATABASE_VERSION = 2;

    public static final String DATABASE_VERSION_URL = "https://backend.voxel.frangioniwebdev.com/db-version";
    public static final String DATABASE_COLLECTIONS_URL = "https://backend.voxel.frangioniwebdev.com/api.php?endpoint=models/list";

    // Numero di Marker che vengono spawnati attorno all'utente
    public static final int MARKER_SPAWN_AMOUNT = 7;

    // Distanza massima fra l'utente e il Marker per poterlo catturare
    public static final int MARKER_COLLECT_DISTANCE = 200;

    // Distanza necessaria percorsa dall'utente per aggiornare la posizione della telecamera
    public static final int CAMERA_DISTANCE_METERS = 15;

    // Distanza necessaria percorsa dall'utente per aggironare graficamente la poszione dei Marker
    public static final float MARKER_UPDATE_DISTANCE = 20;

    // Distanza massima fra l'utente e il Marker per poterlo respawnare
    public static final float MARKER_SPAWN_DISTANCE = 50;

}
