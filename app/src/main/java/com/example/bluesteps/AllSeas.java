package com.example.bluesteps;

import android.content.Intent;
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

public class AllSeas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_seas);

        JSONArray jsonSeaArray = loadSeaJson();
        if(jsonSeaArray != null){
            loadAllSeasToPage();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(dpToPx(100), dpToPx(100));
                imageParams.setMargins(0, 0, dpToPx(20), 0);
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

}