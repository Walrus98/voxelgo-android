package unipi.samuele.calugi.voxelgo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import unipi.samuele.calugi.voxelgo.R;
import unipi.samuele.calugi.voxelgo.fragments.FragmentCollectible;
import unipi.samuele.calugi.voxelgo.room.collectible.Collectible;

public class CollectibleAdapterProfile extends RecyclerView.Adapter<CollectibleAdapterProfile.ViewHolder> {

    /**
     * Adapter che viene utilizzato dal RecycleView del FragmentProfile. Contiene la lista di tutti i
     * collezionabili che l'utente ha catturato
     */
    private List<Collectible> collectibles;
    private final Context context;

    private final FragmentManager fragmentManager;
    private final Bundle bundle;

    public CollectibleAdapterProfile(Context context) {
        this.context = context;

        collectibles = new ArrayList<>();
        bundle = new Bundle();

        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_collectible, parent, false);
        return new ViewHolder(view);
    }

    @Override
    @SuppressLint("Range")
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Prendo il riferimento del collezionabile
        Collectible collectible = collectibles.get(position);

        // Nome del collezionabile
        final String collectibleName = collectible.getCollectibleName();
        // URL del modello 3D da visualizzare all'interno della web view
        final String collectibleModel = collectible.getCollectibleModel();
        // Rarità del modello
        final String collectibleRarity = collectible.getCollectibleRarity().toString();

        // Imposto il nome del collezionabile
        holder.titleView.setText(collectibleName);

        // Libreria utilizzata per scaricare l'immagine dalla rete e inserirla all'interno dell'imageView.
        // Grazie a Glide le immagini una volta scaricate vengono anche cachate, in modo da ridurre il consumo di rete
        Glide.with(context)
                .load(collectible.getCollectibleImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.imageView);

        // Se l'utente clicca sulla card view del collezionabile
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inserisco nel bundle gli argomenti necessari per far funzionare il FragmentCollectible
                bundle.putString("collectible_name", collectibleName);
                bundle.putString("collectible_model", collectibleModel);
                bundle.putString("collectible_rarity", collectibleRarity);

                // Inserisco il nuovo fragment nella pila di Fragment e lo mostro a schermo.
                // In questo modo quando l'utente torna indietro di schermata, torna a quella precedente
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, FragmentCollectible.class, bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return collectibles.size();
    }

    /**
     * Metodo che viene invocato dall'observer tramite callback onChange() nel FragmentProfile. Ogni volta che
     * vengono aggiunti nuovi collezionabili catturati all'interno della lista, viene chiamato il questo metodo.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setCollectibles(List<Collectible> collectibles) {
        // Aggiorno il contenuto della lista inserendo i nuovi collezionabili catturati al suo interno
        this.collectibles = collectibles;
        // Notifico che la lista di collezionabili è cambiata e quindi è necessario aggiornare la view
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardView;
        private final TextView titleView;
        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardViewCollectible);
            titleView = itemView.findViewById(R.id.textCollectible);
            imageView = itemView.findViewById(R.id.imageCollectible);
        }
    }
}
