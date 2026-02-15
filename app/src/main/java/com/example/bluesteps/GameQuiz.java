package com.example.bluesteps;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameQuiz extends AppCompatActivity {

    int userSelectedIndex = 0;
    int correctAnswerIndex = 0;

    String randomQuestionCorrectAnswer = "";
    String randomQuestionExplanation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_quiz);

        NavigationHelper.updateNavigationBar(this, "quiz");
        NavigationHelper.setCenterLogoVisibility(this, true);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        showRandomQuestion();
    }

    public void showAnswerStatus(boolean status, String description){
        // Tıklamaları engelle (Soru çözülürken arkaya basılmasın)
        toggleAnswersClickable(false);

        FrameLayout root_layout = findViewById(R.id.root_layout);
        root_layout.setVisibility(View.VISIBLE);
        root_layout.bringToFront();

        // UI Elemanlarını Bağla
        ImageView answerStatusImage = findViewById(R.id.answerStatusImage);
        TextView answerStatusText = findViewById(R.id.answerStatusText);
        TextView questionDescription = findViewById(R.id.questionDescription);

        if(status){
            // Doğru Cevap Durumu
            answerStatusText.setText(getString(R.string.quiz_right_answer));
            answerStatusText.setTextColor(Color.parseColor("#10B981")); // Modern Green
            answerStatusImage.setImageResource(R.drawable.correct); // Correct ikonun olduğundan emin ol
        } else {
            // Yanlış Cevap Durumu
            answerStatusText.setText(getString(R.string.quiz_wrong_answer));
            answerStatusText.setTextColor(Color.parseColor("#EF4444")); // Modern Red
            answerStatusImage.setImageResource(R.drawable.wrong); // Wrong ikonun olduğundan emin ol
        }

        // Açıklamayı ve Doğru Cevabı Göster
        String fullDescription = getString(R.string.quiz_correct_answer_label) + ": " + randomQuestionCorrectAnswer + "\n\n" + description;
        questionDescription.setText(fullDescription);
    }

    public void showNextQuestion(View view){
        toggleAnswersClickable(true);
        showRandomQuestion();
        FrameLayout root_layout = findViewById(R.id.root_layout);
        root_layout.setVisibility(View.INVISIBLE);
    }

    // Tekrarlanan tıklama açma/kapama işlemini metotlaştırdık
    private void toggleAnswersClickable(boolean isClickable) {
        findViewById(R.id.quizGameAnswer1).setClickable(isClickable);
        findViewById(R.id.quizGameAnswer2).setClickable(isClickable);
        findViewById(R.id.quizGameAnswer3).setClickable(isClickable);
        findViewById(R.id.quizGameAnswer4).setClickable(isClickable);
    }

    private void showRandomQuestion() {
        JSONArray jsonArray = loadJsonFromAssets();
        if (jsonArray == null || jsonArray.length() == 0) return;

        if (usedQuestionIndices.size() >= jsonArray.length()) {
            Toast.makeText(this, "Quiz Finished! Restarting...", Toast.LENGTH_SHORT).show();
            usedQuestionIndices.clear(); // Soruları sıfırla
        }

        int randomIndex;
        Random random = new Random();
        do {
            randomIndex = random.nextInt(jsonArray.length());
        } while (usedQuestionIndices.contains(randomIndex));

        usedQuestionIndices.add(randomIndex);

        try {
            JSONObject questionObj = jsonArray.getJSONObject(randomIndex);
            correctAnswerIndex = questionObj.getInt("correct_answer_index");
            randomQuestionExplanation = questionObj.optString("explanation", "Discover more about this in the education section!");

            JSONArray answers = questionObj.getJSONArray("answers");
            randomQuestionCorrectAnswer = answers.getString(correctAnswerIndex);

            // UI Set
            ((TextView) findViewById(R.id.question)).setText(questionObj.getString("question"));
            ((TextView) findViewById(R.id.answer1)).setText(answers.getString(0));
            ((TextView) findViewById(R.id.answer2)).setText(answers.getString(1));
            ((TextView) findViewById(R.id.answer3)).setText(answers.getString(2));
            ((TextView) findViewById(R.id.answer4)).setText(answers.getString(3));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONArray loadJsonFromAssets() {
        /*
        try {
            InputStream is = getAssets().open("questions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8"));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
        */
        try {
            // Cihazın diline göre raw-tr, raw-pt veya default raw'daki questions.json'ı çeker
            InputStream is = getResources().openRawResource(R.raw.questions);

            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            String json = s.hasNext() ? s.next() : "";
            is.close();

            return new JSONArray(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Set<Integer> usedQuestionIndices = new HashSet<>();

    public void quizGameAnswer1(View view){ userSelectedIndex = 0; handleAnswer(); }
    public void quizGameAnswer2(View view){ userSelectedIndex = 1; handleAnswer(); }
    public void quizGameAnswer3(View view){ userSelectedIndex = 2; handleAnswer(); }
    public void quizGameAnswer4(View view){ userSelectedIndex = 3; handleAnswer(); }

    private void handleAnswer() {
        boolean isCorrect = (userSelectedIndex == correctAnswerIndex);
        showAnswerStatus(isCorrect, randomQuestionExplanation);
    }

    // Navigasyon Metotları (Aynı Kaldı)
    public void navHomePage(View view){ Nav.goToHomePage(view); }
    public void navQuiz(View view){ if(this.getClass() != GameQuiz.class) Nav.goToQuizPage(view); }
    public void goToEducation(View view){ Nav.goToEducation(view); }
    public void btnAboutUs(View view){ Nav.goToAboutUs(view); }
}