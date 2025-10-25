package com.example.bluesteps;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class Nav {

    // --- View üzerinden geçiş ---
    // --- Main Page ---
    public static void goToEducation(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), EducationMainPage.class);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToHomePage(View view){
        if(view != null && view.getContext() != null){
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            view.getContext().startActivity(intent);
        }
    }
    public static void goToMainPage(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToQuizPage(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), GameQuiz.class);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToBadgesPage(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), Badges.class);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToGamePage(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), GamesMainPage.class);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToAboutUs(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), AboutUs.class);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToUserProfile(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), UserProfile.class);
            view.getContext().startActivity(intent);
        }
    }

    // --- Context üzerinden geçiş ---
    public static void goToUserProfile(Context context){
        startActivitySafely(context, UserProfile.class);
    }
    public static void goToAboutUs(Context context){
        startActivitySafely(context, AboutUs.class);
    }
    public static void goToEducation(Context context){
        startActivitySafely(context, EducationMainPage.class);
    }
    public static void goToMainPage(Context context){
        startActivitySafely(context, EducationMainPage.class);
    }

    public static void goToHomePage(Context context){
        startActivitySafely(context,MainActivity.class);
    }

    public static void goToQuizPage(Context context){
        startActivitySafely(context, GameQuiz.class);
    }

    public static void goToBadgesPage(Context context){
        startActivitySafely(context, Badges.class);
    }

    public static void goToGamePage(Context context){
        startActivitySafely(context, GamesMainPage.class);
    }
    // --- Main Page End ---


    // --- Education Main Page ---

    public static void goToEducationSeas(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), EducationSeas.class);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToEducationAnimals(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), EducationSeaAnimals.class);
            intent.putExtra("fish_id",3);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToEducationSeas(Context context){
        startActivitySafely(context, EducationSeas.class);
    }
    public static void goToEducationAnimals(Context context){
        startActivitySafely(context, EducationSeaAnimals.class);
    }

    // --- Education Main Page End ---

    // --- Game Main Page ---

    public static void goToMatching(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), GameMatching.class);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToQuiz(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), GameQuiz.class);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToMatching(Context context){
        startActivitySafely(context, GameMatching.class);
    }
    public static void goToQuiz(Context context){
        startActivitySafely(context, GameQuiz.class);
    }

    // --- Game Main Page End ---

    // --- Animals Template ---

    public static void goToAnimalsTemplate(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), EducationAnimalsTemplate.class);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToAnimalsTemplate(Context context){
        startActivitySafely(context, EducationAnimalsTemplate.class);
    }

    // --- Animals Template End ---


    // --- Seas Template ---

    public static void goToSeasTemplate(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), EducationSeasTemplate.class);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToSeasTemplate(Context context){
        startActivitySafely(context, EducationSeasTemplate.class);
    }

    // --- Seas Template End ---




    // --- Ortak metod, crash önleme ---
    private static void startActivitySafely(Context context, Class<?> targetActivity) {
        if (context == null) return;

        Intent intent = new Intent(context, targetActivity);

        // Eğer context Activity değilse, yeni task başlat
        if (!(context instanceof android.app.Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        context.startActivity(intent);
    }
}
