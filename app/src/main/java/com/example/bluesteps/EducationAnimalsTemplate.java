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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EducationAnimalsTemplate extends AppCompatActivity {

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

    public JSONArray loadFishJson() {
        try {
            InputStream is = getAssets().open("fish.json");
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

    public JSONObject searchFishById(int id) {
        try {
            JSONArray countriesArray = loadFishJson();
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
        TextView textViewFishColors = findViewById(R.id.fish_colors);
        TextView textViewFishDanger = findViewById(R.id.fish_danger);
        TextView textViewFishEdible = findViewById(R.id.fish_edible);
        TextView textViewFishDescription = findViewById(R.id.fish_description);
        TextView textViewFishFamily = findViewById(R.id.fish_family);
        TextView textViewFishDistributionHabitat = findViewById(R.id.fish_distribution_habitat);
        TextView textViewFishHabitat = findViewById(R.id.fish_habitat);
        TextView textViewFishLocationDescription = findViewById(R.id.fish_location_description);
        TextView textViewFishPoisonous = findViewById(R.id.fish_poisonous);
        TextView textViewFishSocialBehaviour = findViewById(R.id.fish_social_behaviour);

        int intentFishData = getIntent().getIntExtra("fish_id", 0);
        JSONObject fish = searchFishById(intentFishData);
        if (fish != null) {
            try {
                textViewFishName.setText(fish.getString("animal_name"));
                textViewFishMaxWeight.setText(String.valueOf(fish.getDouble("max_weight")));
                textViewFishMaxSize.setText(String.valueOf(fish.getInt("max_size")));
                textViewFishMaxDepth.setText(String.valueOf(fish.getInt("max_depth")));
                textViewFishFamily.setText(fish.getString("family"));
                textViewFishHabitat.setText(fish.getString("habitat"));
                textViewFishEdible.setText(fish.getString("edible"));
                textViewFishDescription.setText(fish.getString("description"));
                textViewFishDistributionHabitat.setText(fish.getString("distribution_habitat"));
                textViewFishLocationDescription.setText(fish.getString("locations_description"));
                textViewFishSocialBehaviour.setText(fish.getString("social_behaviour"));
                textViewFishPoisonous.setText(fish.getString("poisonous"));
                textViewFishDanger.setText(fish.getBoolean("danger_to_human") ? "Tehlikeli" : "Tehlikesiz");

                JSONArray colorsArray = fish.getJSONArray("colors");
                StringBuilder colors = new StringBuilder();
                for (int i = 0; i < colorsArray.length(); i++) {
                    colors.append(colorsArray.getString(i));
                    if (i != colorsArray.length() - 1) colors.append(", ");
                }
                textViewFishColors.setText(colors.toString());


                JSONArray locationsArray = fish.optJSONArray("locations");
                if (locationsArray != null) {
                    LinearLayout locationsLayout = findViewById(R.id.fish_locations); // veya uygun container id
                    locationsLayout.removeAllViews(); // önceki içerikleri temizle

                    for (int i = 0; i < locationsArray.length(); i++) {
                        String locationName = locationsArray.optString(i, "");

                        // Ana siyah LinearLayout
                        LinearLayout locationLayout = new LinearLayout(this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutParams.setMargins(0, 5, 0, 5);
                        locationLayout.setLayoutParams(layoutParams);
                        locationLayout.setOrientation(LinearLayout.VERTICAL);
                        locationLayout.setPadding(10, 10, 10, 10);
                        locationLayout.setBackgroundResource(R.drawable.rounded_box);

                        // İç beyaz container
                        LinearLayout textContainer = new LinearLayout(this);
                        textContainer.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1.0f
                        ));
                        textContainer.setOrientation(LinearLayout.VERTICAL);
                        textContainer.setPadding(7, 7, 7, 7);
                        textContainer.setBackgroundResource(R.drawable.roundex_box_white);

                        TextView locationTextView = new TextView(new ContextThemeWrapper(this, R.style.TextViewBody));
                        locationTextView.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        locationTextView.setPadding(6, 6, 6, 6);
                        locationTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        locationTextView.setText(locationName);

                        textContainer.addView(locationTextView);
                        locationLayout.addView(textContainer);
                        locationsLayout.addView(locationLayout);
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void btnEducation(View view){
        Intent intent = new Intent(this, EducationMainPage.class);
        startActivity(intent);
    }
    public void btnAboutUs(View view){
        Nav.goToAboutUs(view);
    }
    public void navHomePage(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}