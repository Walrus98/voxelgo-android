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

        final String collectibleName = collectible.getCollectibleName();
        final String collectibleModel = collectible.getCollectibleModel();
        final String collectibleRarity = collectible.getCollectibleRarity().toString();

        holder.titleView.setText(collectibleName);

        Glide.with(context)
                .load(collectible.getCollectibleImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return collectibles.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCollectibles(List<Collectible> collectibles) {
        this.collectibles = collectibles;
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
