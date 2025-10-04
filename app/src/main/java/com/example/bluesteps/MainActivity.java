package com.example.bluesteps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void btnBadges(View view){
        Nav.goToBadgesPage(view);
    }
    public void btnGames(View view){
        Nav.goToGamePage(view);
    }
    public void btnEducation(View view){
        Nav.goToEducation(view);
    }
    public void btnQuiz(View view){
        Nav.goToQuizPage(view);
    }
    public void btnMainPage(View view){
        Nav.goToMainPage(view);
    }
    public void btnUserProfile(View view){
        Nav.goToUserProfile(view);
    }
    public void btnAboutUs(View view){
        Nav.goToAboutUs(view);
    }

}