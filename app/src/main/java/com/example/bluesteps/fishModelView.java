package com.example.bluesteps;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class fishModelView extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fish_model_view);

        int intentFishData = getIntent().getIntExtra("fish_id_3d", 0);
        JSONObject fish = searchFishById(intentFishData);
        if(checkInternetConnection.isInternetAvailable(this)){
            if (fish != null) {
                try {
                    String modelUrl = fish.optString("3d_model", "");

                    if (!modelUrl.isEmpty()) {
                        //webView.loadUrl(modelUrl);
                        get3dModelPage(modelUrl);
                    } else {
                        modelLoadError("No 3D Model found");                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                modelLoadError("No fish found");
            }
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
        else{
            modelLoadError("You need internet connection for see the 3D model");
        }

    }

    public void modelLoadError(String errorDetail){
        LinearLayout parent = findViewById(R.id.parentLayout);
        parent.removeAllViews();
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setGravity(Gravity.CENTER);
        TextView textviewWarning = new TextView(new ContextThemeWrapper(this, R.style.TextViewTitle));
        textviewWarning.setText(errorDetail);
        textviewWarning.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textviewWarning.setGravity(Gravity.CENTER);
        textviewWarning.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        parent.addView(textviewWarning);
    }

    public JSONObject searchFishById(int id) {
        try {
            JSONArray countriesArray = fishJson.loadFishJson(this);
            if (countriesArray != null) {
                for (int i = 0; i < countriesArray.length(); i++) {
                    JSONObject countryObj = countriesArray.getJSONObject(i);
                    JSONArray fishArray = countryObj.getJSONArray("animals");

                    for (int j = 0; j < fishArray.length(); j++) {
                        JSONObject fish = fishArray.getJSONObject(j);
                        if (fish.has("id") && fish.getInt("id") == id) {
                            return fish;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void get3dModelPage(String url){
        String baseUrl = url;


        webView = findViewById(R.id.sketchfab_webview);

        // 1. WebView ayarlarını yapma (JavaScript, 3D içeriği için zorunludur)
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        // Bu, WebView içindeki linklerin telefonun harici tarayıcısında açılmasını engeller.
        webView.setWebViewClient(new WebViewClient());

        // 2. Gizleme parametreleri eklenmiş URL'yi yükleme
        webView.loadUrl(baseUrl);

        TextView textviewFishName = findViewById(R.id.textviewFishName);
        String fishName = getIntent().getStringExtra("fish_name_3d");
        if (fishName == null) {
            fishName = "Fish"; // varsayılan değer
        }

        if(checkInternetConnection.isInternetAvailable(this)){
            textviewFishName.setText(fishName);
        }
        else{
            textviewFishName.setText("Connection Error");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
            webView.pauseTimers();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
            webView.resumeTimers();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadUrl("about:blank");
            webView.clearHistory();
            webView.clearCache(true);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    public void navQuiz(View view){
        Nav.goToQuizPage(view);
    }
    public void navHomePage(View view){
        Nav.goToHomePage(view);
    }
    public void goToEducation(View view){
        Nav.goToEducation(view);
    }
    public void btnAboutUs(View view){
        Nav.goToAboutUs(view);
    }
    public void btnReturn(View view){
        finish();
    }
}