package com.example.bluesteps;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/*
public class fishJson {
    public static JSONArray loadFishJson(Context context) {
        try {
            InputStream is = context.getAssets().open("fish.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            int read = is.read(buffer);
            is.close();

            if (read <= 0) return null;

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

public class fishJson {
    public static JSONArray loadFishJson(Context context) {
        try {
            // Android, R.raw.fish_data dendiğinde cihazın diline göre
            // raw-tr, raw-pt veya varsayılan raw klasöründeki dosyayı otomatik seçer.
            InputStream is = context.getResources().openRawResource(R.raw.fish_data);

            // InputStream'i String'e çevirmenin en pratik yollarından biri Scanner kullanmaktır
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