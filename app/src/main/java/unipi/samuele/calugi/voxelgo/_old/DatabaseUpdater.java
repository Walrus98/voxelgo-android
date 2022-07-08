//package unipi.samuele.calugi.voxelgo.handlers;
//
//import android.annotation.SuppressLint;
//import android.content.ContentValues;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewManager;
//import android.widget.ProgressBar;
//
//import com.google.gson.Gson;
//
//import org.apache.commons.io.IOUtils;
//
//import java.io.IOException;
//import java.net.URI;
//import java.nio.charset.StandardCharsets;
//
//import unipi.samuele.calugi.voxelgo.utils.ApiResponse;
//
//@SuppressWarnings("deprecation")
//public class DatabaseUpdater extends AsyncTask<String, Integer, Void> {
//
//    private final SQLiteDatabase sqLiteDatabase;
//    private final ProgressBar progressBar;
//
//    public DatabaseUpdater(SQLiteDatabase sqLiteDatabase, ProgressBar progressBar) {
//        this.sqLiteDatabase = sqLiteDatabase;
//        this.progressBar = progressBar;
//    }
//
//    @Override
//    protected Void doInBackground(String... urls) {
//        try {
//            updateDatabase(urls[0], urls[1]);
//        } catch (IOException exception) {
//            Log.e("VoxelGO", exception.toString());
//        }
//        return null;
//    }
//
//    private void updateDatabase(String databaseCollectiblesURL, String databaseVersionURL) throws IOException {
//        int responseDatabaseVersion = Integer.parseInt(IOUtils.toString(URI.create(databaseVersionURL), StandardCharsets.UTF_8).trim());
//
//        if (responseDatabaseVersion == 1) return;
//
//        String responseCollectibles = IOUtils.toString(URI.create(databaseCollectiblesURL), StandardCharsets.UTF_8);
//        ApiResponse apiResponse = new Gson().fromJson(responseCollectibles, ApiResponse.class);
//
//        if (!apiResponse.getCode().equals("SUCCESS")) return;
//
//        ApiResponse.Model[] models = apiResponse.getModels();
//        ContentValues values = new ContentValues();
//
//        for (ApiResponse.Model model : models) {
//            values.put("collectible_name", model.getModel_name());
//            values.put("collectible_model", "https://voxel.frangioniwebdev.com/?model=" + model.getModel_url_name());
//            values.put("collectible_image", "https://cdn.discordapp.com/attachments/606422186923917322/982390254847795270/unknown.png");
//            values.put("collectible_rarity", model.getModel_rarity());
//        }
//        sqLiteDatabase.insert("collectibles", null, values);
//    }
//
//    @Override
//    protected void onProgressUpdate(Integer... values) {
//        super.onProgressUpdate(values);
//    }
//
//    @Override
//    protected void onPostExecute(Void unused) {
//        super.onPostExecute(unused);
//
//        ((ViewManager) progressBar.getParent()).removeView(progressBar);
//
////        progressBar.setVisibility(View.GONE);
//    }
//}
//
