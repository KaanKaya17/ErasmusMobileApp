package com.example.bluesteps;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
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

public class GameMatching extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_matching);

        loadJSONDataToPage();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
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

        JSONObject fish = searchFishById(0);
        if (fish != null) {
            try {
                JSONArray colorsArray = fish.getJSONArray("colors");

// 🔸 Renk toplarını göstereceğimiz LinearLayout
                LinearLayout colorsLayout = findViewById(R.id.fish_colors_container);
                colorsLayout.removeAllViews(); // Önceki topları temizle

// 🔸 JSON'daki her renk için döngü
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

                    // 🔹 Yeni bir View oluştur (yuvarlak top)
                    View colorCircle = new View(this);

                    // Boyut ve margin ayarları
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(28), dpToPx(28));
                    params.setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
                    colorCircle.setLayoutParams(params);

                    // 🔹 Yuvarlak görünüm için arka plan tanımla
                    colorCircle.setBackgroundResource(R.drawable.color_circle_shape);

                    // Arka planın rengini JSON’daki renkle değiştir
                    colorCircle.getBackground().setTint(circleColor);

                    // (Opsiyonel) Her topa tıklanınca Toast ile renk kodunu göster
                    colorCircle.setOnClickListener(v -> {
                        android.widget.Toast.makeText(this, "Color: " + colorValue, android.widget.Toast.LENGTH_SHORT).show();
                    });

                    // 🔹 Oluşturulan topu layout’a ekle
                    colorsLayout.addView(colorCircle);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}