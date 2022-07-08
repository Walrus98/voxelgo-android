package unipi.samuele.calugi.voxelgo.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import unipi.samuele.calugi.voxelgo.fragments.FragmentHome;
import unipi.samuele.calugi.voxelgo.threads.DownloadThread;

public class MainActivityViewModel extends AndroidViewModel {

    private Class<? extends Fragment> fragment;

    private final LiveData<String> messageError;
    private final DownloadThread downloadThread;

    public MainActivityViewModel(Application application) {
        super(application);
        fragment = FragmentHome.class;

        downloadThread = new DownloadThread(application);
        messageError = downloadThread.getMessageError();
        startThread();
    }

    public void setFragment(Class<? extends Fragment> fragment) {
        this.fragment = fragment;
    }

    public Class<? extends Fragment> getFragment() {
        return fragment;
    }

    public LiveData<String> getMessageError() {
        return messageError;
    }

    public void startThread() {
        new Thread(downloadThread).start();
    }

}
