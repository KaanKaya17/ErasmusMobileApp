package com.example.bluesteps;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class Nav {

    //context's//

    public static void goToSeasTemplate(Context context){
        startActivitySafely(context, EducationSeasTemplate.class);
    }

    public static void goToAnimalsTemplate(Context context){
        startActivitySafely(context, EducationAnimalsTemplate.class);
    }

    public static void goToAboutUs(Context context){
        startActivitySafely(context, AboutUs.class);
    }
    public static void goToEducation(Context context){
        startActivitySafely(context, EducationSeas.class);
    }
    public void goTo3D(Context context){
        startActivitySafely(context, EducationSeas.class);

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

    public static void goToEducationSeas(Context context){
        startActivitySafely(context, EducationSeas.class);
    }
    public static void goToAllSeas(Context context){
        startActivitySafely(context, AllSeas.class);
    }
    public static void locationsAnimalsCountry(Context context){
        startActivitySafely(context, LocationsCountryAnimals.class);
    }

    //context's//

    // --- View üzerinden geçiş ---
    public static void goToEducation(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), EducationSeas.class);
            // intent.putExtra("type","creature");  <-- BU SATIRI SİL VEYA YORUMA AL
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        }
    }

    public static void getPagesByAnimalType(View view,String type){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), EducationSeas.class);
            intent.putExtra("type",type);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        }
    }

    public static void goTo3D(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), fishModelView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToHomePage(View view){
        if(view != null && view.getContext() != null){
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToMainPage(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToQuizPage(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), GameQuiz.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToAboutUs(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), AboutUs.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToEducationSeas(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), EducationSeas.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToAllSeas(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), AllSeas.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        }
    }
    public static void locationsAnimalsCountry(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), LocationsCountryAnimals.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToAnimalsTemplate(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), EducationAnimalsTemplate.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        }
    }

    public static void goToSeasTemplate(View view){
        if (view != null && view.getContext() != null) {
            Intent intent = new Intent(view.getContext(), EducationSeasTemplate.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        }
    }

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
