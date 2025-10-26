package com.example.bluesteps;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class EducationSeas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_education_seas);

        JSONArray allData = loadFishJson(); // senin fish.json verini yüklüyor
        JSONArray fishArray = getFishArrayByCountryName(allData, "Türkiye");

        if (fishArray != null) {
            generateFishCardsFromCountryName(this, "Türkiye", fishArray);
        } else {
            Toast.makeText(this, "Ülke bulunamadı!", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(getApplicationContext(), getCountryName, Toast.LENGTH_SHORT).show();

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
    public JSONArray getFishArrayByCountryName(JSONArray jsonArray, String countryName) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryObj = jsonArray.getJSONObject(i);
                String country = countryObj.getString("country");

                // Ülke adı eşleşirse o ülkenin fish dizisini döndür
                if (country.equalsIgnoreCase(countryName)) {
                    return countryObj.getJSONArray("fish");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // ülke bulunamazsa null döndür
    }

    public void openFishDetail(int fishId){
        Intent intent = new Intent(this,EducationAnimalsTemplate.class);
        intent.putExtra("fish_id",fishId);
        startActivity(intent);
    }


    public void generateFishCardsFromCountryName(Context context, String countryName, JSONArray fishArray){
        LinearLayout parentLayout = findViewById(R.id.parentLayout);
        if (parentLayout != null) {
            parentLayout.removeAllViews(); // önceki kartları temizle
            for (int i = 0; i < fishArray.length(); i++) {
                try {

                    JSONObject fishObj = fishArray.getJSONObject(i);
                    String fishName = fishObj.getString("fish_name");
                    int fishId = fishObj.getInt("id");

                    // Kart için LinearLayout oluştur
                    LinearLayout cardLayout = new LinearLayout(context);
                    cardLayout.setOrientation(LinearLayout.VERTICAL);
                    cardLayout.setId(i);
                    cardLayout.setTag("card_" + fishId);
                    cardLayout.setPadding(24, 24, 24, 24);
                    LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    cardLayout.setOnClickListener(v -> {
                        String tag = (String) v.getTag();
                        Toast.makeText(getApplicationContext(), tag + " tıklandı!", Toast.LENGTH_SHORT).show();
                        int convertedFishId = Integer.parseInt(tag.split("_")[1]);
                        openFishDetail(convertedFishId);
                    });
                    cardParams.setMargins(16, 16, 16, 16);
                    cardLayout.setLayoutParams(cardParams);
                    cardLayout.setBackgroundResource(R.drawable.rounded_box); // arkaplan drawable

                    // İçerik TextView oluştur
                    TextView textView = new TextView(context);
                    textView.setText("• " + fishName);
                    textView.setTextSize(18);
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                    textView.setPadding(8, 8, 8, 8);

                    cardLayout.addView(textView);
                    parentLayout.addView(cardLayout);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}