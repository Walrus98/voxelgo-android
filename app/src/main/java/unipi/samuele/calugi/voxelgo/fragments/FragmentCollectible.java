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

public class FragmentCollectible extends Fragment {

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

        Bundle bundle = requireArguments();
        String collectibleName = bundle.getString("collectible_name");
        String collectibleModel = bundle.getString("collectible_model");
        String collectibleRarity = bundle.getString("collectible_rarity");

        collectibleModel += isDarkModEnabled() ? "&mode=dark" : "&mode=light";

        final LottieAnimationView lottieAnimationView = (LottieAnimationView) requireView().findViewById(R.id.lottieAnimationLoading);
        final WebView webView = (WebView) requireView().findViewById(R.id.webViewCollectible);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setDefaultTextEncodingName("UTF-8");

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                lottieAnimationView.setVisibility(View.GONE);
            }

            @Override
            public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
                lottieAnimationView.setVisibility(View.GONE);
                return super.onRenderProcessGone(view, detail);
            }
        });
        webView.setBackgroundColor(0);
        webView.loadUrl(collectibleModel);

        TextView textViewCollectibleName = (TextView) requireView().findViewById(R.id.textViewCollectibleName);
        textViewCollectibleName.setText(collectibleName);

        Button buttonRarity = (Button) requireView().findViewById(R.id.buttonRarity);
        buttonRarity.setText(collectibleRarity);

        ImageButton modelRotateButton = (ImageButton) requireView().findViewById(R.id.buttonRotate);
        modelRotateButton.setOnClickListener((View v) -> {
            webView.evaluateJavascript("javascript:modelRotation();", null);
        });

        ImageButton buttonBack = (ImageButton) requireView().findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener((View v) -> {
            requireActivity().onBackPressed();
        });
    }

    private boolean isDarkModEnabled() {
        int nightModeFlags = requireContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}
