package com.example.bluesteps;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

public class EducationSeas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_education_seas);

        JSONArray allData = loadFishJson();
        String countryNameIntent = getIntent().getStringExtra("countryName");
        JSONArray fishArray = getFishArrayByCountryName(allData, countryNameIntent);
        if(fishArray != null){
            generateFishCardsFromCountryName(this, countryNameIntent, fishArray); // burayı countryNameIntent yap
        }
        else{
            generateCards();
        }

        //Toast.makeText(getApplicationContext(), getCountryName, Toast.LENGTH_SHORT).show();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void generateCards() {
        LinearLayout parentLayout = findViewById(R.id.parentLayout); // Ana layout

        JSONArray countryArray = loadFishJson(); // JSON verisini yükle
        if (countryArray != null) {
            try {
                // Ülkeleri gez
                for (int i = 0; i < countryArray.length(); i++) {
                    JSONObject countryObject = countryArray.getJSONObject(i);
                    JSONArray fishArray = countryObject.getJSONArray("fish");

                    for (int j = 0; j < fishArray.length(); j++) {
                        JSONObject fishObj = fishArray.getJSONObject(j);
                        String fishName = fishObj.getString("fish_name");
                        String fishSizeStr = fishObj.optString("max_size", "N/A");
                        String fishDepthStr = fishObj.optString("max_depth", "N/A");
                        int fishId = fishObj.getInt("id");

                        // --- Satır layout ---
                        LinearLayout rowLayout = new LinearLayout(this);
                        rowLayout.setOrientation(LinearLayout.VERTICAL);
                        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));

                        // --- Kart layout ---
                        LinearLayout card = new LinearLayout(this);
                        card.setOrientation(LinearLayout.HORIZONTAL);
                        card.setGravity(Gravity.CENTER_VERTICAL);
                        card.setClickable(true);
                        card.setPadding(12, 12, 12, 12);
                        card.setBackgroundResource(R.drawable.card);
                        card.setId(View.generateViewId());
                        card.setTag("card_" + fishId);

                        card.setOnClickListener(v -> {
                            String tag = (String) v.getTag();
                            Toast.makeText(getApplicationContext(), tag + " tıklandı!", Toast.LENGTH_SHORT).show();

                            int convertedFishId = Integer.parseInt(tag.split("_")[1]);
                            openFishDetail(convertedFishId);
                        });

                        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        cardParams.setMargins(16, 16, 16, 16);
                        card.setLayoutParams(cardParams);

                        // --- ImageView ---
                        ImageView imageView = new ImageView(this);
                        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(100, 100);
                        imageParams.setMargins(0, 0, 16, 0);
                        imageView.setLayoutParams(imageParams);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setImageResource(R.drawable.quiz);

                        // --- Sağdaki text layout ---
                        LinearLayout textLayout = new LinearLayout(this);
                        textLayout.setOrientation(LinearLayout.VERTICAL);
                        textLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));

                        // Fish Name
                        TextView tvName = new TextView(this);
                        tvName.setText(fishName);
                        tvName.setTextColor(Color.parseColor("#066037"));
                        tvName.setTextSize(16);
                        tvName.setGravity(Gravity.CENTER_VERTICAL);
                        tvName.setPadding(0, 0, 0, 2);
                        textLayout.addView(tvName);

                        // Fish Size
                        TextView tvSize = new TextView(this);
                        tvSize.setText(fishSizeStr + " cm");
                        tvSize.setTextColor(Color.parseColor("#066037"));
                        tvSize.setTextSize(12);
                        tvSize.setGravity(Gravity.CENTER_VERTICAL);
                        tvSize.setPadding(0, 0, 0, 2);
                        textLayout.addView(tvSize);

                        // Fish Depth
                        TextView tvDepth = new TextView(this);
                        tvDepth.setText(fishDepthStr + " m depth");
                        tvDepth.setTextColor(Color.parseColor("#066037"));
                        tvDepth.setTextSize(12);
                        tvDepth.setGravity(Gravity.CENTER_VERTICAL);
                        textLayout.addView(tvDepth);

                        // Kart içine ekle
                        card.addView(imageView);
                        card.addView(textLayout);

                        // --- Divider ---
                        View divider = new View(this);
                        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                2
                        );
                        dividerParams.setMargins(0, 8, 0, 8);
                        divider.setLayoutParams(dividerParams);
                        divider.setBackgroundColor(Color.BLACK);

                        // Row layout'a ekle
                        rowLayout.addView(card);
                        rowLayout.addView(divider);

                        // Ana layout'a ekle
                        parentLayout.addView(rowLayout);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "JSON hatası", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "JSON yüklenemedi", Toast.LENGTH_SHORT).show();
        }
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

    public void generateFishCardsFromCountryName(Context context, String countryName, JSONArray fishArray) {
        LinearLayout parentLayout = findViewById(R.id.parentLayout);
        TextView title = findViewById(R.id.title);
        title.setText(countryName);

        if (parentLayout != null) {
            parentLayout.removeAllViews();

            for (int i = 0; i < fishArray.length(); i++) {
                try {
                    JSONObject fishObj = fishArray.getJSONObject(i);
                    String fishName = fishObj.getString("fish_name");
                    String fishSizeStr = fishObj.optString("max_size", "N/A");
                    String fishDepthStr = fishObj.optString("max_depth", "N/A");
                    int fishId = fishObj.getInt("id");

                    // --- Satır layout ---
                    LinearLayout rowLayout = new LinearLayout(context);
                    rowLayout.setOrientation(LinearLayout.VERTICAL);
                    rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));

                    // --- Kart layout ---
                    LinearLayout card = new LinearLayout(context);
                    card.setOrientation(LinearLayout.HORIZONTAL);
                    card.setGravity(Gravity.CENTER_VERTICAL);
                    card.setClickable(true);
                    card.setPadding(12, 12, 12, 12);
                    card.setBackgroundResource(R.drawable.card);
                    card.setId(View.generateViewId());
                    card.setTag("card_" + fishId);

                    card.setOnClickListener(v -> {
                        String tag = (String) v.getTag();
                        Toast.makeText(getApplicationContext(), tag + " tıklandı!", Toast.LENGTH_SHORT).show();

                        int convertedFishId = Integer.parseInt(tag.split("_")[1]);
                        openFishDetail(convertedFishId);
                    });

                    LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    cardParams.setMargins(16, 16, 16, 16);
                    card.setLayoutParams(cardParams);

                    // --- ImageView ---
                    ImageView imageView = new ImageView(context);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(100, 100);
                    imageParams.setMargins(0, 0, 16, 0);
                    imageView.setLayoutParams(imageParams);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageResource(R.drawable.quiz);

                    // --- Sağdaki text layout ---
                    LinearLayout textLayout = new LinearLayout(context);
                    textLayout.setOrientation(LinearLayout.VERTICAL);
                    textLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));

                    // Fish Name
                    TextView tvName = new TextView(context);
                    tvName.setText(fishName);
                    tvName.setTextColor(Color.parseColor("#066037"));
                    tvName.setTextSize(16);
                    tvName.setGravity(Gravity.CENTER_VERTICAL);
                    tvName.setPadding(0, 0, 0, 2);
                    textLayout.addView(tvName);

                    // Fish Size
                    TextView tvSize = new TextView(context);
                    tvSize.setText(fishSizeStr + " cm");
                    tvSize.setTextColor(Color.parseColor("#066037"));
                    tvSize.setTextSize(12);
                    tvSize.setGravity(Gravity.CENTER_VERTICAL);
                    tvSize.setPadding(0, 0, 0, 2);
                    textLayout.addView(tvSize);

                    // Fish Depth
                    TextView tvDepth = new TextView(context);
                    tvDepth.setText(fishDepthStr + " m depth");
                    tvDepth.setTextColor(Color.parseColor("#066037"));
                    tvDepth.setTextSize(12);
                    tvDepth.setGravity(Gravity.CENTER_VERTICAL);
                    textLayout.addView(tvDepth);

                    // Kart içine ekle
                    card.addView(imageView);
                    card.addView(textLayout);

                    // --- Divider ---
                    View divider = new View(context);
                    LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                    );
                    dividerParams.setMargins(0, 8, 0, 8);
                    divider.setLayoutParams(dividerParams);
                    divider.setBackgroundColor(Color.BLACK);

                    // Row layout'a ekle
                    rowLayout.addView(card);
                    rowLayout.addView(divider);

                    // Ana layout'a ekle
                    parentLayout.addView(rowLayout);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public void btnEducation(View view){
        Nav.goToEducation(view);
    }


}