package com.example.bluesteps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Intent intent = new Intent(this, GameMatching.class);
        //startActivity(intent);

        JSONArray jsonArray = fishJson.loadFishJson(this);
        JSONArray jsonSeaArray = loadSeaJson();
        if(jsonSeaArray != null){
            loadAllSeasToPage();
        }
        if (jsonArray != null) {
            loadCountriesFromJson(this, jsonArray);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadCountriesFromJson(Context context, JSONArray countriesArray) {
        LinearLayout parentLayout = findViewById(R.id.parentLayout);
        //parentLayout.removeAllViews();

        for (int i = 0; i < countriesArray.length(); i++) {
            try {
                JSONObject countryObj = countriesArray.getJSONObject(i);
                String countryName = countryObj.getString("country");

                // "fish" dizisi varsa uzunluğunu al, yoksa 0 ver
                int fishCount = 0;
                int creatureCount = 0;
                int otherAnimalsCount = 0;

                if (countryObj.has("animals") && !countryObj.isNull("animals")) {
                    JSONArray animalsArray = countryObj.getJSONArray("animals");
                    for (int j = 0; j < animalsArray.length(); j++) {
                        JSONObject animal = animalsArray.getJSONObject(j);
                        String type = animal.optString("type", "");

                        if (type.equalsIgnoreCase("fish")) {
                            fishCount++;
                        } else if (type.equalsIgnoreCase("creature")) {
                            creatureCount++;
                        } else {
                            otherAnimalsCount++; // fish ve creature değilse diğerler
                        }
                    }
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
                LinearLayout.LayoutParams flagParams = new LinearLayout.LayoutParams(200, 200);
                flagParams.setMargins(0, 0, 20, 0);
                flagImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                flagImage.setLayoutParams(flagParams);
                flagImage.setImageResource(R.drawable.country);

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
                creatureText.setText(creatureCount + " Creatures");
                creatureText.setTextColor(0xFF000000);

                // Diğer Canlı sayısı
                TextView othersText = new TextView(context);
                othersText.setText(otherAnimalsCount + " Sponges, Plants, Corals");
                othersText.setTextColor(0xFF000000);

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
                infoLayout.addView(othersText);
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

    public void btnEducation(View view){
        Nav.goToEducation(view);
    }
    public void btnQuiz(View view){
        Nav.goToQuizPage(view);
    }
    public void btnAboutUs(View view){
        Nav.goToAboutUs(view);
    }

    public void navQuiz(View view){
        Nav.goToQuizPage(view);
    }

    public void navHomePage(View view){
        if(this.getClass() != MainActivity.class){
            Nav.goToHomePage(view);
        }
    }

    public void goToEducation(View view){
        Nav.goToEducation(view);
    }

    public void btnEducationAnimalsCreatures(View view){
        Intent intent = new Intent(this, EducationSeas.class);
        intent.putExtra("type","creature");
        startActivity(intent);
    }

    public void btnEducationSeas(View view){
        Intent intent = new Intent(this, EducationSeas.class);
        //intent.putExtra("categories",false);
        intent.putExtra("type","fish");
        startActivity(intent);
    }

    public void btnEducationAnimalsOther(View view){
        Intent intent = new Intent(this, EducationSeas.class);
        intent.putExtra("type","other");
        startActivity(intent);
    }

    public void btnAnimalCountryLocations(View view){
        Nav.locationsAnimalsCountry(view);
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
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void loadAllSeasToPage() {
        JSONArray seaArray = loadSeaJson();
        if (seaArray == null) return;

        try {
            // Ana layout
            LinearLayout mainLayout = findViewById(R.id.main_seas_layout);
            mainLayout.removeAllViews();

            for (int i = 0; i < seaArray.length(); i++) {
                JSONObject sea = seaArray.getJSONObject(i);

                // 🔹 Ana satır layout
                LinearLayout seaRow = new LinearLayout(this);
                seaRow.setOrientation(LinearLayout.HORIZONTAL);
                seaRow.setId(i);
                seaRow.setTag("sea_" + i);
                seaRow.setPadding(0, dpToPx(20), 0, dpToPx(20));
                seaRow.setGravity(Gravity.CENTER_VERTICAL);
                seaRow.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), EducationSeasTemplate.class);
                    intent.putExtra("seaId", sea.optInt("id", -1));
                    startActivity(intent);
                });

                // 🔹 Deniz resmi
                ImageView seaImage = new ImageView(this);
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(dpToPx(125), dpToPx(125));
                imageParams.setMargins(0, 0, dpToPx(20), 0);
                seaImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                seaImage.setLayoutParams(imageParams);

                String imageUrl = null;
                if (sea.has("image_paths")) {
                    JSONArray images = sea.getJSONArray("image_paths");
                    if (images.length() > 0) {
                        imageUrl = images.getString(0);
                    }
                }
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(this).load(imageUrl).into(seaImage);
                } else {
                    seaImage.setImageResource(R.drawable.badges);
                }

                // 🔹 Bilgi kısmı
                LinearLayout infoLayout = new LinearLayout(this);
                infoLayout.setOrientation(LinearLayout.VERTICAL);
                infoLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                // Deniz adı
                TextView seaName = new TextView(this);
                seaName.setText(sea.optString("sea_name", "Sea Name"));
                seaName.setTextSize(18);
                seaName.setTextColor(0xFF000000);
                seaName.setTypeface(null, android.graphics.Typeface.BOLD);

                // Alan
                TextView area = new TextView(this);
                area.setText("Area: " + sea.optString("area", "N/A"));
                area.setTextColor(0xFF000000);

                // Konum
                TextView location = new TextView(this);
                location.setText("Location: " + sea.optString("location", "N/A"));
                location.setTextColor(0xFF000000);

                /*
                // Balık sayısı (opsiyonel, JSON’da varsa)
                int fishCount = sea.has("fish_count") ? sea.getInt("fish_count") : 0;
                TextView fishText = new TextView(this);
                fishText.setText(fishCount + " Fishes");
                fishText.setTextColor(0xFF000000);

                // Diğer canlılar
                int creatureCount = sea.has("creature_count") ? sea.getInt("creature_count") : 0;
                TextView creatureText = new TextView(this);
                creatureText.setText(creatureCount + " Creatures");
                creatureText.setTextColor(0xFF000000);

                int otherAnimalsCount = sea.has("other_count") ? sea.getInt("other_count") : 0;
                TextView othersText = new TextView(this);
                othersText.setText(otherAnimalsCount + " Sponges, Plants, Corals");
                othersText.setTextColor(0xFF000000);
                */

                // Ayırıcı çizgi
                View divider = new View(this);
                LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(2));
                dividerParams.setMargins(0, dpToPx(10), 0, 0);
                divider.setLayoutParams(dividerParams);
                divider.setBackgroundColor(0xFF000000);

                // Bilgileri ekle
                infoLayout.addView(seaName);
                infoLayout.addView(area);
                infoLayout.addView(location);
                /*
                infoLayout.addView(fishText);
                infoLayout.addView(creatureText);
                infoLayout.addView(othersText);
                */
                infoLayout.addView(divider);

                // Ana satıra ekle
                seaRow.addView(seaImage);
                seaRow.addView(infoLayout);

                // Parent’e ekle
                mainLayout.addView(seaRow);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "JSON parse error", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnAllSeas(View view){
        Nav.goToAllSeas(view);
    }

}