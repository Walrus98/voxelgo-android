//package unipi.samuele.calugi.voxelgo.viewmodels;
//
//import android.location.Location;
//
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//import static unipi.samuele.calugi.voxelgo.utils.VoxelUtils.*;
//
//import unipi.samuele.calugi.voxelgo.R;
//
//public class MarkerHandler2 {
//
//    private static MarkerHandler2 markerHandler;
//
//    public static MarkerHandler2 getInstance(GoogleMap map) {
//        if (markerHandler == null) {
//            markerHandler = new MarkerHandler2(map);
//        }
//        return markerHandler;
//    }
//
////    private final List<Marker> markerList;
//    private final Map<Marker, MarkerOptions> markerMap;
//
//    private final Random random;
//    private Location userLocation;
//
//    private GoogleMap map;
//
//    private MarkerHandler2(GoogleMap map) {
//        this.map = map;
////        markerList = new ArrayList<>();
//        markerMap = new HashMap<>();
//        random = new Random();
//
//        for (int i = 0; i < 10; i++) {
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.jewel));
//            markerOptions.position(new LatLng(0, 0));
//            Marker marker = map.addMarker(markerOptions);
//            markerMap.put(marker, markerOptions);
//        }
//    }
//
//    public void setUserLocation(Location currentLocation) {
//        this.userLocation = currentLocation;
//        updateMarkers();
//    }
//
//    private void updateMarkers() {
//        Location markerLocation = new Location("");
//        for (Map.Entry<Marker, MarkerOptions> markerEntry : markerMap.entrySet()) {
//            Marker marker = markerEntry.getKey();
//            LatLng markerPosition = marker.getPosition();
//            markerLocation.setLongitude(markerPosition.longitude);
//            markerLocation.setLatitude(markerPosition.latitude);
//
//            float distanceInMeters = userLocation.distanceTo(markerLocation);
//            if (distanceInMeters > 20) {
//                respawnMarker(marker);
//            }
//        }
//    }
//
//    private void respawnMarker(Marker marker) {
//        double randomLat = ((double) random.nextInt(50 + 10) - 30) / 100000;
//        double randomLgt = ((double) random.nextInt(50 + 10) - 30) / 100000;
//
//        LatLng latLng = new LatLng(userLocation.getLatitude() + randomLat, userLocation.getLongitude() + randomLgt);
//        marker.setPosition(latLng);
//        marker.setTitle("Nuovo collezionabile");
//    }
//
////    public boolean removeMarker(Marker marker) {
////        Location markerLocation = new Location("");
////
////        double markerLatitude = marker.getPosition().latitude;
////        double markerLongitude = marker.getPosition().longitude;
////
////        markerLocation.setLatitude(markerLatitude);
////        markerLocation.setLongitude(markerLongitude);
////
////        float distanceInMeters = userLocation.distanceTo(markerLocation);
////
////        if (distanceInMeters <= MARKER_COLLECT_DISTANCE_METERS) {
////            MarkerOptions markerOptions = getMarkerOptionFromMarker(marker);
////            if (markerOptions != null) {
////                respawnMarker(markerOptions);
////                marker.remove();
////                return true;
////            }
////        }
////        return false;
////    }
//
//
////    private List<MarkerOptions> spawnMarkers() {
////        for (int i = 0; i < MARKER_SPAWN_RATE; i++) {
////            double randomLat = ((double) random.nextInt(50 + 10) - 30) / 100000;
////            double randomLgt = ((double) random.nextInt(50 + 10) - 30) / 100000;
////
////            LatLng latLng = new LatLng(userLocation.getLatitude() + randomLat, userLocation.getLongitude() + randomLgt);
////            MarkerOptions markerOptions = new MarkerOptions();
////            markerOptions.position(latLng);
////            markerOptions.title("Nome collezionabile");
////            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.jewel));
//////            markerOptions.infoWindowAnchor(1.5f, 1.5f);
//////            markerOptions.snippet("Ultrararo\nClicca qui per catturare!");
////
////            markerOptionsList.add(markerOptions);
////        }
////
////        return markerOptionsList;
////    }
//
////    public boolean removeMarker(Marker marker) {
////
////        double markerLatitude = marker.getPosition().latitude;
////        double markerLongitude = marker.getPosition().longitude;
////
////        markerLocation.setLatitude(markerLatitude);
////        markerLocation.setLongitude(markerLongitude);
////
////        float distanceInMeters = userLocation.distanceTo(markerLocation);
////
////        if (distanceInMeters <= MARKER_COLLECT_DISTANCE_METERS) {
////            removeMarkerOption(marker);
////            marker.remove();
////            return true;
////        }
////        return false;
////    }
//
////    private void removeMarkerOption(Marker marker) {
////        for (MarkerOptions markerOptions : markerOptionsList) {
////            if (markerOptions.getTitle().equals(marker.getTitle())) {
////                markerOptionsList.remove(markerOptions);
////                return;
////            }
////        }
////    }
//
////    public List<MarkerOptions> getMarkersFromLocation(Location location) {
////        return markerOptionsList.isEmpty() ? spawnMarkerFromLocation(location) : markerOptionsList;
////    }
////
////    private List<MarkerOptions> spawnMarkerFromLocation(Location location) {
////        for (int i = 0; i < 10; i++) {
////            double randomLat = ((double) random.nextInt(50 + 10) - 30) / 100000;
////            double randomLgt = ((double) random.nextInt(50 + 10) - 30) / 100000;
////
////            LatLng latLng = new LatLng(location.getLatitude() + randomLat, location.getLongitude() + randomLgt);
////            MarkerOptions markerOptions = new MarkerOptions();
////            markerOptions.position(latLng);
////            markerOptions.title("Marker");
////
////            markerOptionsList.add(markerOptions);
////        }
////
////        return markerOptionsList;
////    }
//}
