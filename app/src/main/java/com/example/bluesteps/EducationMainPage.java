package com.example.bluesteps;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EducationMainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_education_main_page);
        generateCards();
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
    private void openFishDetail(int fishId) {

        Intent intent = new Intent(this, EducationAnimalsTemplate.class);
        intent.putExtra("fish_id", fishId);
        startActivity(intent);
    }
    public void generateCards() {
        LinearLayout cardLayout = findViewById(R.id.cardLayout); // Ana layout

        JSONArray countryArray = loadFishJson(); // JSON'u yükle
        if (countryArray != null) {
            try {
                // Ülkeleri gez
                for (int i = 0; i < countryArray.length(); i++) {
                    JSONObject countryObject = countryArray.getJSONObject(i);
                    JSONArray fishArray = countryObject.getJSONArray("fish");

                    LinearLayout rowLayout = null; // Her satır için Horizontal layout

                    for (int j = 0; j < fishArray.length(); j++) {
                        JSONObject fish = fishArray.getJSONObject(j);
                        int fishId = fish.getInt("id");
                        String fishName = fish.getString("fish_name");

                        // Her 2 balıkta bir yeni satır oluştur
                        if (j % 2 == 0) {
                            rowLayout = new LinearLayout(this);
                            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            ));
                            cardLayout.addView(rowLayout);
                        }

                        // --- Kart oluştur ---
                        LinearLayout card = new LinearLayout(this);
                        card.setOrientation(LinearLayout.VERTICAL);
                        card.setClickable(true);
                        card.setPadding(16, 16, 16, 16);
                        card.setBackgroundResource(R.drawable.card);

                        // Benzersiz ID ve JSON ID ile tag
                        card.setId(View.generateViewId());
                        card.setTag("card_" + fishId);

                        card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String tag = (String) v.getTag(); // örn: "card_0"
                                Toast.makeText(getApplicationContext(), tag + " tıklandı!", Toast.LENGTH_SHORT).show();

                                // İstersen JSON'daki fish_id ile detay sayfasına geçebilirsin
                                int fishId = Integer.parseInt(tag.split("_")[1]);
                                openFishDetail(fishId);
                            }
                        });



                        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1  // weight ile yan yana sığdır
                        );
                        cardParams.setMargins(7, 7, 7, 7);
                        card.setLayoutParams(cardParams);

                        // ImageView
                        ImageView imageView = new ImageView(this);
                        imageView.setImageResource(R.drawable.quiz);
                        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        imageParams.setMargins(0, 0, 0, 10);
                        imageView.setLayoutParams(imageParams);

                        // TextView
                        TextView textView = new TextView(this);
                        textView.setText(fishName);
                        textView.setTextSize(16);
                        textView.setTextColor(Color.parseColor("#066037"));
                        textView.setGravity(Gravity.CENTER);

                        LinearLayout innerLayout = new LinearLayout(this);
                        innerLayout.setOrientation(LinearLayout.HORIZONTAL);
                        innerLayout.setGravity(Gravity.CENTER);
                        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        innerLayout.addView(textView);

                        // Kartın içine ekle
                        card.addView(imageView);
                        card.addView(innerLayout);

                        // Kartı satıra ekle
                        rowLayout.addView(card);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "JSON hatası", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "JSON yüklenemedi", Toast.LENGTH_SHORT).show();
        }
    }






    public void btnEducationAnimals(View view){
        Nav.goToAnimalsTemplate(view);
    }
    public void btnEducationSeas(View view){
        Nav.goToSeasTemplate(view);
    }

}