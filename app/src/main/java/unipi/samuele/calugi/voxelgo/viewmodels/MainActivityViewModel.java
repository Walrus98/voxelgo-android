package unipi.samuele.calugi.voxelgo.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import unipi.samuele.calugi.voxelgo.fragments.FragmentHome;
import unipi.samuele.calugi.voxelgo.threads.DownloadThread;

public class MainActivityViewModel extends AndroidViewModel {

    /**
     * Il MainActivityViewModel viene istanziato all'avvio del MainActivty e rimane in memoria per tutto il ciclo di vita dell'Activity, ovvero finché
     * non viene distrutta con la callback onDestroy().
     * Quando viene ruotato lo schermo, l'Activity esegue i metodi onPause(), onStop(), onDestroy() e successivamente continua il suo flusso di esecuzione
     * ripartendo con la callback onCreate(). Questo significa che se l'utente ruota lo schermo, viene anche ricaricata la View. Per memorizzare
     * il Fragment che l'utente stava visualizzando prima di eseguire la rotazione, è stata utilizzata la classe ViewModel.
     *
     * Oltre a memorizzare il Fragment, il ViewModel si occupa anche di lanciare il Thread che ha lo scopo di scaricare tutti i collezionabili presenti
     * sul server. In questo modo viene lanciata una sola istanza del Thread e si evitano problemi di concorrenza.
     *
     * @see DownloadThread
     * @see "https://developer.android.com/topic/libraries/architecture/viewmodel"
     */

    // Field utilizzato per memorizzare quale Fragment mostrare a schermo
    private Class<? extends Fragment> fragment;

    // Livedata utilizzato in caso di errore da parte del Thread
    private final LiveData<String> messageError;

    // Interfaccia implementata dal Thread per scaricare i collezionabili dal server
    private final DownloadThread downloadThread;

    public MainActivityViewModel(Application application) {
        super(application);

        // Imposto il Fragment da visualizzare all'avvio dell'applicazione
        fragment = FragmentHome.class;

        // Faccio partitre il Thread per scaricare i collezionabili
        downloadThread = new DownloadThread(application);
        messageError = downloadThread.getMessageError();
        startThread();
    }

    // Metodo che viene chiamato dalla classe MainActivity per memorizzare il Fragment da mostrare a schermo
    public void setFragment(Class<? extends Fragment> fragment) {
        this.fragment = fragment;
    }

    // Metodo che viene chiamato dalla classe MainActivity per sapere quale Fragment andare a mostrare a schermo
    public Class<? extends Fragment> getFragment() {
        return fragment;
    }

    // Livedata utilizzato in caso di errore da parte del Thread
    public LiveData<String> getMessageError() {
        return messageError;
    }

    // Metodo utilizzato per far partire un'istanza del Thread, viene invocato una volta nel costruttore del ViewModel o
    // viene invocato dalla classe MainActivity con il Dialog dell'assenza di internet
    public void startThread() {
        new Thread(downloadThread).start();
    }

}
