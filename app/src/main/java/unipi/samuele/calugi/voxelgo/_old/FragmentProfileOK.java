//package unipi.samuele.calugi.voxelgo._old;
//
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import unipi.samuele.calugi.voxelgo.R;
//import unipi.samuele.calugi.voxelgo.adapters.Collectible;
//import unipi.samuele.calugi.voxelgo.adapters.ListAdapter;
//import unipi.samuele.calugi.voxelgo.handlers.DatabaseHandler;
//import unipi.samuele.calugi.voxelgo.viewmodels.MainViewModel;
//
//public class FragmentProfileOK extends Fragment {
//
//    private SQLiteDatabase database;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(getContext());
//        database = databaseHandler.getReadableDatabase();
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_profile, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
////        RecyclerView collectibles = requireView().findViewById(R.id.recyclerViewCollectibles);
////        collectibles.setLayoutManager(new GridLayoutManager(getContext(), 3));
////        collectibles.setAdapter(new CursorAdapter(getContext(), getAllItems()));
//
//        RecyclerView collectibles = requireView().findViewById(R.id.recyclerViewCollectibles);
//        collectibles.setLayoutManager(new GridLayoutManager(getContext(), 3));
//
//        ListAdapter adapter = new ListAdapter(getContext(), getAllItems());
//        collectibles.setAdapter(adapter);
//
//        Observer<ArrayList<Collectible>> userListUpdateObserver = new Observer<ArrayList<Collectible>>() {
//            @Override
//            public void onChanged(ArrayList<Collectible> collectibleArrayList) {
//                adapter.updateCollectibleList(collectibleArrayList);
//            }
//        };
//
//        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
//        viewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), userListUpdateObserver);
//    }
//
//    private List<Collectible> getAllItems() {
//        List<Collectible> collectibles = new ArrayList<>();
//        collectibles.add(new Collectible("Nome1", "Modello1"));
//        collectibles.add(new Collectible("Nome2", "Modello2"));
//        collectibles.add(new Collectible("Nome3", "Modello3"));
//        collectibles.add(new Collectible("Nome4", "Modello4"));
//        collectibles.add(new Collectible("Nome5", "Modello5"));
//        return collectibles;
//    }
//}
