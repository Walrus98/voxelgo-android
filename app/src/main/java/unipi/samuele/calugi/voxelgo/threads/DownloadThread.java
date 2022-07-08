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

    /**
     * Thread secondario che si occupa di scaricare i collezionabili dal server.
     *
     * Quando quest'ultimo viene avviato, il thread contatta prima l'endpoint DATABASE_VERSION_URL per controllare se la versione del database
     * che l'utente possiede è aggiornata.
     * Nel caso in cui l'utente non possiede l'ultima versione del database, il Thread contatta l'endpoint DATABASE_COLLECTIONS_URL che restituisce
     * un file in formato json contente la lista di tutti i collezionabili che l'utente può catturare.
     * A questo punto il file json viene deserializzato sottoforma di classe Collectble (tramite liberia Gson) e ogni nuovo collezionabile viene
     * inserito all'interno del database tramite Repository.
     *
     * @see DATABASE_VERSION_URL versione del database
     * @see DATABASE_COLLECTIONS_URL lista dei collezionabili in json
     * @see Collectible utilizzata per la deserializzazione
     * @see CollectibleRepository utilizzata per inserire i collezionabili all'interno del database
     *
     * Questo Thread viene eseguito all'avvio dell'applicazione, o può essere lanciato dalla classe MainActivity con il Dialog dell'assenza di internet
     *
     */

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
