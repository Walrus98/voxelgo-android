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

    /**
     * Fragment utilizzato per mostrare a schermo quali sono i collezionabili che l'utente può catturare.
     */

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

        // RecycleView utilizzato per mostrare i collezionabili che l'utente può catturare
        collectibles = requireView().findViewById(R.id.recyclerViewCollectibles);
        // Imposto un LayoutManager a Griglia con 2 elmenti per riga
        collectibles.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Istanzio un RecyclerViewAdapter che si occupa di prendere l'insieme di collezionabili da passare poi al RecyclerView
        CollectibleAdapterHome adapter = new CollectibleAdapterHome(getContext());
        collectibles.setAdapter(adapter);

        // Istanzio il ViewModel del FragmentHome e prendo sotto forma di LiveData la lista di collezionabili dal database
        HomeViewModel viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Essendo la struttura dati di tipo LiveData, attraverso l'utilizzo di un Observer posso osservare cambiamenti sulla lista.
        // Se arrivano nuovi collezionabili all'interno della lista (per esempio vengono scaricati e inseriti dal thread secondario),
        // viene invocata la callback onChanged() che va ad aggiornare la lista di collezionabili dell'adapter con quella nuova
        viewModel.getAllCollectibles().observe(getViewLifecycleOwner(), new Observer<List<Collectible>>() {
            @Override
            public void onChanged(List<Collectible> collectibles) {
                adapter.setCollectibles(collectibles);

                // TextView che mostra nel FragmentHome il numero totale di collezionabili che l'utente può andare a catturare
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