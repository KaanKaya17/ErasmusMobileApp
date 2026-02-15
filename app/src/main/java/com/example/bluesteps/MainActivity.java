package com.example.bluesteps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        if (!language.isEmpty()) {
            setLocaleWithoutRestart(language); // Restart içermeyen hali
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //locate

        Spinner spinner = findViewById(R.id.languageSpinner);

        // String array'i Spinner ile bağlıyoruz
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages_array, android.R.layout.simple_spinner_item);

        // Liste açıldığında nasıl görüneceğini seçiyoruz
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter'ı Spinner'a set ediyoruz
        spinner.setAdapter(adapter);

        // Bir seçenek seçildiğinde ne olacağını dinliyoruz
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 1: setLocale("en"); break;
                    case 2: setLocale("tr"); break;
                    case 3: setLocale("pt"); break;
                    case 4: setLocale("el"); break;
                    case 5: setLocale("bg"); break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //locate end in onCreate


        // Navigation bar'ı güncelle - Home aktif
        NavigationHelper.updateNavigationBar(this, "home");
        NavigationHelper.setCenterLogoVisibility(this, true);

        JSONArray jsonArray = fishJson.loadFishJson(this);
        JSONArray jsonSeaArray = seaJson.loadSeaJson(this);
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

    public void setLocaleWithoutRestart(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration config = resources.getConfiguration();

        // Yeni dili konfigürasyona set ediyoruz
        config.setLocale(locale);

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", langCode);
        editor.apply();

        // Ayarları güncelliyoruz
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        // Değişikliğin görünmesi için Activity'yi yeniden başlatıyoruz
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish(); // Mevcut activity'yi kapatıyoruz ki eskisi arkada kalmasın
    }
    // locate end

    @Override
    protected void onResume() {
        super.onResume();
        // Activity geri geldiğinde navigation bar'ı güncelle
        NavigationHelper.updateNavigationBar(this, "home");
        NavigationHelper.setCenterLogoVisibility(this, true);
    }

    private void loadCountriesFromJson(Context context, JSONArray countriesArray) {
        LinearLayout parentLayout = findViewById(R.id.parentLayout);

        for (int i = 0; i < countriesArray.length(); i++) {
            try {
                JSONObject countryObj = countriesArray.getJSONObject(i);
                String countryName = countryObj.getString("country");

                String imageName = countryObj.optString("country_image", "country");
                int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

                // Tür sayacı
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
                            otherAnimalsCount++;
                        }
                    }
                }

                // Modern CardView
                CardView cardView = new CardView(context);
                CardView.LayoutParams cardParams = new CardView.LayoutParams(
                        CardView.LayoutParams.MATCH_PARENT,
                        CardView.LayoutParams.WRAP_CONTENT
                );
                cardParams.setMargins(0, 0, 0, dpToPx.convertDpToPx(this, 12));
                cardView.setLayoutParams(cardParams);
                cardView.setRadius(dpToPx.convertDpToPx(this, 16));
                cardView.setCardElevation(dpToPx.convertDpToPx(this, 4));
                cardView.setCardBackgroundColor(Color.WHITE);
                cardView.setClickable(true);
                cardView.setFocusable(true);
                cardView.setForeground(context.getDrawable(android.R.drawable.list_selector_background));

                // Ana layout içeriği
                LinearLayout cardContent = new LinearLayout(context);
                cardContent.setOrientation(LinearLayout.HORIZONTAL);
                cardContent.setPadding(
                        dpToPx.convertDpToPx(this, 16),
                        dpToPx.convertDpToPx(this, 16),
                        dpToPx.convertDpToPx(this, 16),
                        dpToPx.convertDpToPx(this, 16)
                );
                cardContent.setGravity(Gravity.CENTER_VERTICAL);
                cardContent.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), EducationSeas.class);
                    intent.putExtra("countryName", countryName);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });

                // Ülke resmi (yuvarlak)
                CardView imageCard = new CardView(context);
                LinearLayout.LayoutParams imageCardParams = new LinearLayout.LayoutParams(
                        dpToPx.convertDpToPx(this, 70),
                        dpToPx.convertDpToPx(this, 70)
                );
                imageCardParams.setMargins(0, 0, dpToPx.convertDpToPx(this, 16), 0);
                imageCard.setLayoutParams(imageCardParams);
                imageCard.setRadius(dpToPx.convertDpToPx(this, 12));
                imageCard.setCardElevation(0);
                imageCard.setPreventCornerOverlap(false);
                imageCard.setUseCompatPadding(false);

                ImageView flagImage = new ImageView(context);
                flagImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                flagImage.setAdjustViewBounds(true);
                flagImage.setLayoutParams(new CardView.LayoutParams(
                        CardView.LayoutParams.MATCH_PARENT,
                        CardView.LayoutParams.MATCH_PARENT
                ));

                if (imageResId != 0) {
                    flagImage.setImageResource(imageResId);
                } else {
                    flagImage.setImageResource(R.drawable.country);
                }

                imageCard.addView(flagImage);

                // Bilgi kısmı
                LinearLayout infoLayout = new LinearLayout(context);
                infoLayout.setOrientation(LinearLayout.VERTICAL);
                infoLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                ));

                // Ülke adı
                TextView countryText = new TextView(context);
                countryText.setText(countryName);
                countryText.setTextColor(Color.parseColor("#1A1A1A"));
                countryText.setTextSize(18);
                countryText.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                nameParams.setMargins(0, 0, 0, dpToPx.convertDpToPx(this, 6));
                countryText.setLayoutParams(nameParams);

                // Balık sayısı
                TextView fishText = new TextView(context);
                fishText.setText(fishCount + " " + getString(R.string.main_categories_fish));
                fishText.setTextColor(Color.parseColor("#6B7280"));
                fishText.setTextSize(14);
                LinearLayout.LayoutParams fishParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                fishParams.setMargins(0, 0, 0, dpToPx.convertDpToPx(this, 4));
                fishText.setLayoutParams(fishParams);

                // Canlı sayısı
                TextView creatureText = new TextView(context);
                creatureText.setText(creatureCount + " " + getString(R.string.main_categories_creatures));
                creatureText.setTextColor(Color.parseColor("#6B7280"));
                creatureText.setTextSize(14);
                LinearLayout.LayoutParams creatureParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                creatureParams.setMargins(0, 0, 0, dpToPx.convertDpToPx(this, 4));
                creatureText.setLayoutParams(creatureParams);

                // Diğer canlı sayısı
                TextView othersText = new TextView(context);
                othersText.setText(otherAnimalsCount + " " + getString(R.string.main_all_other_animals));
                othersText.setTextColor(Color.parseColor("#6B7280"));
                othersText.setTextSize(14);

                // Bilgileri ekle
                infoLayout.addView(countryText);
                infoLayout.addView(fishText);
                infoLayout.addView(creatureText);
                infoLayout.addView(othersText);

                // İçeriğe ekle
                cardContent.addView(imageCard);
                cardContent.addView(infoLayout);

                // CardView'e içeriği ekle
                cardView.addView(cardContent);

                // Parent'e ekle
                parentLayout.addView(cardView);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadAllSeasToPage() {
        JSONArray seaArray = seaJson.loadSeaJson(this);
        if (seaArray == null) return;

        try {
            LinearLayout mainLayout = findViewById(R.id.main_seas_layout);
            mainLayout.removeAllViews();

            for (int i = 0; i < seaArray.length(); i++) {
                JSONObject sea = seaArray.getJSONObject(i);

                // Modern CardView
                CardView cardView = new CardView(this);
                CardView.LayoutParams cardParams = new CardView.LayoutParams(
                        CardView.LayoutParams.MATCH_PARENT,
                        CardView.LayoutParams.WRAP_CONTENT
                );
                cardParams.setMargins(0, 0, 0, dpToPx.convertDpToPx(this, 12));
                cardView.setLayoutParams(cardParams);
                cardView.setRadius(dpToPx.convertDpToPx(this, 16));
                cardView.setCardElevation(dpToPx.convertDpToPx(this, 4));
                cardView.setCardBackgroundColor(Color.WHITE);
                cardView.setClickable(true);
                cardView.setFocusable(true);
                cardView.setForeground(getDrawable(android.R.drawable.list_selector_background));

                // Ana satır
                LinearLayout seaRow = new LinearLayout(this);
                seaRow.setOrientation(LinearLayout.HORIZONTAL);
                seaRow.setPadding(
                        dpToPx.convertDpToPx(this, 16),
                        dpToPx.convertDpToPx(this, 16),
                        dpToPx.convertDpToPx(this, 16),
                        dpToPx.convertDpToPx(this, 16)
                );
                seaRow.setGravity(Gravity.CENTER_VERTICAL);
                seaRow.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), EducationSeasTemplate.class);
                    intent.putExtra("seaId", sea.optInt("id", -1));
                    startActivity(intent);
                });

                // Deniz resmi (yuvarlak)
                CardView imageCard = new CardView(this);
                LinearLayout.LayoutParams imageCardParams = new LinearLayout.LayoutParams(
                        dpToPx.convertDpToPx(this, 70),
                        dpToPx.convertDpToPx(this, 70)
                );
                imageCardParams.setMargins(0, 0, dpToPx.convertDpToPx(this, 16), 0);
                imageCard.setLayoutParams(imageCardParams);
                imageCard.setRadius(dpToPx.convertDpToPx(this, 12));
                imageCard.setCardElevation(0);
                imageCard.setPreventCornerOverlap(false);
                imageCard.setUseCompatPadding(false);

                ImageView seaImage = new ImageView(this);
                seaImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                seaImage.setAdjustViewBounds(true);
                seaImage.setLayoutParams(new CardView.LayoutParams(
                        CardView.LayoutParams.MATCH_PARENT,
                        CardView.LayoutParams.MATCH_PARENT
                ));
                seaImage.setImageResource(R.drawable.sea);

                imageCard.addView(seaImage);

                // Bilgi kısmı
                LinearLayout infoLayout = new LinearLayout(this);
                infoLayout.setOrientation(LinearLayout.VERTICAL);
                infoLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                ));

                // Deniz adı
                TextView seaName = new TextView(this);
                seaName.setText(sea.optString("sea_name", "Sea Name"));
                seaName.setTextColor(Color.parseColor("#1A1A1A"));
                seaName.setTextSize(18);
                seaName.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                nameParams.setMargins(0, 0, 0, dpToPx.convertDpToPx(this, 6));
                seaName.setLayoutParams(nameParams);

                // Alan
                TextView area = new TextView(this);
                area.setText("Area: " + sea.optString("area", "N/A"));
                area.setTextColor(Color.parseColor("#6B7280"));
                area.setTextSize(14);
                LinearLayout.LayoutParams areaParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                areaParams.setMargins(0, 0, 0, dpToPx.convertDpToPx(this, 4));
                area.setLayoutParams(areaParams);

                // Konum
                TextView location = new TextView(this);
                location.setText("Location: " + sea.optString("location", "N/A"));
                location.setTextColor(Color.parseColor("#6B7280"));
                location.setTextSize(14);

                // Bilgileri ekle
                infoLayout.addView(seaName);
                infoLayout.addView(area);
                infoLayout.addView(location);

                // Ana satıra ekle
                seaRow.addView(imageCard);
                seaRow.addView(infoLayout);

                // CardView'e ekle
                cardView.addView(seaRow);

                // Parent'e ekle
                mainLayout.addView(cardView);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "JSON parse error", Toast.LENGTH_SHORT).show();
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
        Nav.getPagesByAnimalType(view,"creature");
    }

    public void btnEducationSeas(View view){
        Nav.getPagesByAnimalType(view,"fish");
    }

    public void btnEducationAnimalsOther(View view){
        Nav.getPagesByAnimalType(view,"other");
    }

    public void btnAnimalCountryLocations(View view){
        Nav.locationsAnimalsCountry(view);
    }

    public void btnAllSeas(View view){
        Nav.goToAllSeas(view);
    }
}