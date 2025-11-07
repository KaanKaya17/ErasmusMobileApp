package com.example.bluesteps;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewOutlineProvider;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EducationSeas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_education_seas);


        JSONArray allData = fishJson.loadFishJson(this);
        String countryName = getIntent().getStringExtra("countryName");
        String selectedType = getIntent().getStringExtra("type"); // Örneğin "fish", "sponge", vb.

        JSONArray fishArray = getFishArrayByCountryName(allData, countryName);

        if (selectedType != null && (countryName == null || countryName.isEmpty())) {
            // Ülke seçilmemiş ama tür seçilmişse: tüm ülkelerde o türü göster
            generateAllAnimalCardsByType(selectedType);
        } else if (fishArray != null && selectedType != null) {
            // Ülke ve tür seçilmiş: sadece o ülke ve türü göster
            generateAllAnimalsCardsBySelectedCountry(this, countryName, fishArray, selectedType);
        } else if (fishArray != null) {
            // Sadece ülke seçilmiş: o ülkenin tüm hayvanları
            generateFishCardsFromCountryName(this, countryName, fishArray);
        } else {
            // Hiçbiri yoksa: tüm ülkelerde tüm hayvanlar
            generateAllAnimalCards();
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }



    public void generateAllAnimalCardsByType(String type) {
        LinearLayout parentLayout = findViewById(R.id.parentLayout);
        TextView textviewCategory = findViewById(R.id.textviewCategory);
        textviewCategory.setVisibility(View.GONE);

        TextView textviewTitle = findViewById(R.id.title);
        if (type != null) {
            switch (type.toLowerCase()) {
                case "fish":
                    textviewTitle.setText("Fishes");
                    break;
                case "creature":
                    textviewTitle.setText("Creatures");
                    break;
                default:
                    textviewTitle.setText("Corals, Sponges, Plants");
                    break;
            }
        } else {
            textviewTitle.setText("");
        }

        JSONArray countryArray = fishJson.loadFishJson(this);

        if (countryArray != null) {
            parentLayout.removeAllViews();
            try {
                for (int i = 0; i < countryArray.length(); i++) {
                    JSONObject countryObj = countryArray.getJSONObject(i);
                    JSONArray animalsArray = countryObj.getJSONArray("animals");

                    for (int j = 0; j < animalsArray.length(); j++) {
                        JSONObject obj = animalsArray.getJSONObject(j);
                        if (obj.optString("type","").equalsIgnoreCase(type)) {
                            View cardView = createFishCard(this, obj);
                            parentLayout.addView(cardView);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void generateAllAnimalsCardsBySelectedCountry(Context context, String countryName, JSONArray animalArray, String selectedType) {
        LinearLayout parentLayout = findViewById(R.id.parentLayout);
        TextView title = findViewById(R.id.title);
        TextView textviewType = findViewById(R.id.animalsType);

        if (selectedType != null) {
            switch (selectedType.toLowerCase()) {
                case "fish":
                    textviewType.setText("Fishes");
                    title.setText(countryName + " - " + "Fishes");
                    break;
                case "creature":
                    textviewType.setText("Creatures");
                    title.setText(countryName + " - " + "Creatures");
                    break;
                default:
                    textviewType.setText("Corals, Sponges, Plants");
                    title.setText(countryName + " - " + "Corals, Sponges, Plants");
                    break;
            }
        } else {
            textviewType.setText("");
        }



        if (parentLayout != null) {
            parentLayout.removeAllViews();

            for (int i = 0; i < animalArray.length(); i++) {
                try {
                    JSONObject animal = animalArray.getJSONObject(i);
                    String type = animal.optString("type", "unknown");

                    if (type.equalsIgnoreCase(selectedType)) {
                        View cardView = createFishCard(context, animal);
                        parentLayout.addView(cardView);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void generateAllAnimalCards() {
        LinearLayout parentLayout = findViewById(R.id.parentLayout);
        JSONArray countryArray = fishJson.loadFishJson(this);

        if (countryArray != null) {
            try {
                for (int i = 0; i < countryArray.length(); i++) {
                    JSONObject countryObj = countryArray.getJSONObject(i);
                    JSONArray fishArray = countryObj.getJSONArray("animals");

                    for (int j = 0; j < fishArray.length(); j++) {
                        JSONObject fishObj = fishArray.getJSONObject(j);
                        View cardView = createFishCard(this, fishObj);
                        parentLayout.addView(cardView);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "JSON Error", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "JSON Not Downloaded", Toast.LENGTH_SHORT).show();
        }
    }


    private View createFishCard(Context context, JSONObject obj) throws JSONException {
        String animalName = obj.getString("animal_name"); // artık hepsi için tek alan
        String type = obj.optString("type", "unknown");
        String maxSizeStr = obj.optString("max_size", "N/A");
        String maxDepthStr = obj.optString("max_depth", "N/A");
        int id = obj.getInt("id");

        LinearLayout rowLayout = new LinearLayout(context);
        rowLayout.setOrientation(LinearLayout.VERTICAL);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout card = new LinearLayout(context);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setClickable(true);
        card.setPadding(12, 12, 12, 12);
        card.setBackgroundResource(R.drawable.card);
        card.setId(View.generateViewId());
        card.setTag("card_" + id);

        card.setOnClickListener(v -> {
            int convertedId = Integer.parseInt(((String)v.getTag()).split("_")[1]);
            openFishDetail(convertedId);
        });

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(16, 16, 16, 16);
        card.setElevation(12f);
        card.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        card.setLayoutParams(cardParams);

        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(200, 200);
        imageParams.setMargins(0, 0, 25, 0);
        imageView.setLayoutParams(imageParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (type.equalsIgnoreCase("fish")) {
            imageView.setImageResource(R.drawable.fish);
        } else if (type.equalsIgnoreCase("other")) {
            imageView.setImageResource(R.drawable.coral);
        } else{
            imageView.setImageResource(R.drawable.creature);
        }


        LinearLayout textLayout = new LinearLayout(context);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        TextView tvName = new TextView(new ContextThemeWrapper(context, R.style.TextviewBodySubtitleWhite));
        tvName.setText(animalName);
        tvName.setPadding(0, 0, 0, 2);
        textLayout.addView(tvName);

        if (!maxSizeStr.equals("N/A")) {
            TextView tvSize = new TextView(new ContextThemeWrapper(context, R.style.TextviewBodyWhite));
            tvSize.setText(maxSizeStr + " cm");
            textLayout.addView(tvSize);
        }

        if (!maxDepthStr.equals("N/A")) {
            TextView tvDepth = new TextView(new ContextThemeWrapper(context, R.style.TextviewBodyWhite));
            tvDepth.setText(maxDepthStr + " m depth");
            textLayout.addView(tvDepth);
        }

        card.addView(imageView);
        card.addView(textLayout);

        /*
        View divider = new View(context);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 2);
        dividerParams.setMargins(0, 8, 0, 8);
        divider.setLayoutParams(dividerParams);
        divider.setBackgroundColor(Color.BLACK);
        */

        rowLayout.addView(card);
        //rowLayout.addView(divider);

        return rowLayout;
    }


    public JSONArray getFishArrayByCountryName(JSONArray jsonArray, String countryName) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryObj = jsonArray.getJSONObject(i);
                String country = countryObj.getString("country");

                // Ülke adı eşleşirse o ülkenin fish dizisini döndür
                if (country.equalsIgnoreCase(countryName)) {
                    return countryObj.getJSONArray("animals");
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
                    View cardView = createFishCard(context, fishObj);
                    parentLayout.addView(cardView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void goToEducation(View view){
        if(this.getClass() != EducationSeas.class){
            Nav.goToEducation(view);
        }
    }
    public void navHomePage(View view){
        Nav.goToHomePage(view);
    }
    public void navQuiz(View view){
        Nav.goToQuizPage(view);
    }
    public void btnAboutUs(View view){
        Nav.goToAboutUs(view);
    }
    public void goToEducationSeas(String type, String country){
        String currentType = getIntent().getStringExtra("type");
        String currentCountry = getIntent().getStringExtra("countryName");

        // Eğer aynı filtre ve ülke ise çık
        if (type.equals(currentType) && country.equals(currentCountry)) {
            //Toast.makeText(this, "Zaten bu filtre gösteriliyor", Toast.LENGTH_SHORT).show();
            return;
        }

        // Farklı filtre/ülke ise Activity aç
        Intent intent = new Intent(this, EducationSeas.class);
        intent.putExtra("type", type);
        intent.putExtra("countryName", country);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void countryAllFishes(View view){
        String selectedType = "fish";
        String countryName = getIntent().getStringExtra("countryName");
        String type = getIntent().getStringExtra("type");
        if (countryName == null && type != null){
            if(!selectedType.equalsIgnoreCase(type)){
                Nav.getPagesByAnimalType(view,"creature");
            }
        }
        else{
            goToEducationSeas("fish", countryName);
        }

    }

    public void countryAllCreatures(View view){
        String selectedType = "creature";
        String countryName = getIntent().getStringExtra("countryName");
        String type = getIntent().getStringExtra("type");
        if (countryName == null && type != null){
            if(!selectedType.equalsIgnoreCase(type)){
                Nav.getPagesByAnimalType(view,"creature");
            }
        }
        else{
            goToEducationSeas("creature", countryName);
        }
    }

    public void countryAllOther(View view){
        String selectedType = "other";
        String countryName = getIntent().getStringExtra("countryName");
        String type = getIntent().getStringExtra("type");
        if (countryName == null && type != null){
            if(!selectedType.equalsIgnoreCase(type)){
                Nav.getPagesByAnimalType(view,"other");
            }
        }
        else{
            goToEducationSeas("other", countryName);
        }
    }




}