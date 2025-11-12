package com.example.bluesteps;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
    String randomQuestionExplanation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_quiz);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        showRandomQuestion();
    }

    public void showAnswerStatus(boolean status, String description){
        LinearLayout answer1 = findViewById(R.id.quizGameAnswer1);
        LinearLayout answer2 = findViewById(R.id.quizGameAnswer2);
        LinearLayout answer3 = findViewById(R.id.quizGameAnswer3);
        LinearLayout answer4 = findViewById(R.id.quizGameAnswer4);

        answer1.setClickable(false);
        answer2.setClickable(false);
        answer3.setClickable(false);
        answer4.setClickable(false);


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

        LinearLayout correctAnswerCard = findViewById(R.id.correctAnswerCard);

        if(status == true){
            correctAnswerCard.setBackgroundResource(R.drawable.quiz_correct_answer_card);
            answerStatusText.setText(trueStatusText);
            answerStatusImage.setImageResource(R.drawable.correct);
            answerStatusImage2.setImageResource(R.drawable.correct);
            questionDescription.setText(questionDescriptionText);
        }
        else {
            correctAnswerCard.setBackgroundResource(R.drawable.quiz_correct_answer_card);
            answerStatusText.setText(falseStatusText);
            answerStatusImage.setImageResource(R.drawable.wrong);
            answerStatusImage2.setImageResource(R.drawable.correct);
            questionDescription.setText(questionDescriptionText);
        }
        answerStatusQuestionText.setText(randomQuestionCorrectAnswer);
    }
    public void showNextQuestion(View view){
        LinearLayout answer1 = findViewById(R.id.quizGameAnswer1);
        LinearLayout answer2 = findViewById(R.id.quizGameAnswer2);
        LinearLayout answer3 = findViewById(R.id.quizGameAnswer3);
        LinearLayout answer4 = findViewById(R.id.quizGameAnswer4);

        answer1.setClickable(true);
        answer2.setClickable(true);
        answer3.setClickable(true);
        answer4.setClickable(true);
        showRandomQuestion();
        FrameLayout root_layout = findViewById(R.id.root_layout);
        root_layout.setVisibility(View.INVISIBLE);
    }

    public boolean isUserAnswerCorrect(int answerIndex,int correctIndex){
        return answerIndex == correctIndex;
    }

    private JSONArray loadJsonFromAssets() {
        try {
            InputStream is = getAssets().open("questions.json");
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

        if (usedQuestionIndices.size() >= jsonArray.length()) {
            Toast.makeText(this, "All questions have been shown", Toast.LENGTH_SHORT).show();
            return;
        }

        int randomIndex;
        Random random = new Random();

        do {
            randomIndex = random.nextInt(jsonArray.length());
        } while (usedQuestionIndices.contains(randomIndex));

        usedQuestionIndices.add(randomIndex);

        try {
            JSONObject questionObj = jsonArray.getJSONObject(randomIndex);
            String question = questionObj.getString("question");
            JSONArray answers = questionObj.getJSONArray("answers");
            correctAnswerIndex = questionObj.getInt("correct_answer_index");

            randomQuestionExplanation = questionObj.optString("explanation", "No detailed explanation available."); // GÜNCELLENEN KISIM

            randomQuestionCorrectAnswer = answers.getString(correctAnswerIndex);

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

    private int getRandomIndex(int maxLength) {
        Random random = new Random();
        return random.nextInt(maxLength);
    }

    public void quizGameAnswer1(View view){
        userSelectedIndex = 0;
        showAnswerStatus(checkAnswerAndShowNext(), randomQuestionExplanation); // GÜNCELLENEN KISIM
    }

    public void quizGameAnswer2(View view){
        userSelectedIndex = 1;
        showAnswerStatus(checkAnswerAndShowNext(), randomQuestionExplanation); // GÜNCELLENEN KISIM
    }

    public void quizGameAnswer3(View view){
        userSelectedIndex = 2;
        showAnswerStatus(checkAnswerAndShowNext(), randomQuestionExplanation); // GÜNCELLENEN KISIM
    }

    public void quizGameAnswer4(View view){
        userSelectedIndex = 3;
        showAnswerStatus(checkAnswerAndShowNext(), randomQuestionExplanation); // GÜNCELLENEN KISIM
    }


    private boolean checkAnswerAndShowNext() {
        if(isUserAnswerCorrect(userSelectedIndex, correctAnswerIndex)){
            //Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            //Toast.makeText(this, "Wrong Try Again!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void navHomePage(View view){
        Nav.goToHomePage(view);
    }
    public void navQuiz(View view){
        if(this.getClass() != GameQuiz.class){
            Nav.goToQuizPage(view);
        }
    }
    public void goToEducation(View view){
        Nav.goToEducation(view);
    }
    public void btnAboutUs(View view){
        Nav.goToAboutUs(view);
    }
}