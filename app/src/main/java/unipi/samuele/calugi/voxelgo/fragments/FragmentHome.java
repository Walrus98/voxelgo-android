package unipi.samuele.calugi.voxelgo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import unipi.samuele.calugi.voxelgo.R;
import unipi.samuele.calugi.voxelgo.adapters.CollectibleAdapterHome;
import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;
import unipi.samuele.calugi.voxelgo.viewmodels.HomeViewModel;

public class FragmentHome extends Fragment {

    private RecyclerView collectibles;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        collectibles = requireView().findViewById(R.id.recyclerViewCollectibles);
        collectibles.setLayoutManager(new GridLayoutManager(getContext(), 2));

        CollectibleAdapterHome adapter = new CollectibleAdapterHome(getContext());
        collectibles.setAdapter(adapter);

        HomeViewModel viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.getAllCollectibles().observe(getViewLifecycleOwner(), new Observer<List<Collectible>>() {
            @Override
            public void onChanged(List<Collectible> collectibles) {
                adapter.setCollectibles(collectibles);

                TextView textViewCollectibleQuantity = (TextView) requireView().findViewById(R.id.textViewCollectibleQuantity);
                textViewCollectibleQuantity.setText(String.valueOf(collectibles.size()));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        collectibles.setAdapter(null);
    }
}