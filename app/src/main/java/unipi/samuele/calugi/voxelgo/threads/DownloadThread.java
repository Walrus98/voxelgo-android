package unipi.samuele.calugi.voxelgo.threads;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static unipi.samuele.calugi.voxelgo.utils.VoxelUtils.*;

import androidx.lifecycle.MutableLiveData;

import unipi.samuele.calugi.voxelgo.R;
import unipi.samuele.calugi.voxelgo.handlers.NotificationHandler;
import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;
import unipi.samuele.calugi.voxelgo.room.collectible.CollectibleRepository;

public class DownloadThread implements Runnable {

    private final Application application;
    private final Context context;

    private final Gson gson;
    private final MutableLiveData<String> messageError;

    public DownloadThread(Application application) {
        this.application = application;
        context = application.getApplicationContext();

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        messageError = new MutableLiveData<>();
    }

    @Override
    public void run() {
        try {
            fetchAllCollectibles();
        } catch (IOException exception) {
            messageError.postValue(exception.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    private void fetchAllCollectibles() throws IOException {

        File file = new File(context.getFilesDir(), "database-version");
        String serverResponse = IOUtils.toString(URI.create(DATABASE_VERSION_URL), StandardCharsets.UTF_8).trim();

        if (file.exists()) {
            int serverDatabaseVersion = Integer.parseInt(serverResponse);
            int localDatabaseVersion = Integer.parseInt(readFile(file));

            if (localDatabaseVersion != serverDatabaseVersion) {
                downloadCollectibles();
                writeFile(file, serverResponse);
            }
            return;
        }

        downloadCollectibles();
        writeFile(file, serverResponse);
    }

    private void downloadCollectibles() throws IOException {
        String responseCollectibles = IOUtils.toString(URI.create(DATABASE_COLLECTIONS_URL), StandardCharsets.UTF_8);
        Collectible[] models = gson.fromJson(responseCollectibles, Collectible[].class);

        CollectibleRepository repository = CollectibleRepository.getInstance(application);

        for (Collectible collectible : models) {
            repository.insert(collectible);
        }

        CharSequence notificationTitle = context.getText(R.string.app_name);
        CharSequence notificationContent = context.getText(R.string.notification_content);
        new NotificationHandler(application).showNotification(notificationTitle, notificationContent);
    }

    private String readFile(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return IOUtils.toString(fileInputStream, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            Log.e("VoxelGO ->", exception.getMessage());
        }
        throw new IllegalArgumentException("File inesistente!");
    }

    private void writeFile(File file, String text) {
        try {
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.write(text, outputStream, StandardCharsets.UTF_8);
        } catch (Exception exception) {
            Log.e("VoxelGO ->", exception.getMessage());
        }
    }
}
