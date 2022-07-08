//package unipi.samuele.calugi.voxelgo._old;
//
//import android.os.Handler;
//
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import java.util.ArrayList;
//
//import unipi.samuele.calugi.voxelgo.adapters.Collectible;
//
//public class MainViewModelOK extends ViewModel {
//
//    private final MutableLiveData<ArrayList<Collectible>> collectibleLiveData;
//
//    public MainViewModelOK() {
//        collectibleLiveData = new MutableLiveData<>();
//
//        ArrayList<Collectible> collectibleArrayList = new ArrayList<>();
//
//        Handler mHandler = new Handler();
//        Thread thread = new Thread(() -> {
//            try {
//                Thread.sleep(5000L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            collectibleArrayList.add(new Collectible("", ""));
//            collectibleArrayList.add(new Collectible("", ""));
//            collectibleArrayList.add(new Collectible("", ""));
//
//            mHandler.post(() -> {
//                collectibleLiveData.setValue(collectibleArrayList);
//            });
//        });
//        thread.start();
//    }
//
//    public MutableLiveData<ArrayList<Collectible>> getUserMutableLiveData(){
//        return collectibleLiveData;
//    }
//
//
//}