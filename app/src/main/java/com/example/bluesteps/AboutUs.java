package com.example.bluesteps;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about_us);

        NavigationHelper.updateNavigationBar(this, "about");
        NavigationHelper.setCenterLogoVisibility(this, true); // Logo'yu gizle

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
        if(this.getClass() != AboutUs.class){
            Nav.goToAboutUs(view);
        }
    }
}