package unipi.samuele.calugi.voxelgo.fragments.infoadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import unipi.samuele.calugi.voxelgo.R;

public class CollectibleInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View viewInfoAdapter;

    public CollectibleInfoWindowAdapter(Context context) {
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
}
