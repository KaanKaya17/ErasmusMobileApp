package com.example.bluesteps;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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
