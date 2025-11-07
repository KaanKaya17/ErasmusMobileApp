package com.example.bluesteps;

import android.content.Context;

public class dpToPx {
    public static int convertDpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
