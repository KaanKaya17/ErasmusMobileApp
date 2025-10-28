package com.example.bluesteps;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class LocationsCountryAnimals extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_locations_country_animals);

        JSONArray fishArray = loadFishJson();
        loadCountriesFromJson(this,fishArray);

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
            int read = is.read(buffer);
            is.close();

            if (read <= 0) return null;

            String json = new String(buffer, StandardCharsets.UTF_8);
            return new JSONArray(json);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }
    private void loadCountriesFromJson(Context context, JSONArray countriesArray) {
        LinearLayout parentLayout = findViewById(R.id.root_layout);

        for (int i = 0; i < countriesArray.length(); i++) {
            try {
                JSONObject countryObj = countriesArray.getJSONObject(i);
                String countryName = countryObj.getString("country");

                int fishCount = 0;
                if (countryObj.has("animals") && !countryObj.isNull("animals")) {
                    JSONArray animalsArray = countryObj.getJSONArray("animals");
                    fishCount = animalsArray.length();
                }

                // Ana root layout (vertical)
                LinearLayout rootLayout = new LinearLayout(context);
                rootLayout.setOrientation(LinearLayout.VERTICAL);
                rootLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                // countryCard layout (arka plan + padding)
                LinearLayout countryCard = new LinearLayout(context);
                countryCard.setId(i);
                countryCard.setTag("sea_" + i);
                countryCard.setOrientation(LinearLayout.HORIZONTAL);
                countryCard.setBackgroundResource(R.drawable.roundex_box_white);
                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                cardParams.setMargins(dpToPx(0), dpToPx(10), dpToPx(0), dpToPx(10));
                countryCard.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
                countryCard.setLayoutParams(cardParams);

                // İçteki clickable horizontal layout
                LinearLayout innerLayout = new LinearLayout(context);
                innerLayout.setOrientation(LinearLayout.HORIZONTAL);
                innerLayout.setClickable(true);
                innerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                // Ülke adı TextView
                TextView countryText = new TextView(context);
                countryText.setText(countryName);
                countryText.setTextSize(16);
                countryText.setTextColor(Color.BLACK);
                countryText.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams countryTextParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                countryTextParams.setMargins(0, 0, 15, 0);
                countryText.setLayoutParams(countryTextParams);

                // Balık sayısı TextView
                TextView fishCountText = new TextView(context);
                fishCountText.setText(String.valueOf(fishCount));
                fishCountText.setTextSize(16);
                fishCountText.setTextColor(Color.BLACK);

                // Tıklama olayı
                countryCard.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), EducationSeas.class);
                    intent.putExtra("countryName", countryName);
                    startActivity(intent);
                });

                // Alt çizgi
                View divider = new View(context);
                LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2
                );
                //dividerParams.setMargins(dpToPx(0),dpToPx(7),dpToPx(0),dpToPx(0));
                divider.setLayoutParams(dividerParams);
                divider.setBackgroundColor(Color.BLACK);

                // Hiyerarşi:
                // rootLayout
                // ├── countryCard
                // │   └── innerLayout
                // │       ├── countryText
                // │       └── fishCountText
                // └── divider

                innerLayout.addView(countryText);
                innerLayout.addView(fishCountText);
                countryCard.addView(innerLayout);
                rootLayout.addView(countryCard);
                rootLayout.addView(divider);

                // Parent’e ekle
                parentLayout.addView(rootLayout);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}