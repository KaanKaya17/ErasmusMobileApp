package com.example.bluesteps;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;

public class NavigationHelper {

    /**
     * Navigation bar'daki aktif sekmeyi günceller
     * @param activity Mevcut activity
     * @param activeTab Hangi sekme aktif ("home", "quiz", "education", "about")
     */
    public static void updateNavigationBar(Activity activity, String activeTab) {
        if (activity == null) return;

        // Bottom bar içindeki tüm sekmeleri bul
        LinearLayout bottomBar = activity.findViewById(R.id.bottom_bar_container);
        if (bottomBar == null) return;

        // Tüm sekmeleri default duruma getir
        resetAllTabs(bottomBar);

        // Aktif sekmeyi vurgula
        switch (activeTab.toLowerCase()) {
            case "home":
                setActiveTab(bottomBar, 0);
                break;
            case "quiz":
                setActiveTab(bottomBar, 1);
                break;
            case "education":
                setActiveTab(bottomBar, 3);
                break;
            case "about":
                setActiveTab(bottomBar, 4);
                break;
        }
    }

    /**
     * Tüm sekmeleri varsayılan (pasif) duruma getirir
     */
    private static void resetAllTabs(LinearLayout bottomBar) {
        for (int i = 0; i < bottomBar.getChildCount(); i++) {
            View child = bottomBar.getChildAt(i);

            // Skip orta boşluk (index 2)
            if (i == 2) continue;

            if (child instanceof LinearLayout) {
                LinearLayout tabLayout = (LinearLayout) child;

                // CardView varsa kaldır
                for (int j = 0; j < tabLayout.getChildCount(); j++) {
                    View tabChild = tabLayout.getChildAt(j);
                    if (tabChild instanceof CardView) {
                        CardView cardView = (CardView) tabChild;
                        // CardView'i kaldır, ImageView'i direkt göster
                        ImageView imageView = (ImageView) cardView.getChildAt(0);
                        if (imageView != null) {
                            tabLayout.removeView(cardView);

                            ImageView newImageView = new ImageView(tabLayout.getContext());
                            newImageView.setLayoutParams(new LinearLayout.LayoutParams(
                                    dpToPx.convertDpToPx(tabLayout.getContext(), 28),
                                    dpToPx.convertDpToPx(tabLayout.getContext(), 28)
                            ));
                            newImageView.setImageDrawable(imageView.getDrawable());
                            newImageView.setAlpha(0.6f);

                            tabLayout.addView(newImageView, 0);
                        }
                        break;
                    } else if (tabChild instanceof ImageView) {
                        ImageView imageView = (ImageView) tabChild;
                        imageView.setAlpha(0.6f);
                    }
                }

                // TextView rengini gri yap
                for (int j = 0; j < tabLayout.getChildCount(); j++) {
                    View tabChild = tabLayout.getChildAt(j);
                    if (tabChild instanceof TextView) {
                        TextView textView = (TextView) tabChild;
                        textView.setTextColor(Color.parseColor("#64748B"));
                        textView.setTypeface(null, android.graphics.Typeface.NORMAL);
                    }
                }
            }
        }
    }

    /**
     * Belirtilen sekmeyi aktif yapar
     */
    private static void setActiveTab(LinearLayout bottomBar, int tabIndex) {
        View child = bottomBar.getChildAt(tabIndex);

        if (child instanceof LinearLayout) {
            LinearLayout tabLayout = (LinearLayout) child;

            // ImageView'i CardView içine al
            for (int i = 0; i < tabLayout.getChildCount(); i++) {
                View tabChild = tabLayout.getChildAt(i);
                if (tabChild instanceof ImageView) {
                    ImageView imageView = (ImageView) tabChild;
                    tabLayout.removeView(imageView);

                    // Yeni CardView oluştur
                    CardView cardView = new CardView(tabLayout.getContext());
                    LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    cardView.setLayoutParams(cardParams);
                    cardView.setRadius(dpToPx.convertDpToPx(tabLayout.getContext(), 12));
                    cardView.setCardElevation(0);
                    cardView.setCardBackgroundColor(Color.parseColor("#E0F2FE"));

                    // Yeni ImageView oluştur
                    ImageView newImageView = new ImageView(tabLayout.getContext());
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                            dpToPx.convertDpToPx(tabLayout.getContext(), 32),
                            dpToPx.convertDpToPx(tabLayout.getContext(), 32)
                    );
                    newImageView.setLayoutParams(imageParams);
                    newImageView.setPadding(
                            dpToPx.convertDpToPx(tabLayout.getContext(), 6),
                            dpToPx.convertDpToPx(tabLayout.getContext(), 6),
                            dpToPx.convertDpToPx(tabLayout.getContext(), 6),
                            dpToPx.convertDpToPx(tabLayout.getContext(), 6)
                    );
                    newImageView.setImageDrawable(imageView.getDrawable());
                    newImageView.setAlpha(1.0f);

                    cardView.addView(newImageView);
                    tabLayout.addView(cardView, 0);
                    break;
                }
            }

            // TextView'i mavi ve bold yap
            for (int i = 0; i < tabLayout.getChildCount(); i++) {
                View tabChild = tabLayout.getChildAt(i);
                if (tabChild instanceof TextView) {
                    TextView textView = (TextView) tabChild;
                    textView.setTextColor(Color.parseColor("#0369A1"));
                    textView.setTypeface(null, android.graphics.Typeface.BOLD);
                }
            }
        }
    }

    /**
     * Merkez logoyu gizle/göster
     * @param activity Mevcut activity
     * @param show true = göster, false = gizle
     */
    public static void setCenterLogoVisibility(Activity activity, boolean show) {
        if (activity == null) return;

        View centerLogo = activity.findViewById(R.id.center_logo);
        if (centerLogo != null && centerLogo.getParent() instanceof View) {
            View logoContainer = (View) centerLogo.getParent();
            logoContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}