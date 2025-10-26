package com.example.bluesteps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

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

public class EducationCountrySeas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_education_country_seas);

        JSONArray jsonArray = loadFishJson();
        if (jsonArray != null) {
            loadCountriesFromJson(this, jsonArray);
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

    private void loadCountriesFromJson(Context context, JSONArray countriesArray) {
        LinearLayout parentLayout = findViewById(R.id.parentLayout);

        for (int i = 0; i < countriesArray.length(); i++) {
            try {
                JSONObject countryObj = countriesArray.getJSONObject(i);
                String countryName = countryObj.getString("country");

                // "fish" dizisi varsa uzunluğunu al, yoksa 0 ver
                int fishCount = 0;
                if (countryObj.has("fish") && !countryObj.isNull("fish")) {
                    JSONArray fishArray = countryObj.getJSONArray("fish");
                    fishCount = fishArray.length();
                }

                // Ana satır layout
                LinearLayout countryRow = new LinearLayout(context);
                countryRow.setOrientation(LinearLayout.HORIZONTAL);
                countryRow.setId(i);
                countryRow.setTag("sea_" + i);
                countryRow.setOnClickListener(v -> {
                    int viewId = v.getId();
                    String tag = (String) v.getTag();
                    /*
                    Toast.makeText(getApplicationContext(),
                            "ID: " + viewId + " | Tag: " + tag + " tıklandı!",
                            Toast.LENGTH_SHORT).show();

                     */
                    Intent intent = new Intent(getApplicationContext(), EducationSeas.class);
                    intent.putExtra("countryName",countryName);
                    startActivity(intent);
                });
                countryRow.setPadding(0, 20, 0, 20);

                // Ülke resmi
                ImageView flagImage = new ImageView(context);
                LinearLayout.LayoutParams flagParams = new LinearLayout.LayoutParams(200, 120);
                flagParams.setMargins(0, 0, 20, 0);
                flagImage.setLayoutParams(flagParams);
                flagImage.setImageResource(R.drawable.badges);

                // Bilgi kısmı
                LinearLayout infoLayout = new LinearLayout(context);
                infoLayout.setOrientation(LinearLayout.VERTICAL);
                infoLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                // Ülke adı
                TextView countryText = new TextView(context);
                countryText.setText(countryName);
                countryText.setTextSize(18);
                countryText.setTextColor(0xFF000000);
                countryText.setTypeface(null, android.graphics.Typeface.BOLD);

                // Balık sayısı
                TextView fishText = new TextView(context);
                fishText.setText(fishCount + " Fishes");
                fishText.setTextColor(0xFF000000);

                // Canlı sayısı
                TextView creatureText = new TextView(context);
                creatureText.setText("0 Creatures");
                creatureText.setTextColor(0xFF000000);

                // Ayırıcı çizgi
                View divider = new View(context);
                LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 2);
                dividerParams.setMargins(0, 10, 0, 0);
                divider.setLayoutParams(dividerParams);
                divider.setBackgroundColor(0xFF000000);

                // Bilgileri ekle
                infoLayout.addView(countryText);
                infoLayout.addView(fishText);
                infoLayout.addView(creatureText);
                infoLayout.addView(divider);

                // Ana satıra ekle
                countryRow.addView(flagImage);
                countryRow.addView(infoLayout);

                // Parent'e ekle
                parentLayout.addView(countryRow);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnQuiz(View view){
        Nav.goToQuiz(view);
    }

    public void goToEducation(View view){
        Nav.goToEducation(view);
    }
}
