package unipi.samuele.calugi.voxelgo.activities;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import unipi.samuele.calugi.voxelgo.R;
import unipi.samuele.calugi.voxelgo.fragments.FragmentCollectible;
import unipi.samuele.calugi.voxelgo.fragments.FragmentHome;
import unipi.samuele.calugi.voxelgo.fragments.FragmentMap;
import unipi.samuele.calugi.voxelgo.fragments.FragmentProfile;
import unipi.samuele.calugi.voxelgo.handlers.MarkerHandler;
import unipi.samuele.calugi.voxelgo.room.VoxelRoomDatabase;
import unipi.samuele.calugi.voxelgo.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private FragmentManager fragmentManager;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        // Istanzio il ViewModel della classe MainActivity
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        // - se c'è qualcosa nel bundle, significa che l'utente prima si trovava nel FragmentCollectible,
        // il metodo replaceFragment non deve essere chiamato
        // - se il bundle è vuoto, significa che l'utentte prima si trovava in uno dei fragment della bottom navigation bar,
        // quindi posso chiamare il metodo replace fragment
        if (savedInstanceState == null || savedInstanceState.getString("fragment_collectible") == null) {
            // Mostro a schermo uno dei fragment della barra di navigazione
            replaceFragment(viewModel.getFragment());
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);

        // Alloco 10MiB di cache per memorizzare le risposte delle richieste HTTP
        try {
            File httpCacheDir = new File(getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException exception) {
            Log.e("VoxelGO ->", exception.getMessage());
        }

        // MutableLiveData che viene utilizzato all'interno del Thread che si occupa di scaricare i collezionabili
        // dal server. Se il Thread non riesce a scaricarli per mancanza di internet, viene lanciata un'eccezione. Tale eccezione
        // viene memorizzata all'interno del MutableLiveData. Attraverso un Observer, quando il MutableLiveData cambia valore
        // (quindi è stata lanciata un eccezione dal Thread), viene invocata la callback onChanged(). Questo metodo si occupa
        // di andare a mostrare a schermo un Dialog che notifica l'assenza di rete. Se l'utente decide di riprovare a scaricare i modelli,
        // viene lanciata una nuova istanza del Thread
        viewModel.getMessageError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String messageError) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getApplicationContext().getText(R.string.dialog_title_no_internet))
                        .setMessage(getApplicationContext().getText(R.string.dialog_content_no_internet))
                        .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                            viewModel.startThread();
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                Log.e("VoxelGO ->", messageError);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Quando l'activity viene terminata, pulisco la cache
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    /**
     * Metodo utilizzato come listener dalla BottomNavigationBar, quando l'utente clicca su uno
     * dei pulsanti, viene sostituito il Fragment e viene memorizato sul MainActivityViewModel
     * quale Fragment l'utente sta visualizzando.
     *
     * @see MainActivityViewModel
     */
    @Override
    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.buttonHome:
                replaceFragment(FragmentHome.class);
                viewModel.setFragment(FragmentHome.class);
                break;
            case R.id.buttonExplore:
                replaceFragment(FragmentMap.class);
                viewModel.setFragment(FragmentMap.class);
                break;
            case R.id.buttonProfile:
                replaceFragment(FragmentProfile.class);
                viewModel.setFragment(FragmentProfile.class);
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Se mi trovo all'interno del Fragment Collectible
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment instanceof FragmentCollectible) {
            // Inserisco un valore all'interno del bundle, in questo modo quando viene richiamato il metodo onCreate della MainActivty
            // non vado a sostituire il Fragment con uno della bottom navigation bar
            outState.putString("fragment_collectible", "collectible");
        }
    }

    /**
     * Metodo utilizzato dalla BottomNavigationBar per cambiare il Fragment che si vuole mostrare a schermo
     * @param fragment il File.class del Fragment che si vuole far visualizzare
     */
    private void replaceFragment(Class<? extends Fragment> fragment) {
        // Elimino la pila di fragment, in questo modo quando l'utente decide di tornare indietro dopo aver cliccato
        // su uno dei pulsanti della BottomNavigationBar, l'applicazione viene chiusa
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // Sostituisco il Fragment con quello passato per parametro
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, null, null);
        fragmentTransaction.commit();
    }
}