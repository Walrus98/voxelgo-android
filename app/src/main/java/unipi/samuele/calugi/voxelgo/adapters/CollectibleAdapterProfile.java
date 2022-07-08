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

        Collectible collectible = collectibles.get(position);

        final String collectibleName = collectible.getCollectibleName();
        final String collectibleModel = collectible.getCollectibleModel();
        final String collectibleRarity = collectible.getCollectibleRarity().toString();

        holder.titleView.setText(collectibleName);

        Glide.with(context)
                .load(collectible.getCollectibleImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle.putString("collectible_name", collectibleName);
                bundle.putString("collectible_model", collectibleModel);
                bundle.putString("collectible_rarity", collectibleRarity);

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

    @SuppressLint("NotifyDataSetChanged")
    public void setCollectibles(List<Collectible> collectibles) {
        this.collectibles = collectibles;
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
