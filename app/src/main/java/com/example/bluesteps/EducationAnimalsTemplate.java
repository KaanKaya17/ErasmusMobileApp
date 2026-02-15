package com.example.bluesteps;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class EducationAnimalsTemplate extends AppCompatActivity {

    public String fishName = "Fish";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_education_animals_template);

        int intentFishData = getIntent().getIntExtra("fish_id", 0);

        if (loadJSONDataToPage()) {
            JSONObject fish = searchFishById(intentFishData);
            if (fish != null) {
                loadFishImagesToViewPager(fish);
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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

    public boolean loadJSONDataToPage() {
        TextView textViewFishName = findViewById(R.id.fish_name);
        TextView textViewFishMaxWeight = findViewById(R.id.max_weight);
        TextView textViewFishMaxDepth = findViewById(R.id.max_depth);
        TextView textViewFishMaxSize = findViewById(R.id.max_size);
        TextView textViewFishDanger = findViewById(R.id.fish_danger);
        TextView textViewFishEdible = findViewById(R.id.fish_edible);
        TextView textViewFishDescription = findViewById(R.id.fish_description);
        TextView textViewFishFamily = findViewById(R.id.fish_family);
        TextView textViewFishDistributionHabitat = findViewById(R.id.fish_distribution_habitat);
        TextView textViewFishHabitat = findViewById(R.id.fish_habitat);
        TextView textViewFishLocationDescription = findViewById(R.id.fish_location_description);
        TextView textViewFishPoisonous = findViewById(R.id.fish_poisonous);
        TextView textViewFishSocialBehaviour = findViewById(R.id.fish_social_behaviour);
        TextView textviewOtherNames = findViewById(R.id.other_names);

        int intentFishData = getIntent().getIntExtra("fish_id", 0);
        JSONObject fish = searchFishById(intentFishData);
        if (fish != null) {
            try {
                textViewFishName.setText(fish.getString("animal_name"));
                fishName = fish.getString("animal_name");
                textViewFishMaxWeight.setText(String.valueOf(fish.getDouble("max_weight")) + " kg");
                textViewFishMaxSize.setText(String.valueOf(fish.getInt("max_size")) + " cm");
                textViewFishMaxDepth.setText(String.valueOf(fish.getInt("max_depth")) + " m");
                textViewFishFamily.setText(fish.getString("family"));
                textViewFishHabitat.setText(fish.getString("habitat"));
                //textViewFishEdible.setText(fish.getBoolean("edible") ? "Yes" : "No");
                boolean isEdible = fish.getBoolean("edible");
                textViewFishEdible.setText(isEdible ? getString(R.string.animal_yes) : getString(R.string.animal_no));
                textViewFishDescription.setText(fish.getString("description"));
                textViewFishDistributionHabitat.setText(fish.getString("distribution_habitat"));
                textViewFishLocationDescription.setText(fish.getString("locations_description"));
                textViewFishSocialBehaviour.setText(fish.getString("social_behaviour"));
                //textViewFishPoisonous.setText(fish.getBoolean("poisonous") ? "Yes" : "No");
                boolean isPoisonous = fish.getBoolean("edible");
                textViewFishPoisonous.setText(isPoisonous ? getString(R.string.animal_poisonous_yes) : getString(R.string.animal_not_poisonous));
                //textViewFishDanger.setText(fish.getBoolean("danger_to_human") ? "Dangerous" : "Not dangerous");
                boolean isDangerous = fish.getBoolean("danger_to_human");
                textViewFishDanger.setText(isDangerous ? getString(R.string.animal_dangerous) : getString(R.string.animal_not_dangerous));

                JSONArray colorsArray = fish.getJSONArray("colors");

                JSONArray otherNamesArray = fish.optJSONArray("other_names");
                if (otherNamesArray != null) {
                    StringBuilder namesBuilder = new StringBuilder();
                    for (int i = 0; i < otherNamesArray.length(); i++) {
                        namesBuilder.append(otherNamesArray.optString(i));
                        if (i < otherNamesArray.length() - 1) {
                            namesBuilder.append(", ");
                        }
                    }
                    textviewOtherNames.setText(namesBuilder.toString());
                }

                //Renk toplarını göstereceğimiz LinearLayout
                LinearLayout colorsLayout = findViewById(R.id.fish_colors_container);
                colorsLayout.removeAllViews(); // Önceki topları temizle

                //JSON'daki her renk için döngü
                for (int i = 0; i < colorsArray.length(); i++) {
                    String colorValue = colorsArray.getString(i).trim();
                    int circleColor;

                    // Eğer renk hex formatında (örneğin "#FF0000") değilse hata olmasın diye kontrol et
                    try {
                        circleColor = android.graphics.Color.parseColor(colorValue);
                    } catch (IllegalArgumentException e) {
                        // Renk ismini çözemezse, gri tonuna çevir
                        circleColor = android.graphics.Color.GRAY;
                    }

                    //Yeni bir View oluştur (yuvarlak top)
                    View colorCircle = new View(this);

                    // Boyut ve margin ayarları
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx.convertDpToPx(this,28), dpToPx.convertDpToPx(this,28));
                    params.setMargins(dpToPx.convertDpToPx(this,4), dpToPx.convertDpToPx(this,4), dpToPx.convertDpToPx(this,4), dpToPx.convertDpToPx(this,4));
                    colorCircle.setLayoutParams(params);

                    //Yuvarlak görünüm için arka plan tanımla
                    colorCircle.setBackgroundResource(R.drawable.color_circle_shape);

                    // Arka planın rengini JSON’daki renkle değiştir
                    colorCircle.getBackground().setTint(circleColor);

                    // (Opsiyonel) Her topa tıklanınca Toast ile renk kodunu göster
                    colorCircle.setOnClickListener(v -> {
                        android.widget.Toast.makeText(this, "Color: " + colorValue, android.widget.Toast.LENGTH_SHORT).show();
                    });

                    //Oluşturulan topu layout’a ekle
                    colorsLayout.addView(colorCircle);
                }



                JSONArray locationsArray = fish.optJSONArray("locations");
                if (locationsArray != null) {
                    LinearLayout locationsLayout = findViewById(R.id.fish_locations);
                    locationsLayout.removeAllViews(); // Önceki içerikleri temizle

                    for (int i = 0; i < locationsArray.length(); i++) {
                        String locationName = locationsArray.optString(i, "");

                        // Yatay Kart Satırı (Row)
                        LinearLayout row = new LinearLayout(this);
                        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        // Kartlar arası boşluk (8dp)
                        rowParams.setMargins(0, 0, 0, dpToPx.convertDpToPx(this, 8));
                        row.setLayoutParams(rowParams);
                        row.setOrientation(LinearLayout.HORIZONTAL);
                        row.setGravity(android.view.Gravity.CENTER_VERTICAL);
                        row.setPadding(dpToPx.convertDpToPx(this, 12), dpToPx.convertDpToPx(this, 12),
                                dpToPx.convertDpToPx(this, 12), dpToPx.convertDpToPx(this, 12));

                        // Sizin belirttiğiniz kategori arka planı
                        row.setBackground(androidx.core.content.ContextCompat.getDrawable(this, R.drawable.category_card_bg));

                        // Lokasyon/Pin İkonu
                        android.widget.ImageView icon = new android.widget.ImageView(this);
                        icon.setLayoutParams(new LinearLayout.LayoutParams(
                                dpToPx.convertDpToPx(this, 20),
                                dpToPx.convertDpToPx(this, 20)
                        ));
                        // Eğer elinizde lokasyon ikonu varsa onu koyabilirsiniz, yoksa fish ikonuyla devam:
                        icon.setImageResource(R.drawable.fish);
                        icon.setColorFilter(android.graphics.Color.parseColor("#3B82F6")); // Uygulama ana mavisi

                        // Lokasyon İsmi
                        TextView locationText = new TextView(this);
                        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        textParams.setMarginStart(dpToPx.convertDpToPx(this, 12));
                        locationText.setLayoutParams(textParams);
                        locationText.setText(locationName);
                        locationText.setTextColor(android.graphics.Color.parseColor("#1F2937")); // Koyu gri/siyah
                        locationText.setTextSize(14);
                        locationText.setTypeface(null, android.graphics.Typeface.BOLD);

                        // Hiyerarşiyi oluştur
                        row.addView(icon);
                        row.addView(locationText);
                        locationsLayout.addView(row);
                    }
                }


                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void loadFishImagesToViewPager(JSONObject fish) {
        try {
            JSONArray imagesArray = fish.getJSONArray("image_paths");
            List<String> imageUrls = new ArrayList<>();

            for (int i = 0; i < imagesArray.length(); i++) {
                imageUrls.add(imagesArray.getString(i));
            }

            ViewPager2 viewPager = findViewById(R.id.viewPager);
            RemoteImageAdapter adapter = new RemoteImageAdapter(this, imageUrls);
            viewPager.setAdapter(adapter);

            // dotsIndicator bağlantısı
            DotsIndicator dotsIndicator = findViewById(R.id.dotsIndicator);
            dotsIndicator.attachTo(viewPager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void btnEducation(View view){
        Nav.goToEducation(view);
    }
    public void navQuiz(View view){
        Nav.goToQuizPage(view);
    }
    public void btnAboutUs(View view){
        Nav.goToAboutUs(view);
    }
    public void navHomePage(View view){
        Nav.goToHomePage(view);
    }
    public void btnView3d(View view){
        int intentFishData = getIntent().getIntExtra("fish_id", 0);
        Intent intent = new Intent(this, fishModelView.class);
        intent.putExtra("fish_id_3d",intentFishData);
        intent.putExtra("fish_name_3d",fishName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}