package unipi.samuele.calugi.voxelgo.fragments.infoadapters;

import android.annotation.SuppressLint;
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

    /**
     * View che viene mostrata quando quando l'utente clicca su un Marker
     */

    private final View viewInfoAdapter;

    @SuppressLint("InflateParams")
    public CollectibleInfoWindowAdapter(Context context) {
        viewInfoAdapter = LayoutInflater.from(context).inflate(R.layout.info_window, null);
    }

    /**
     * Metodo per modificare il contentuo della finestra
     */
    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        TextView textViewMarkerTitle = (TextView) viewInfoAdapter.findViewById(R.id.markerTitle);
        TextView textViewMarkerDescription = (TextView) viewInfoAdapter.findViewById(R.id.markerDescription);

        // Nome del collezionabile
        textViewMarkerTitle.setText(marker.getTitle());
        // Rarità del collezionabile
        textViewMarkerDescription.setText(marker.getSnippet());

        return viewInfoAdapter;
    }

    /**
     * Metodo per modificare la finestra.
     */
    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return null;
    }
}
