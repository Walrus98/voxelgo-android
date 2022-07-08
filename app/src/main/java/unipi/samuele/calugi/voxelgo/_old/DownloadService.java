//package unipi.samuele.calugi.voxelgo.services;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.Service;
//import android.content.ContentValues;
//import android.content.Intent;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Build;
//import android.os.IBinder;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.NotificationCompat;
//
//import com.google.gson.Gson;
//
//import org.apache.commons.io.IOUtils;
//
//import java.io.IOException;
//import java.net.URI;
//import java.nio.charset.StandardCharsets;
//
//import unipi.samuele.calugi.voxelgo.R;
//import unipi.samuele.calugi.voxelgo.handlers.DatabaseHandler;
//import unipi.samuele.calugi.voxelgo.utils.ApiResponse;
//import unipi.samuele.calugi.voxelgo.utils.VoxelUtils;
//
//public class DownloadService extends Service {
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        createNotificationChannel();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//
//        Thread thread = new Thread(this::checkDatabaseVersion);
//        thread.start();
////        String databaseCollectiblesURL = "https://backend.voxel.frangioniwebdev.com/api.php?endpoint=models/list";
////        String databaseVersionURL = "https://backend.voxel.frangioniwebdev.com/db-version";
////
////        Thread thread = new Thread(new Runnable() {
////
////            @Override
////            public void run() {
////                try {
////
////                    int responseDatabaseVersion = Integer.parseInt(IOUtils.toString(URI.create(databaseVersionURL), StandardCharsets.UTF_8).trim());
////
////                    if (responseDatabaseVersion != 2) {
//////
////                        String responseCollectibles = IOUtils.toString(URI.create(databaseCollectiblesURL), StandardCharsets.UTF_8);
////                        ApiResponse apiResponse = new Gson().fromJson(responseCollectibles, ApiResponse.class);
////
////                        if (apiResponse.getCode().equals("SUCCESS")) {
////
////                            ApiResponse.Model[] models = apiResponse.getModels();
////                            ContentValues values = new ContentValues();
////
////                            for (ApiResponse.Model model : models) {
////                                values.put("collectible_name", model.getModel_name());
////                                values.put("collectible_model", "https://voxel.frangioniwebdev.com/?model=" + model.getModel_url_name());
////                                values.put("collectible_image", "https://cdn.discordapp.com/attachments/606422186923917322/982390254847795270/unknown.png");
////                                values.put("collectible_rarity", model.getModel_rarity());
////                            }
////                            sqLiteDatabase.insert("collectibles", null, values);
////                        }
////                    }
////                } catch (IOException exception) {
////                    Log.e("VoxelGO", exception.toString());
////                }
////            }
////        });
////
////        thread.start();
//        return START_NOT_STICKY;
//    }
//
//    private void createNotificationChannel() {
//        NotificationChannel channel = new NotificationChannel("VoxelGOChannel", "Name", NotificationManager.IMPORTANCE_DEFAULT);
//        channel.setDescription("Description");
//        NotificationManager notificationManager = getSystemService(NotificationManager.class);
//        notificationManager.createNotificationChannel(channel);
//    }
//
//    private void checkDatabaseVersion() {
//
//        while (true) {
//
//            try {
//                int responseDatabaseVersion = Integer.parseInt(IOUtils.toString(URI.create(VoxelUtils.DATABASE_VERSION_URL), StandardCharsets.UTF_8).trim());
//
//                Log.e("AAAAAAAAAAAAA", responseDatabaseVersion + "");
//                if (responseDatabaseVersion == 1) {
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "VoxelGOChannel")
//                            .setSmallIcon(R.drawable.ic_baseline_home_24)
//                            .setContentTitle("Titolo di prova")
//                            .setContentText("Messaggio di prova")
//                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                }
//
//
//                Thread.sleep(5000L);
//
//            } catch (IOException | InterruptedException exception) {
//                Log.e("AAAAAAAAAAAAA", exception.toString());
//            }
//        }
//    }
//
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//}
