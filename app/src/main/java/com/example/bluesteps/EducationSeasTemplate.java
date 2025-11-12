package com.example.bluesteps;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EducationSeasTemplate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_education_seas_template);

        int seaId = getIntent().getIntExtra("seaId", -1);
        if (seaId != -1) {
            JSONObject sea = searchSeaById(seaId); // Daha önce yazdığın searchSeaById metodunu kullan
            if (sea != null) {
                TextView seaNameTextView = findViewById(R.id.sea_name);
                seaNameTextView.setText(sea.optString("sea_name", "Not Found"));

                // Burada diğer bilgileri de aynı şekilde çekebilirsin
            }
        } else {
            Toast.makeText(this, "Sea Not Found!", Toast.LENGTH_SHORT).show();
        }



        loadJSONToPage(seaId);

        JSONObject sea = searchSeaById(seaId);
        if (sea != null) {
            loadSeaImagesToViewPager(sea);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void loadSeaImagesToViewPager(JSONObject sea) {
        try {
            JSONArray imagesArray = sea.getJSONArray("image_paths");
            List<String> imageUrls = new ArrayList<>();

            for (int i = 0; i < imagesArray.length(); i++) {
                imageUrls.add(imagesArray.getString(i));
            }

            ViewPager2 viewPager = findViewById(R.id.viewPager);
            RemoteImageAdapter adapter = new RemoteImageAdapter(this, imageUrls);
            viewPager.setAdapter(adapter);

            DotsIndicator dotsIndicator = findViewById(R.id.dotsIndicator);
            dotsIndicator.attachTo(viewPager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject searchSeaById(int id) {
        try {
            JSONArray seas = seaJson.loadSeaJson(this);
            if (seas != null) {
                for (int i = 0; i < seas.length(); i++) {
                    JSONObject sea = seas.getJSONObject(i);
                    if (sea.has("id") && sea.getInt("id") == id) {
                        return sea;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadJSONToPage(int seaId) {
        JSONArray seaArray = seaJson.loadSeaJson(this);
        JSONObject sea = null;

        try {
            // ID’ye göre denizi bul
            for (int i = 0; i < seaArray.length(); i++) {
                JSONObject s = seaArray.getJSONObject(i);
                if (s.getInt("id") == seaId) {
                    sea = s;
                    break;
                }
            }

            if (sea == null) {
                Toast.makeText(this, "Sea not found", Toast.LENGTH_SHORT).show();
                return;
            }

            // TextView’leri bul
            TextView textSeaName = findViewById(R.id.sea_name);
            TextView textMaxDepth = findViewById(R.id.sea_max_depth);
            TextView textSalinity = findViewById(R.id.sea_salinity);
            TextView textTemperatureRange = findViewById(R.id.sea_tempature_range);
            TextView textArea = findViewById(R.id.sea_area);
            TextView textDescription = findViewById(R.id.sea_description);
            TextView textEcosystem = findViewById(R.id.sea_ecosystem);
            TextView textEconomicActivities = findViewById(R.id.sea_economic_activities);
            TextView textMainFishesDescription = findViewById(R.id.sea_main_fishes_description);

            // Verileri ayarla
            textSeaName.setText(sea.optString("sea_name", ""));
            textMaxDepth.setText(sea.optInt("max_depth", 0) + " m");
            textSalinity.setText(sea.optString("salinity", ""));
            textTemperatureRange.setText(sea.optString("temperature_range", ""));
            textArea.setText(sea.optString("area", ""));
            textDescription.setText(sea.optString("description", ""));
            textEcosystem.setText(sea.optString("ecosystem", ""));

            // Economic activities
            JSONArray activitiesArray = sea.optJSONArray("economic_activities");
            if (activitiesArray != null) {
                StringBuilder activitiesBuilder = new StringBuilder();
                for (int j = 0; j < activitiesArray.length(); j++) {
                    activitiesBuilder.append(activitiesArray.getString(j));
                    if (j < activitiesArray.length() - 1) activitiesBuilder.append(", ");
                }
                textEconomicActivities.setText(activitiesBuilder.toString());
            }

            // Main fishes
            JSONArray mainFishesArray = sea.optJSONArray("main_fishes");
            if (mainFishesArray != null) {
                LinearLayout mainFishesLayout = findViewById(R.id.main_fishes_layout);
                mainFishesLayout.removeAllViews(); // Önce eski verileri temizle

                for (int i = 0; i < mainFishesArray.length(); i++) {
                    String fishName = mainFishesArray.optString(i, "");

                    // Her balık için container oluştur
                    LinearLayout fishContainer = new LinearLayout(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, dpToPx.convertDpToPx(this,8), 0, dpToPx.convertDpToPx(this,8)); // Üst ve alt margin
                    fishContainer.setLayoutParams(params);

                    fishContainer.setOrientation(LinearLayout.VERTICAL);
                    fishContainer.setPadding(dpToPx.convertDpToPx(this,3), dpToPx.convertDpToPx(this,3), dpToPx.convertDpToPx(this,3), dpToPx.convertDpToPx(this,3));
                    fishContainer.setBackground(getDrawable(R.drawable.roundex_box_white));
                    // XML’deki arkaplan

                    // TextView oluştur
                    TextView fishTextView = new TextView(new ContextThemeWrapper(this, R.style.TextViewBody));
                    fishTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    fishTextView.setPadding(dpToPx.convertDpToPx(this,6), dpToPx.convertDpToPx(this,6), dpToPx.convertDpToPx(this,6), dpToPx.convertDpToPx(this,6));
                    fishTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    fishTextView.setText(fishName);

                    fishContainer.addView(fishTextView);
                    mainFishesLayout.addView(fishContainer);
                }

                textMainFishesDescription.setText("The main fishes of " + sea.optString("sea_name", ""));
            }

            // ViewPager2 için resimler
            ViewPager2 viewPager = findViewById(R.id.viewPager);
            JSONArray imagePaths = sea.optJSONArray("image_paths");
            if (imagePaths != null) {
                List<String> images = new ArrayList<>();
                for (int k = 0; k < imagePaths.length(); k++) {
                    images.add(imagePaths.getString(k));
                }
                //ImageAdapterSea adapter = new ImageAdapterSea(this, ImageAdapterSea);
                //viewPager.setAdapter(adapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "JSON parse error", Toast.LENGTH_SHORT).show();
        }
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
}