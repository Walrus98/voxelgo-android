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

    /**
     * Fragment utilizzato per mostrare a schermo quali sono i collezionabili che l'utente ha catturato.
     */

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

        // RecycleView utilizzato per mostrare i collezionabili che l'utente ha catturato
        collectibles = requireView().findViewById(R.id.recyclerViewCollectibles);
        // Imposto un LayoutManager a Griglia con 2 elmenti per riga
        collectibles.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Istanzio un RecyclerViewAdapter che si occupa di prendere l'insieme di collezionabili da passare poi al RecyclerView
        CollectibleAdapterProfile adapter = new CollectibleAdapterProfile(getContext());
        collectibles.setAdapter(adapter);

        // Istanzio il ViewModel del FragmentProfile e prendo sotto forma di LiveData la lista di collezionabili dal database
        ProfileViewModel viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Essendo la struttura dati di tipo LiveData, attraverso l'utilizzo di un Observer posso osservare cambiamenti sulla lista.
        // Se arrivano nuovi collezionabili catturati all'interno della lista, viene invocata la callback onChanged() che va ad aggiornare la lista
        // di collezionabili dell'adapter con quella nuova
        viewModel.getAllCapturedCollectibles().observe(getViewLifecycleOwner(), new Observer<List<Collectible>>() {
            @Override
            public void onChanged(List<Collectible> capturedCollectibles) {
                adapter.setCollectibles(capturedCollectibles);

                // Se la lista è vuota, significa che l'utente non ha catturato nessun collezionabile. Quindi mostro
                // Il messaggio di "nessun collezionabile catturato" a schermo
                // Se la lista non è vuota
                if (!capturedCollectibles.isEmpty()) {
                    // Nascondo la view
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
}