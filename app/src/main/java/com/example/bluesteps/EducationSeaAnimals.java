package com.example.bluesteps;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EducationSeaAnimals extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_education_sea_animals);

        // JSON verisini yükle
        if (loadJSONDataToPage()) {
            JSONObject fish = searchFishByName("Hamsi (Anchovy)");
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

    // 🔹 JSON'u oku
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

    // 🔹 Belirli balığı isme göre bul
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

    // 🔹 Sayfadaki bilgileri doldur
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
                textViewFishName.setText(fish.getString("fish_name"));
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
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // 🔹 Drive linklerini ViewPager2’ye yükle
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
}
