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

        int seaId = getIntent().getIntExtra("id", 2);

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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray loadSeaJson() {
        try {
            InputStream is = getAssets().open("sea.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            return new JSONArray(json);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public JSONObject searchSeaById(int id) {
        try {
            JSONArray seas = loadSeaJson();
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
        JSONArray seaArray = loadSeaJson();
        JSONObject sea = null;

        try {
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

            TextView textSeaName = findViewById(R.id.sea_name);
            TextView textMaxDepth = findViewById(R.id.sea_max_depth);
            TextView textSalinity = findViewById(R.id.sea_salinity);
            TextView textTemperatureRange = findViewById(R.id.sea_tempature_range);
            TextView textArea = findViewById(R.id.sea_area);
            TextView textDescription = findViewById(R.id.sea_description);
            TextView textEcosystem = findViewById(R.id.sea_ecosystem);
            TextView textEconomicActivities = findViewById(R.id.sea_economic_activities);

            textSeaName.setText(sea.optString("sea_name", ""));
            textMaxDepth.setText(sea.optInt("max_depth", 0) + " m");
            textSalinity.setText(sea.optString("salinity", ""));
            textTemperatureRange.setText(sea.optString("temperature_range", ""));
            textArea.setText(sea.optString("area", ""));
            textDescription.setText(sea.optString("description", ""));
            textEcosystem.setText(sea.optString("ecosystem", ""));

            JSONArray activitiesArray = sea.optJSONArray("economic_activities");
            if (activitiesArray != null) {
                StringBuilder activitiesBuilder = new StringBuilder();
                for (int j = 0; j < activitiesArray.length(); j++) {
                    activitiesBuilder.append(activitiesArray.getString(j));
                    if (j < activitiesArray.length() - 1) activitiesBuilder.append(", ");
                }
                textEconomicActivities.setText(activitiesBuilder.toString());
            }

            JSONArray mainFishesArray = sea.optJSONArray("main_fishes");
            if (mainFishesArray != null) {
                LinearLayout mainFishesLayout = findViewById(R.id.main_fishes_layout);
                mainFishesLayout.removeAllViews(); // Önce eski verileri temizle

                for (int i = 0; i < mainFishesArray.length(); i++) {
                    String fishName = mainFishesArray.optString(i, "");

                    // Her balık için LinearLayout oluştur
                    LinearLayout fishLayout = new LinearLayout(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMargins(0, 5, 0, 5); // Margin top/bottom
                    fishLayout.setLayoutParams(layoutParams);
                    fishLayout.setOrientation(LinearLayout.VERTICAL);
                    fishLayout.setPadding(10, 10, 10, 10);
                    fishLayout.setBackgroundColor(0xFF000000); // #000000

                    // İçine TextView ekle
                    LinearLayout textContainer = new LinearLayout(this);
                    textContainer.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1.0f
                    ));
                    textContainer.setOrientation(LinearLayout.VERTICAL);
                    textContainer.setPadding(3, 3, 3, 3);
                    textContainer.setBackgroundColor(0xFFFFFFFF); // #ffffff

                    TextView fishTextView = new TextView(new ContextThemeWrapper(this, R.style.TextViewBody));
                    fishTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    fishTextView.setPadding(6, 6, 6, 6);
                    fishTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    fishTextView.setText(fishName);

                    textContainer.addView(fishTextView);
                    fishLayout.addView(textContainer);
                    mainFishesLayout.addView(fishLayout);
                }
            }

            /*
            ViewPager2 viewPager = findViewById(R.id.viewPager);
            JSONArray imagePaths = sea.optJSONArray("image_paths");
            if (imagePaths != null) {
                List<String> images = new ArrayList<>();
                for (int k = 0; k < imagePaths.length(); k++) {
                    images.add(imagePaths.getString(k));
                }
                ImageAdapter adapter = new ImageAdapter(this, images);
                viewPager.setAdapter(adapter);
            }
            */

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "JSON parse error", Toast.LENGTH_SHORT).show();
        }
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