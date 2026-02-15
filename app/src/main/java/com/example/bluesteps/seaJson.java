package com.example.bluesteps;

import android.content.Context;

import org.json.JSONArray;

import java.io.InputStream;

/*
public class seaJson {
    public static JSONArray loadSeaJson(Context context) {
        try {
            InputStream is = context.getAssets().open("sea.json");
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
}
*/
import java.util.Scanner;

public class seaJson {
    public static JSONArray loadSeaJson(Context context) {
        try {
            // R.raw.seas_data diyerek Android'in cihaz diline göre
            // doğru klasörü (raw, raw-tr, raw-pt) seçmesini sağlıyoruz.
            InputStream is = context.getResources().openRawResource(R.raw.sea_data);

            // Veriyi okumak için Scanner en pratik yoldur
            Scanner s = new Scanner(is).useDelimiter("\\A");
            String json = s.hasNext() ? s.next() : "";

            is.close();
            return new JSONArray(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}