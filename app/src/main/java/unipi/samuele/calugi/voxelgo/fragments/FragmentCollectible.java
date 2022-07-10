package unipi.samuele.calugi.voxelgo.fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

import unipi.samuele.calugi.voxelgo.R;

public class FragmentCollectible extends Fragment implements View.OnClickListener {

    /**
     * Fragment utilizzato per mostrare il modello 3D del collezionabile catturato
     */

    private ImageButton modelRotateButton;
    private ImageButton buttonBack;
    private WebView webView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collectible, container, false);
    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Prendo gli argomenti inseriti nel bundle dal CollectibleAdapterProfile
        Bundle bundle = requireArguments();
        // Nome del collezionabile
        String collectibleName = bundle.getString("collectible_name");
        // URL del modello 3D da visualizzare all'interno della web view
        String collectibleModel = bundle.getString("collectible_model");
        // Rarità del modello
        String collectibleRarity = bundle.getString("collectible_rarity");

        // In base al tipo di modalità che l'utente sta utilizzando (darkmode o lightmode), inserisco un parametro in GET
        // nella richiesta da inviare al server. Serve per cambiare lo sfondo del collezionabile
        collectibleModel += isDarkModEnabled() ? "&mode=dark" : "&mode=light";

        // Libreria utilizzata per creare una view animata, viene utilizzata come schermata di caricamento del modello 3D.
        // Quando la webview ha terminato di renderizzare il modello, l'animazione di caricamento viene rimossa
        final LottieAnimationView lottieAnimationView = (LottieAnimationView) requireView().findViewById(R.id.lottieAnimationLoading);
        webView = (WebView) requireView().findViewById(R.id.webViewCollectible);

        // Impostazioni della webview
        WebSettings webSettings = webView.getSettings();
        // Abilito la possibilità di eseguire file script di tipo javascript all'interno della webview
        webSettings.setJavaScriptEnabled(true);
        // Abilito la manipolazione del DOM della pagina web
        webSettings.setDomStorageEnabled(true);
        // Abilito meccanismi di caching per la webview, così da diminuire il consumo di rete
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // Imposto il tipo di codifica delle pagine web che la webview dovrà caricare
        webSettings.setDefaultTextEncodingName("UTF-8");

        // Inserisco nella webview le funzionalità presenti nella webview di chrome
        webView.setWebChromeClient(new WebChromeClient());
        // Faccio l'override di due metodi dela classe webview, così da rimuovere la schermata di caricamento
        webView.setWebViewClient(new WebViewClient() {

            /**
             * Metodo che viene invcato quando la webview ha terminato di caricare la pagina
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Nascondo l'animazione di caricamento
                lottieAnimationView.setVisibility(View.GONE);
            }

            /**
             * Metodo che viene invocato quando la webview ha una pagina web cachata e termina di renderizzare
             * la schermata
             */
            @Override
            public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
                // Nascondo l'animazione di caricamento
                lottieAnimationView.setVisibility(View.GONE);
                return super.onRenderProcessGone(view, detail);
            }
        });
        // Rimuovo lo sfondo dalla webview, dato che quello viene dato dalla pagina web che viene poi renderizzata
        webView.setBackgroundColor(0);
        // Carico l'URL del collezionabile che deve essere mostrato a schermo
        webView.loadUrl(collectibleModel);

        // Nome del collezionabile
        TextView textViewCollectibleName = (TextView) requireView().findViewById(R.id.textViewCollectibleName);
        textViewCollectibleName.setText(collectibleName);

        // Rarità del collezionabile
        Button buttonRarity = (Button) requireView().findViewById(R.id.buttonRarity);
        buttonRarity.setText(collectibleRarity);

        // Pulsante per abilitare o meno la rotazione automatica del modello 3D presente all'interno della webview
        modelRotateButton = (ImageButton) requireView().findViewById(R.id.buttonRotate);
        modelRotateButton.setOnClickListener(this);

        // Pulsante per tornare indietro dal Fragment
        buttonBack = (ImageButton) requireView().findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(this);
    }

    private boolean isDarkModEnabled() {
        int nightModeFlags = requireContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    /**
     * Metodo invocato dai pulsanti buttonBack e modelRotateButton.
     */
    @Override
    public void onClick(View view) {
        // Se l'utente vuole abilitare o meno la rotazione automatica del modello 3D
        if (modelRotateButton.equals(view)) {
            // Chiamo una funzione javascript presente all'interno della pagina web caricata dalla webview
            webView.evaluateJavascript("javascript:modelRotation();", null);
        // Se l'utente vuole tornare nella schermata precedente
        } else if (buttonBack.equals(view)) {
            // Simulo il pulsante back di android, rimuovendo così dalla pila il Fragment corrente
            requireActivity().onBackPressed();
        }
    }
}
