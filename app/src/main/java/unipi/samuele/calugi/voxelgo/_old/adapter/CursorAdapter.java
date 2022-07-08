package unipi.samuele.calugi.voxelgo._old.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
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

import unipi.samuele.calugi.voxelgo.R;
import unipi.samuele.calugi.voxelgo.fragments.FragmentCollectible;

public class CursorAdapter extends RecyclerView.Adapter<CursorAdapter.ViewHolder> implements View.OnClickListener {

    private final Context context;
    private final Cursor cursor;

    private String collectibleName;
    private String collectibleModel;
    private final Bundle bundle;

    private final FragmentManager fragmentManager;

    private final int indexCollectibleName;
    private final int indexCollectibleModel;
    private final int indexCollectibleImage;

    public CursorAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;

        bundle = new Bundle();

        indexCollectibleName = cursor.getColumnIndex("collectible_name");
        indexCollectibleModel = cursor.getColumnIndex("collectible_model");
        indexCollectibleImage = cursor.getColumnIndex("collectible_image");

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

        if (!cursor.moveToPosition(position)) {
            return;
        }

        collectibleName = cursor.getString(indexCollectibleName);

        collectibleModel = cursor.getString(indexCollectibleModel);

        String collectibleImage = cursor.getString(indexCollectibleImage);

        holder.titleView.setText(collectibleName);

        Glide.with(context)
            .load(collectibleImage)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(holder.imageView);

        holder.cardView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    @Override
    public void onClick(View view) {

        bundle.putString("CollectibleName", collectibleName);
        bundle.putString("CollectibleModel", collectibleModel);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, FragmentCollectible.class, bundle, "fragment_collectible");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
