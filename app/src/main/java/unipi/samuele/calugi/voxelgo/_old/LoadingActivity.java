//package unipi.samuele.calugi.voxelgo.activities;
//
//import android.content.ContentValues;
//import android.content.Intent;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
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
//
//public class LoadingActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//
//        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(getApplicationContext());
//        SQLiteDatabase sqLiteDatabase = databaseHandler.getWritableDatabase();
//
//        String databaseCollectiblesURL = "https://backend.voxel.frangioniwebdev.com/api.php?endpoint=models/list";
//        String databaseVersionURL = "https://backend.voxel.frangioniwebdev.com/db-version";
//
//        Handler mHandler = new Handler();
//
//        Thread thread = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    int responseDatabaseVersion = Integer.parseInt(IOUtils.toString(URI.create(databaseVersionURL), StandardCharsets.UTF_8).trim());
//
//                    if (responseDatabaseVersion != 2) {
////
//                        String responseCollectibles = IOUtils.toString(URI.create(databaseCollectiblesURL), StandardCharsets.UTF_8);
//                        ApiResponse apiResponse = new Gson().fromJson(responseCollectibles, ApiResponse.class);
//
//                        if (apiResponse.getCode().equals("SUCCESS")) {
//
//                            ApiResponse.Model[] models = apiResponse.getModels();
//                            ContentValues values = new ContentValues();
//
//                            for (ApiResponse.Model model : models) {
//                                values.put("collectible_name", model.getModel_name());
//                                values.put("collectible_model", "https://voxel.frangioniwebdev.com/?model=" + model.getModel_url_name());
//                                values.put("collectible_image", "https://cdn.discordapp.com/attachments/606422186923917322/982390254847795270/unknown.png");
//                                values.put("collectible_rarity", model.getModel_rarity());
//                            }
//                            sqLiteDatabase.insert("collectibles", null, values);
//                        }
//                    }
//                } catch (IOException exception) {
//                    Log.e("VoxelGO", exception.toString());
//
//                    mHandler.post(() -> {
//                        Toast.makeText(LoadingActivity.this, "Nessuna connessione ad internet!", Toast.LENGTH_LONG).show();
//                    });
//                }
//
//                mHandler.post(() -> {
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                });
//            }
//        });
//
//        thread.start();
//
//
////        Handler handler = new Handler();
////        handler.post(new DownloadThread(
////            database,
////            "https://backend.voxel.frangioniwebdev.com/api.php?endpoint=models/list",
////            "https://backend.voxel.frangioniwebdev.com/db-version"
////        ));
//    }
//}
