package com.example.bluesteps;

import android.os.Bundle;
import android.view.View;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

public class EducationSeaAnimals extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_education_sea_animals);
        loadJSONDataToPage();
        int[] images = {
                R.drawable.badges,
                R.drawable.profile,
                R.drawable.aboutus
        };

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        ImageAdapter adapter = new ImageAdapter(this, images);
        viewPager.setAdapter(adapter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void navHomePage(View view){
        Nav.goToHomePage(view);
    }
    public void goToEducation(View view){
        Nav.goToEducation(view);
    }
    public void btnAnimalTemplate(View view){
        Nav.goToAnimalsTemplate(view);
    }

    public JSONArray loadFishJson() {
        try {
            InputStream is = getAssets().open("fish.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            return new JSONArray(json);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject searchFishByName(String name) {
        try {
            JSONArray countriesArray = loadFishJson();
            if (countriesArray != null) {
                for (int i = 0; i < countriesArray.length(); i++) {
                    JSONObject countryObj = countriesArray.getJSONObject(i);
                    JSONArray fishArray = countryObj.getJSONArray("fish");
                    for (int j = 0; j < fishArray.length(); j++) {
                        JSONObject fish = fishArray.getJSONObject(j);
                        if (fish.getString("fish_name").equalsIgnoreCase(name)) {
                            return fish; // eşleşen balık bulundu
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // eşleşme yoksa
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

        JSONObject fish = searchFishByName("Hamsi (Anchovy)");
        if (fish != null) {
            try {
                // Data
                String fishName = fish.getString("fish_name");
                double maxWeight = fish.getDouble("max_weight"); // artık double
                int maxSize = fish.getInt("max_size");
                int maxDepth = fish.getInt("max_depth");
                String family = fish.getString("family");
                String habitat = fish.getString("habitat");
                String edible = fish.getString("edible");
                String description = fish.getString("description");
                String distributionHabitat = fish.getString("distribution_habitat");
                String locationDescription = fish.getString("locations_description");
                String socialBehaviour = fish.getString("social_behaviour");
                String poisonous = fish.getString("poisonous");
                boolean dangerToHuman = fish.getBoolean("danger_to_human");

                // Data Arrays
                JSONArray colorsArray = fish.getJSONArray("colors");
                StringBuilder colors = new StringBuilder();
                for (int i = 0; i < colorsArray.length(); i++) {
                    colors.append(colorsArray.getString(i));
                    if (i != colorsArray.length() - 1) colors.append(", ");
                }

                // TextViews
                textViewFishName.setText(fishName);
                textViewFishEdible.setText(edible);
                textViewFishMaxWeight.setText(String.valueOf(maxWeight));
                textViewFishMaxSize.setText(String.valueOf(maxSize));
                textViewFishMaxDepth.setText(String.valueOf(maxDepth));
                textViewFishColors.setText(colors.toString());
                textViewFishDanger.setText(dangerToHuman ? "Tehlikeli" : "Tehlikesiz");
                textViewFishDescription.setText(description);
                textViewFishFamily.setText(family);
                textViewFishDistributionHabitat.setText(distributionHabitat);
                textViewFishHabitat.setText(habitat);
                textViewFishLocationDescription.setText(locationDescription);
                textViewFishPoisonous.setText(poisonous);
                textViewFishSocialBehaviour.setText(socialBehaviour);

                return true;

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }





}