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

        // Istanzio la classe Gson per deserializzare i collezionabili
        gson = new Gson();
        // MutableLiveData che contiene il messaggio di errore dell'eccezione nel try-catch. Quest'ultimo viene osservato
        // da un observer nel MainActivity
        messageError = new MutableLiveData<>();
    }

    @Override
    public void run() {
        try {
            fetchAllCollectibles();
        } catch (IOException exception) {
            // Nel caso in cui non ho nessuna connessione ad internet, modifico lo stato del MutableLiveData
            // inserendoci al suo interno il messaggio di errore dato dall'eccezione.
            // In questo modo posso invocare la callback onChanged() dell'observer registrata nel MainActivity.
            // La callback se invocata mostrerà a schermo un Dialog con un messaggio di errore per l'assenza di internet.
            messageError.postValue(exception.getMessage());

            // Interrompo l'esecuzione del Thread
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Se il database non è aggiornato, il thread secondario scarica la lista di tutti i
     * collezionabili che l'utente può catturare
     *
     * @throws IOException assenza di internet
     */
    private void fetchAllCollectibles() throws IOException {

        // File presente all'interno della memoria interna del telefono che contiene la versione del database
        File file = new File(context.getFilesDir(), "database-version");
        // Risposta dal server con l'ultima versione del database
        String serverResponse = IOUtils.toString(URI.create(DATABASE_VERSION_URL), StandardCharsets.UTF_8).trim();

        // Se il file esiste, significa che l'utente ha già scaricato dei collezionabili, quindi è necessario andare a controllare
        // se le due versioni dei database coincidono
        if (file.exists()) {
            int serverDatabaseVersion = Integer.parseInt(serverResponse);
            int localDatabaseVersion = Integer.parseInt(readFile(file));

            // Se le due versioni sono differenti, significa che sono arrivati dei nuovi collezionabili che devono essere scaricati
            if (localDatabaseVersion != serverDatabaseVersion) {
                downloadCollectibles();
                writeFile(file, serverResponse);
            }
            return;
        }

        // Se il file non esiste, significa che è stata avviata per la prima volta l'applicazione ed è quindi necessario
        // scaricare tutti i collezionabili
        downloadCollectibles();
        writeFile(file, serverResponse);
    }

    /**
     * Scarica la lista di collezionabili dal server, li deserializza e li inserisce all'interno del database
     *
     * @throws IOException assenza di internet
     */
    private void downloadCollectibles() throws IOException {
        // Prendo la risposta del server che contiene la lista di tutti i collezionabili in formato JSON
        String responseCollectibles = IOUtils.toString(URI.create(DATABASE_COLLECTIONS_URL), StandardCharsets.UTF_8);
        // Deserializzo il file JSON e lo converto in un array di collezionabili
        Collectible[] collectibles = gson.fromJson(responseCollectibles, Collectible[].class);

        // Prendo il riferimento alla Repository per potere aggiungere nuovi collezionabili  all'interno del database
        CollectibleRepository repository = CollectibleRepository.getInstance(application);

        // Inserisco i collezionabili all'interno del database tramite repository
        for (Collectible collectible : collectibles) {
            repository.insert(collectible);
        }

        // Mostro all'utente una notifica per dirgli che sono stati aggiunti nuovi collezionabili
        CharSequence notificationTitle = context.getText(R.string.app_name);
        CharSequence notificationContent = context.getText(R.string.notification_content);
        // Prendo il riferimento all'istanza del notification handler
        NotificationHandler notificationHandler = NotificationHandler.getInstance(application);
        // Mostro la notifica a schermo
        notificationHandler.showNotification(notificationTitle, notificationContent);
    }

    /**
     * Legge il contenuto di un file e lo restituisce sottoforma di stringa
     */
    private String readFile(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return IOUtils.toString(fileInputStream, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            Log.e("VoxelGO ->", exception.getMessage());
        }
        throw new IllegalArgumentException("File inesistente!");
    }

    /**
     * Scrive il contenuto di una stringa all'interno di un file. Se il file non esiste ne crea uno nuovo
     */
    private void writeFile(File file, String text) {
        try {
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.write(text, outputStream, StandardCharsets.UTF_8);
        } catch (Exception exception) {
            Log.e("VoxelGO ->", exception.getMessage());
        }
    }

    /**
     * Restituisce il MutableLiveData che contiene il messaggio di errore dell'eccezione. Viene utilizzato dal MainActivity per poter osservare
     * l'eventuale messaggio di errore dato dal thread.
     */
    public MutableLiveData<String> getMessageError() {
        return messageError;
    }
}
