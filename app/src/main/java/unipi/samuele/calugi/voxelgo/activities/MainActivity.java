package unipi.samuele.calugi.voxelgo.activities;

import android.annotation.SuppressLint;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

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

import unipi.samuele.calugi.voxelgo.R;
import unipi.samuele.calugi.voxelgo.fragments.FragmentHome;
import unipi.samuele.calugi.voxelgo.fragments.FragmentMap;
import unipi.samuele.calugi.voxelgo.fragments.FragmentProfile;
import unipi.samuele.calugi.voxelgo.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private FragmentManager fragmentManager;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        replaceFragment(viewModel.getFragment());

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);

        try {
            File httpCacheDir = new File(getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException exception) {
            Log.e("VoxelGO ->", exception.getMessage());
        }

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

        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

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

    private void replaceFragment(Class<? extends Fragment> fragment) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, null, null);
        fragmentTransaction.commit();
    }
}