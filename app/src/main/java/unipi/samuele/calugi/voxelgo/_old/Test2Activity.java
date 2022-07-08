//package unipi.samuele.calugi.voxelgo.activities;
//
//import android.content.ContentValues;
//import android.content.Intent;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
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
//public class Test2Activity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//
////        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(getApplicationContext());
////        SQLiteDatabase sqLiteDatabase = databaseHandler.getWritableDatabase();
//
//        String databaseCollectiblesURL = "https://backend.voxel.frangioniwebdev.com/api.php?endpoint=models/list";
//        String databaseVersionURL = "https://backend.voxel.frangioniwebdev.com/db-version";
//
//        Handler mHandler = new Handler();
//
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    updateDatabase();
//                } catch (IOException exception) {
//                    Log.e("VoxelGO", exception.toString());
//                }
//            }
//
//            private void updateDatabase() throws IOException {
//
//                int responseDatabaseVersion = Integer.parseInt(IOUtils.toString(URI.create(databaseVersionURL), StandardCharsets.UTF_8).trim());
//
//                if (responseDatabaseVersion != DatabaseHandler.DATABASE_VERSION) {
//                    DatabaseHandler.DATABASE_VERSION = responseDatabaseVersion;
//                    DatabaseHandler.getInstance(getApplicationContext());
//                }
//
//                mHandler.post(() -> {
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                });
//            }
//        }).start();
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
