package com.example.bluesteps;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            loadJSONToPage(seaId);
            JSONObject sea = searchSeaById(seaId);
            if (sea != null) {
                loadSeaImagesToViewPager(sea);
            }
        } else {
            Toast.makeText(this, "Sea Not Found!", Toast.LENGTH_SHORT).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Bottom bar için padding ayarı
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
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
            for (int i = 0; i < seaArray.length(); i++) {
                JSONObject s = seaArray.getJSONObject(i);
                if (s.getInt("id") == seaId) {
                    sea = s;
                    break;
                }
            }

            if (sea == null) return;

            // UI Elementlerini Bağla
            TextView textSeaName = findViewById(R.id.sea_name);
            TextView textMaxDepth = findViewById(R.id.sea_max_depth);
            TextView textSalinity = findViewById(R.id.sea_salinity);
            TextView textTemperatureRange = findViewById(R.id.sea_tempature_range);
            TextView textArea = findViewById(R.id.sea_area);
            TextView textDescription = findViewById(R.id.sea_description);
            TextView textEcosystem = findViewById(R.id.sea_ecosystem);
            TextView textEconomicActivities = findViewById(R.id.sea_economic_activities);
            TextView textMainFishesDescription = findViewById(R.id.sea_main_fishes_description);

            // Verileri Ata
            textSeaName.setText(sea.optString("sea_name", "Unknown"));
            textMaxDepth.setText(sea.optString("max_depth", "0") + "m");
            textSalinity.setText(sea.optString("salinity", "N/A"));
            textTemperatureRange.setText(sea.optString("temperature_range", "N/A"));
            textArea.setText(sea.optString("area", "N/A"));
            textDescription.setText(sea.optString("description", ""));
            textEcosystem.setText(sea.optString("ecosystem", ""));
            textMainFishesDescription.setText("Typical marine life found in the " + sea.optString("sea_name", "sea") + ":");

            // Economy Kısmı
            JSONArray activitiesArray = sea.optJSONArray("economic_activities");
            if (activitiesArray != null) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < activitiesArray.length(); j++) {
                    sb.append("• ").append(activitiesArray.getString(j)).append("\n");
                }
                textEconomicActivities.setText(sb.toString().trim());
            }

            // Balık Listesini Modern Kart Tasarımıyla Oluştur
            JSONArray mainFishesArray = sea.optJSONArray("main_fishes");
            if (mainFishesArray != null) {
                LinearLayout mainFishesLayout = findViewById(R.id.main_fishes_layout);
                mainFishesLayout.removeAllViews();

                for (int i = 0; i < mainFishesArray.length(); i++) {
                    String fishName = mainFishesArray.optString(i, "");

                    // Yatay Kart (Row)
                    LinearLayout row = new LinearLayout(this);
                    LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    rowParams.setMargins(0, 0, 0, dpToPx.convertDpToPx(this, 8));
                    row.setLayoutParams(rowParams);
                    row.setOrientation(LinearLayout.HORIZONTAL);
                    row.setGravity(Gravity.CENTER_VERTICAL);
                    row.setPadding(dpToPx.convertDpToPx(this, 12), dpToPx.convertDpToPx(this, 12),
                            dpToPx.convertDpToPx(this, 12), dpToPx.convertDpToPx(this, 12));
                    row.setBackground(ContextCompat.getDrawable(this, R.drawable.category_card_bg));

                    // Balık İkonu
                    ImageView icon = new ImageView(this);
                    icon.setLayoutParams(new LinearLayout.LayoutParams(
                            dpToPx.convertDpToPx(this, 20),
                            dpToPx.convertDpToPx(this, 20)
                    ));
                    icon.setImageResource(R.drawable.fish);
                    icon.setColorFilter(Color.parseColor("#3B82F6")); // Tasarımdaki mavi

                    // Balık İsmi
                    TextView fishText = new TextView(this);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    textParams.setMarginStart(dpToPx.convertDpToPx(this, 12));
                    fishText.setLayoutParams(textParams);
                    fishText.setText(fishName);
                    fishText.setTextColor(Color.parseColor("#1F2937"));
                    fishText.setTextSize(14);
                    fishText.setTypeface(null, android.graphics.Typeface.BOLD);

                    row.addView(icon);
                    row.addView(fishText);
                    mainFishesLayout.addView(row);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Navigasyon metodları aynı kalabilir...
    public void navQuiz(View view){ Nav.goToQuizPage(view); }
    public void navHomePage(View view){ Nav.goToHomePage(view); }
    public void goToEducation(View view){ Nav.goToEducation(view); }
    public void btnAboutUs(View view){ Nav.goToAboutUs(view); }
}