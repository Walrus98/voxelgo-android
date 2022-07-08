package unipi.samuele.calugi.voxelgo.handlers;

import android.app.Application;
import android.location.Location;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import unipi.samuele.calugi.voxelgo.R;
import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;
import unipi.samuele.calugi.voxelgo.utils.VoxelUtils;

import static unipi.samuele.calugi.voxelgo.utils.VoxelUtils.*;

public class MarkerHandler {

    private static MarkerHandler markerHandler;

    public static MarkerHandler getInstance(Application application) {
        if (markerHandler == null) {
            markerHandler = new MarkerHandler(application);
        }
        return markerHandler;
    }

    private final List<MarkerOptions> markerOptionsList;
    private final Random random;

    private final Location markerLocation;
    private Location userLocation;

    private final CollectiblesHandler collectiblesHandler;

    private MarkerHandler(Application application) {
        collectiblesHandler = new CollectiblesHandler(application);
        markerOptionsList = new ArrayList<>();
        markerLocation = new Location("");
        random = new Random();

        for (int i = 0; i < MARKER_SPAWN_AMOUNT; i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptionsList.add(markerOptions);
        }
    }

    public void setUserLocation(Location currentLocation) {
        this.userLocation = currentLocation;
        updateMarkers();
    }

    private void updateMarkers() {
        for (MarkerOptions markerOptions : markerOptionsList) {
            LatLng markerPosition = markerOptions.getPosition();

            if (markerPosition == null) {
                respawnMarker(markerOptions);
                continue;
            }

            markerLocation.setLongitude(markerPosition.longitude);
            markerLocation.setLatitude(markerPosition.latitude);

            float distanceInMeters = userLocation.distanceTo(markerLocation);
            if (distanceInMeters > VoxelUtils.MARKER_SPAWN_DISTANCE) {
                respawnMarker(markerOptions);
            }
        }
    }

    private void respawnMarker(MarkerOptions markerOptions) {
        double randomLat = ((double) random.nextInt(70 + 10) - 30) / 100000;
        double randomLgt = ((double) random.nextInt(70 + 10) - 30) / 100000;

        Collectible collectible = collectiblesHandler.getRandomCollectible(random);

        LatLng latLng = new LatLng(userLocation.getLatitude() + randomLat, userLocation.getLongitude() + randomLgt);
        markerOptions.position(latLng);
        markerOptions.title(collectible.getCollectibleName());

        Collectible.RarityType rarityType = collectible.getCollectibleRarity();
        switch (collectible.getCollectibleRarity()) {
            case COMMON:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_common));
                break;
            case RARE:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_rare));
                break;
            case ULTRARARE:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_ultrarare));
                break;
        }

        markerOptions.snippet(rarityType.toString());
    }

    public boolean removeMarker(Marker marker) {
        double markerLatitude = marker.getPosition().latitude;
        double markerLongitude = marker.getPosition().longitude;

        markerLocation.setLatitude(markerLatitude);
        markerLocation.setLongitude(markerLongitude);

        float distanceInMeters = userLocation.distanceTo(markerLocation);

        if (distanceInMeters <= MARKER_COLLECT_DISTANCE) {
            MarkerOptions markerOptions = getMarkerOptionFromMarker(marker);
            collectiblesHandler.submitNewCapture(markerOptions);
            respawnMarker(markerOptions);
            return true;
        }
        return false;
    }

    private MarkerOptions getMarkerOptionFromMarker(Marker marker) {
        for (MarkerOptions markerOptions : markerOptionsList) {
            if (markerOptions.getPosition().equals(marker.getPosition())) {
                return markerOptions;
            }
        }
        throw new IllegalArgumentException("Non esiste nessun Marker associato ad un MarkerOptions!");
    }

    public List<MarkerOptions> getMarkers() {
        return markerOptionsList;
    }

    public void destroy() {
        collectiblesHandler.destroyObserver();
    }

//    private MarkerOptions getMarkerOptionFromMarker(Marker marker) {
//        for (MarkerOptions markerOptions : markerOptionsList) {
//            if (markerOptions.getTitle().equals(marker.getTitle())) {
//                return markerOptions;
//            }
//        }
//        return null;
//    }

//    private List<MarkerOptions> spawnMarkers() {
//        for (int i = 0; i < MARKER_SPAWN_RATE; i++) {
//            double randomLat = ((double) random.nextInt(50 + 10) - 30) / 100000;
//            double randomLgt = ((double) random.nextInt(50 + 10) - 30) / 100000;
//
//            LatLng latLng = new LatLng(userLocation.getLatitude() + randomLat, userLocation.getLongitude() + randomLgt);
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title("Nome collezionabile");
//            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.jewel));
////            markerOptions.infoWindowAnchor(1.5f, 1.5f);
////            markerOptions.snippet("Ultrararo\nClicca qui per catturare!");
//
//            markerOptionsList.add(markerOptions);
//        }
//
//        return markerOptionsList;
//    }

//    public boolean removeMarker(Marker marker) {
//
//        double markerLatitude = marker.getPosition().latitude;
//        double markerLongitude = marker.getPosition().longitude;
//
//        markerLocation.setLatitude(markerLatitude);
//        markerLocation.setLongitude(markerLongitude);
//
//        float distanceInMeters = userLocation.distanceTo(markerLocation);
//
//        if (distanceInMeters <= MARKER_COLLECT_DISTANCE_METERS) {
//            removeMarkerOption(marker);
//            marker.remove();
//            return true;
//        }
//        return false;
//    }

//    private void removeMarkerOption(Marker marker) {
//        for (MarkerOptions markerOptions : markerOptionsList) {
//            if (markerOptions.getTitle().equals(marker.getTitle())) {
//                markerOptionsList.remove(markerOptions);
//                return;
//            }
//        }
//    }

//    public List<MarkerOptions> getMarkersFromLocation(Location location) {
//        return markerOptionsList.isEmpty() ? spawnMarkerFromLocation(location) : markerOptionsList;
//    }
//
//    private List<MarkerOptions> spawnMarkerFromLocation(Location location) {
//        for (int i = 0; i < 10; i++) {
//            double randomLat = ((double) random.nextInt(50 + 10) - 30) / 100000;
//            double randomLgt = ((double) random.nextInt(50 + 10) - 30) / 100000;
//
//            LatLng latLng = new LatLng(location.getLatitude() + randomLat, location.getLongitude() + randomLgt);
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title("Marker");
//
//            markerOptionsList.add(markerOptions);
//        }
//
//        return markerOptionsList;
//    }
}
