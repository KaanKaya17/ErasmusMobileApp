package com.example.bluesteps;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_quiz);

        //showAnswerStatus(true,"Test");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        showRandomQuestion();
    }

    // Örnek bir method, butona tıklandığında çağırabilirsin
    public void showAnswerStatus(boolean status, String description){
        FrameLayout root_layout = findViewById(R.id.root_layout);
        root_layout.setVisibility(View.VISIBLE);
        root_layout.bringToFront();

        String trueStatusText = "Right Answer - Good Job!";
        String falseStatusText = "False Answer - Nice Guess";
        String questionDescriptionText = description;

        ImageView answerStatusImage = findViewById(R.id.answerStatusImage);
        ImageView answerStatusImage2 = findViewById(R.id.answerStatusImage2);

        TextView answerStatusText = findViewById(R.id.answerStatusText);
        TextView answerStatusQuestionText = findViewById(R.id.answerStatusQuestionText);
        TextView questionDescription = findViewById(R.id.questionDescription);

        if(status == true){
            answerStatusText.setText(trueStatusText);
            answerStatusImage.setImageResource(R.drawable.correct);
            answerStatusImage2.setImageResource(R.drawable.correct);
            questionDescription.setText(questionDescriptionText);
        }
        else {
            answerStatusText.setText(falseStatusText);
            answerStatusImage.setImageResource(R.drawable.wrong);
            answerStatusImage2.setImageResource(R.drawable.wrong);
            questionDescription.setText(questionDescriptionText);
        }
        answerStatusQuestionText.setText(randomQuestionCorrectAnswer);
    }
    public void showNextQuestion(View view){
        showRandomQuestion();
        FrameLayout root_layout = findViewById(R.id.root_layout);
        root_layout.setVisibility(View.INVISIBLE);
    }

    public boolean isUserAnswerCorrect(int answerIndex,int correctIndex){
        return answerIndex == correctIndex;
    }

    private JSONArray loadJsonFromAssets() {
        try {
            InputStream is = getAssets().open("questions.json"); // JSON dosya adını buraya yaz
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            return new JSONArray(json);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Set<Integer> usedQuestionIndices = new HashSet<>();

    private void showRandomQuestion() {
        JSONArray jsonArray = loadJsonFromAssets();
        if (jsonArray == null || jsonArray.length() == 0) return;

        // Tüm sorular gösterildiyse uyarı ver
        if (usedQuestionIndices.size() >= jsonArray.length()) {
            Toast.makeText(this, "Tüm sorular gösterildi!", Toast.LENGTH_SHORT).show();
            return;
        }

        int randomIndex;
        Random random = new Random();

        // Daha önce seçilmiş bir index gelirse tekrar seç
        do {
            randomIndex = random.nextInt(jsonArray.length());
        } while (usedQuestionIndices.contains(randomIndex));

        // Seçilen index'i Set'e ekle
        usedQuestionIndices.add(randomIndex);

        try {
            // Rastgele seçilen soru
            JSONObject questionObj = jsonArray.getJSONObject(randomIndex);
            String question = questionObj.getString("question");
            JSONArray answers = questionObj.getJSONArray("answers");
            correctAnswerIndex = questionObj.getInt("correct_answer_index");

            randomQuestionCorrectAnswer = answers.getString(correctAnswerIndex);

            // TextView'lere yazdır
            TextView textviewQuestion = findViewById(R.id.question);
            TextView textviewAnswer1 = findViewById(R.id.answer1);
            TextView textviewAnswer2 = findViewById(R.id.answer2);
            TextView textviewAnswer3 = findViewById(R.id.answer3);
            TextView textviewAnswer4 = findViewById(R.id.answer4);

            textviewQuestion.setText(question);
            textviewAnswer1.setText(answers.getString(0));
            textviewAnswer2.setText(answers.getString(1));
            textviewAnswer3.setText(answers.getString(2));
            textviewAnswer4.setText(answers.getString(3));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Rastgele index üreten method
    private int getRandomIndex(int maxLength) {
        Random random = new Random();
        return random.nextInt(maxLength); // 0 ile maxLength-1 arasında sayı
    }

    public void quizGameAnswer1(View view){
        userSelectedIndex = 0;
        showAnswerStatus(checkAnswerAndShowNext(),"Test Test");
    }

    public void quizGameAnswer2(View view){
        userSelectedIndex = 1;
        showAnswerStatus(checkAnswerAndShowNext(),"Test Test");
    }

    public void quizGameAnswer3(View view){
        userSelectedIndex = 2;
        showAnswerStatus(checkAnswerAndShowNext(),"Test Test");
    }

    public void quizGameAnswer4(View view){
        userSelectedIndex = 3;
        showAnswerStatus(checkAnswerAndShowNext(),"Test Test");
    }



    // Ortak metod: doğruysa yeni soruyu göster
    private boolean checkAnswerAndShowNext() {
        if(isUserAnswerCorrect(userSelectedIndex, correctAnswerIndex)){
            Toast.makeText(this, "Doğru!", Toast.LENGTH_SHORT).show();
            //showRandomQuestion(); // Yeni soruyu göster
            return true;
        } else {
            Toast.makeText(this, "Yanlış, tekrar dene!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void navHomePage(View view){
        Nav.goToHomePage(view);
    }
    public void goToEducation(View view){
        Nav.goToEducation(view);
    }
    public void btnAboutUs(View view){
        Nav.goToAboutUs(view);
    }
}