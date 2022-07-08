package unipi.samuele.calugi.voxelgo.fragments.infoadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import unipi.samuele.calugi.voxelgo.R;
import unipi.samuele.calugi.voxelgo.room.VoxelRoomDatabase;
import unipi.samuele.calugi.voxelgo.utils.VoxelUtils;

public class CollectibleInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View viewInfoAdapter;
    private final Context context;

    public CollectibleInfoWindowAdapter(Context context) {
        this.context = context;
        viewInfoAdapter = LayoutInflater.from(context).inflate(R.layout.info_window, null);
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        TextView textViewMarkerTitle = (TextView) viewInfoAdapter.findViewById(R.id.markerTitle);
        TextView textViewMarkerDescription = (TextView) viewInfoAdapter.findViewById(R.id.markerDescription);

        textViewMarkerTitle.setText(marker.getTitle());
        textViewMarkerDescription.setText(marker.getSnippet());

        return viewInfoAdapter;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return null;
    }

//        ImageView imageViewMarker = (ImageView) viewInfoAdapter.findViewById(R.id.markerImage);

//        Glide.with(context)
//                .load(getImageURLFromName(marker.getTitle()))
//                .into(imageViewMarker);

//    private String getImageURLFromName(String collectibleName) {
//
//        collectibleName = collectibleName.replaceAll("\\s+", "-");
//        collectibleName = collectibleName.toLowerCase();
//        collectibleName += ".png";
//
//        return VoxelUtils.MODEL_IMAGE_URL  + collectibleName;
//    }
}
