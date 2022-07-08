//package unipi.samuele.calugi.voxelgo.adapters;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.cardview.widget.CardView;
//import androidx.fragment.app.FragmentActivity;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//
//import java.util.List;
//
//import unipi.samuele.calugi.voxelgo.R;
//import unipi.samuele.calugi.voxelgo.fragments.FragmentCollectible;
//
//public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
//
//    private List<Collectible> collectibleList;
//    private final Context context;
//
//    public ListAdapter(Context context, List<Collectible> collectibleList) {
//        this.collectibleList = collectibleList;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_collectible, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    @SuppressLint("Range")
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//        Collectible collectible = collectibleList.get(position);
//
//        holder.titleView.setText(collectible.getCollectibleName());
//
//        Glide.with(context)
//            .load(collectible.getCollectibleImage())
//            .diskCacheStrategy(DiskCacheStrategy.DATA)
//            .into(holder.imageView);
//    }
//
//    @Override
//    public int getItemCount() {
//        return collectibleList.size();
//    }
//
//    public void updateCollectibleList(final List<Collectible> userArrayList) {
//        this.collectibleList.clear();
//        this.collectibleList = userArrayList;
//        notifyDataSetChanged();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        private final TextView titleView;
//        private final ImageView imageView;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            titleView = itemView.findViewById(R.id.textCollectible);
//            imageView = itemView.findViewById(R.id.imageCollectible);
//        }
//    }
//}
