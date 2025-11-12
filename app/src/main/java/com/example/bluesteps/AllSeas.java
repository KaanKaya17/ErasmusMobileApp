package com.example.bluesteps;

import android.content.Context;
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

        JSONArray jsonSeaArray = seaJson.loadSeaJson(this);
        if(jsonSeaArray != null){
            loadAllSeasToPage(this);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public JSONObject searchSeaById(int id) {
        try {
            JSONArray seas = seaJson.loadSeaJson(this);
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

    public void loadAllSeasToPage(Context context) {
        JSONArray seaArray = seaJson.loadSeaJson(this);
        if (seaArray == null) return;

        try {
            // Ana layout
            LinearLayout mainLayout = findViewById(R.id.main_seas_layout);
            mainLayout.removeAllViews();

            for (int i = 0; i < seaArray.length(); i++) {
                JSONObject sea = seaArray.getJSONObject(i);

                //Dış konteyner
                LinearLayout outerContainer = new LinearLayout(this);
                LinearLayout.LayoutParams outerParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                outerParams.setMargins(dpToPx.convertDpToPx(this,12), dpToPx.convertDpToPx(this,8), dpToPx.convertDpToPx(this,12), dpToPx.convertDpToPx(this,8));
                outerContainer.setLayoutParams(outerParams);
                outerContainer.setOrientation(LinearLayout.VERTICAL);
                outerContainer.setPadding(dpToPx.convertDpToPx(this,14), dpToPx.convertDpToPx(this,14), dpToPx.convertDpToPx(this,14), dpToPx.convertDpToPx(this,14));
                outerContainer.setBackgroundResource(R.drawable.card); // köşeleri yuvarlatılmış arka plan
                outerContainer.setElevation(dpToPx.convertDpToPx(this,4));

                //Ana satır layout
                LinearLayout seaRow = new LinearLayout(this);
                seaRow.setOrientation(LinearLayout.HORIZONTAL);
                seaRow.setId(i);
                seaRow.setTag("sea_" + i);
                seaRow.setPadding(0, dpToPx.convertDpToPx(this,20), 0, dpToPx.convertDpToPx(this,20));
                seaRow.setGravity(Gravity.CENTER_VERTICAL);
                seaRow.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), EducationSeasTemplate.class);
                    intent.putExtra("seaId", sea.optInt("id", -1));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });

                //Deniz resmi
                ImageView seaImage = new ImageView(this);
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(dpToPx.convertDpToPx(this,100), dpToPx.convertDpToPx(this,100));
                imageParams.setMargins(0, 0, dpToPx.convertDpToPx(this,20), 0);
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

                //Bilgi kısmı
                LinearLayout infoLayout = new LinearLayout(this);
                infoLayout.setOrientation(LinearLayout.VERTICAL);
                infoLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                // Deniz adı
                TextView seaName = new TextView(new ContextThemeWrapper(context, R.style.TextViewTitle));
                seaName.setText(sea.optString("sea_name", "Sea Name"));
                seaName.setTextSize(18);
                seaName.setTextColor(0xFF000000);
                seaName.setTypeface(null, android.graphics.Typeface.BOLD);

                // Alan
                TextView area = new TextView(new ContextThemeWrapper(context, R.style.TextViewBody));
                area.setText("Area: " + sea.optString("area", "N/A"));

                // Konum
                TextView location = new TextView(new ContextThemeWrapper(context, R.style.TextViewBody));
                location.setText("Location: " + sea.optString("location", "N/A"));

                // Ayırıcı çizgi
                View divider = new View(this);
                LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, dpToPx.convertDpToPx(this,2));
                dividerParams.setMargins(0, dpToPx.convertDpToPx(this,10), 0, 0);
                divider.setLayoutParams(dividerParams);
                divider.setBackgroundColor(Color.parseColor("#575757"));

                // Bilgileri ekle
                infoLayout.addView(seaName);
                infoLayout.addView(area);
                infoLayout.addView(location);
                infoLayout.addView(divider);

                // Ana satıra ekle
                seaRow.addView(seaImage);
                seaRow.addView(infoLayout);

                // Ana satırı dış kapsayıcıya ekle
                outerContainer.addView(seaRow);

                // Parent’e ekle
                mainLayout.addView(outerContainer);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "JSON parse error", Toast.LENGTH_SHORT).show();
        }
    }
    public void navHomePage(View view){
        Nav.goToHomePage(view);
    }
    public void goToEducation(View view){
        Nav.goToEducation(view);
    }
    public void navQuiz(View view){
        Nav.goToQuizPage(view);
    }
    public void btnAboutUs(View view){
        Nav.goToAboutUs(view);
    }

}