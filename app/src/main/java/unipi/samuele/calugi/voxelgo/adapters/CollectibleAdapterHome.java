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

public class CollectibleAdapterHome extends RecyclerView.Adapter<CollectibleAdapterHome.ViewHolder> {

    /**
     * Adapter che viene utilizzato dal RecycleView del FragmentHome. Contiene la lista di tutti i
     * collezionabili che l'utente può catturare
     */

    private List<Collectible> collectibles;
    private final Context context;

    public CollectibleAdapterHome(Context context) {
        this.context = context;

        collectibles = new ArrayList<>();
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

        Collectible collectible = collectibles.get(position);

        holder.titleView.setText(collectible.getCollectibleName());

        Glide.with(context)
                .load(collectible.getCollectibleImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return collectibles.size();
    }

    /**
     * Metodo che viene invocato dall'observer tramite callback onChange() nel FragmentHome. Ogni volta che
     * vengono aggiunti nuovi collezionabili all'interno della lista, viene chiamato il questo metodo.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setCollectibles(List<Collectible> collectibles) {
        // Aggiorno il contenuto della lista inserendo i nuovi collezionabili al suo interno
        this.collectibles = collectibles;
        // Notifico che la lista di collezionabili è cambiata e quindi è necessario aggiornare la view
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleView;
        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.textCollectible);
            imageView = itemView.findViewById(R.id.imageCollectible);
        }
    }
}
