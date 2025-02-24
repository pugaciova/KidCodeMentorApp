package com.example.kidcodementorapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kidcodementorapp.Models.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizActivity1 extends AppCompatActivity {

    private DatabaseReference quizRef;
    private List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private Handler handler = new Handler();

    private TextView questionText;
    private Button answerA, answerB, answerC, answerD;
    private DatabaseReference userProgressRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz1);

        questionText = findViewById(R.id.question_text);
        answerA = findViewById(R.id.answer_A);
        answerB = findViewById(R.id.answer_B);
        answerC = findViewById(R.id.answer_C);
        answerD = findViewById(R.id.answer_D);

        quizRef = FirebaseDatabase.getInstance("https://kidcodementorapp-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("courses/course_1/quiz/questions");
        userProgressRef = FirebaseDatabase.getInstance("https://kidcodementorapp-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("users/rLwcfFybwYTkfiBTsNXMdoYBTDo1/courses/course_1");

        loadQuestionsFromFirebase();
    }

    private void loadQuestionsFromFirebase() {
        quizRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                questionList.clear();
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    Question question = questionSnapshot.getValue(Question.class);
                    questionList.add(question);
                }
                if (!questionList.isEmpty()) {
                    showQuestion();
                } else {
                    Toast.makeText(QuizActivity1.this, "Нет доступных вопросов", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Quiz", "Ошибка загрузки вопросов: " + databaseError.getMessage());
            }
        });
    }

    private void showQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            Question question = questionList.get(currentQuestionIndex);
            questionText.setText(question.getText());

            Map<String, String> answers = question.getAnswers();
            answerA.setText("A: " + answers.get("A"));
            answerB.setText("B: " + answers.get("B"));
            answerC.setText("C: " + answers.get("C"));
            answerD.setText("D: " + answers.get("D"));

            resetButtonColors();

            answerA.setOnClickListener(view -> checkAnswer(answerA, "A"));
            answerB.setOnClickListener(view -> checkAnswer(answerB, "B"));
            answerC.setOnClickListener(view -> checkAnswer(answerC, "C"));
            answerD.setOnClickListener(view -> checkAnswer(answerD, "D"));
        } else {
            finishQuiz();
        }
    }

    private void checkAnswer(Button selectedButton, String selectedAnswer) {
        String correctAnswer = questionList.get(currentQuestionIndex).getCorrectAnswer();

        if (selectedAnswer.equals(correctAnswer)) {
            selectedButton.setBackgroundColor(Color.GREEN);
            Toast.makeText(this, "Отлично! Следующий вопрос.", Toast.LENGTH_SHORT).show();
            handler.postDelayed(() -> {
                currentQuestionIndex++;
                showQuestion();
            }, 1000);
        } else {
            selectedButton.setBackgroundColor(Color.RED);
            Toast.makeText(this, "Ошибка! Начинаем заново.", Toast.LENGTH_SHORT).show();
            handler.postDelayed(() -> {
                currentQuestionIndex = 0;
                showQuestion();
            }, 1000);
        }
    }

    private void resetButtonColors() {
        answerA.setBackgroundColor(Color.LTGRAY);
        answerB.setBackgroundColor(Color.LTGRAY);
        answerC.setBackgroundColor(Color.LTGRAY);
        answerD.setBackgroundColor(Color.LTGRAY);
    }

    private void finishQuiz() {
        Toast.makeText(this, "Тест завершен!", Toast.LENGTH_LONG).show();

        userProgressRef.child("lessons_completed/lesson_5").setValue(true);
        userProgressRef.child("progress").setValue(100);

        Intent intent = new Intent(QuizActivity1.this, BasicsInnActivity.class);
        startActivity(intent);
        finish();
    }
}