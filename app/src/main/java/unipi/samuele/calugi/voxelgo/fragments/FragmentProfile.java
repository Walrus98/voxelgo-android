package unipi.samuele.calugi.voxelgo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import unipi.samuele.calugi.voxelgo.R;
import unipi.samuele.calugi.voxelgo.adapters.CollectibleAdapterProfile;
import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;
import unipi.samuele.calugi.voxelgo.viewmodels.ProfileViewModel;

public class FragmentProfile extends Fragment {

    private RecyclerView collectibles;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        collectibles = requireView().findViewById(R.id.recyclerViewCollectibles);
        collectibles.setLayoutManager(new GridLayoutManager(getContext(), 2));

        CollectibleAdapterProfile adapter = new CollectibleAdapterProfile(getContext());
        collectibles.setAdapter(adapter);

        ProfileViewModel viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        viewModel.getAllCapturedCollectibles().observe(getViewLifecycleOwner(), new Observer<List<Collectible>>() {
            @Override
            public void onChanged(List<Collectible> capturedCollectibles) {
                adapter.setCollectibles(capturedCollectibles);

                if (!capturedCollectibles.isEmpty()) {
                    LinearLayout linearLayout = requireView().findViewById(R.id.linearLayoutProfile);
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        collectibles.setAdapter(null);
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//    }
}


//        RecyclerView collectibles = requireView().findViewById(R.id.recyclerViewCollectibles);
//        collectibles.setLayoutManager(new GridLayoutManager(getContext(), 3));
//        collectibles.setAdapter(new CursorAdapter(getContext(), getAllItems()));
//
//        MainViewModel viewModel = new ViewModelProvider(this, new MainViewModelFactory(database)).get(MainViewModel.class);
//        viewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Cursor>() {
//            @Override
//            public void onChanged(Cursor newCursor) {
//                adapter.updateCursor(newCursor);
//            }
//        });
//    private Cursor getAllItems() {
//        return database.rawQuery("SELECT * FROM collectibles", null);
//    }
