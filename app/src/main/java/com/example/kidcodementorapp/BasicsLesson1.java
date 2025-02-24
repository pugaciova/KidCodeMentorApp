package com.example.kidcodementorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BasicsLesson1 extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basics_lesson1);

        // Инициализация SharedPreferences
        sharedPreferences = getSharedPreferences("LessonsProgress", MODE_PRIVATE);

        // Найти кнопку завершения
        Button completeLessonButton = findViewById(R.id.stop_lesson);

        // Установить слушатель нажатий
        completeLessonButton.setOnClickListener(view -> {
            // Обновить прогресс урока и разблокировать следующий
            BasicsInnActivity.updateLessonState(sharedPreferences, "lesson_1", 100, "lesson_2");

            Log.d("ButtonClick", "Кнопка завершения нажата");

            // Сохранение прогресса в Firebase
            saveLessonProgressToFirebase();

            // Вернуться к странице со списком уроков
            Intent intent = new Intent(BasicsLesson1.this, BasicsInnActivity.class);
            startActivity(intent);

            // Завершить текущую активность
            finish();
        });

        // Обработка системных отступов
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Метод для сохранения прогресса урока в Firebase
    private void saveLessonProgressToFirebase() {
        String userId = getCurrentUserUid();
        if (userId == null) return;

        DatabaseReference database = FirebaseDatabase.getInstance("https://kidcodementorapp-default-rtdb.europe-west1.firebasedatabase.app").getReference();

        // Сохраняем прогресс
        database.child("users").child(userId).child("courses").child("course_1")
                .child("lessons_completed").child("lesson_1").setValue(true)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Данные успешно записаны"))
                .addOnFailureListener(e -> Log.e("Firebase", "Ошибка записи данных: " + e.getMessage()));

        // Обновляем прогресс курса
        database.child("users").child(userId).child("courses").child("course_1")
                .child("progress").setValue(28)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Прогресс курса успешно обновлен"))
                .addOnFailureListener(e -> Log.e("Firebase", "Ошибка обновления прогресса: " + e.getMessage()));
    }



    // Метод для получения UID текущего пользователя
    private String getCurrentUserUid() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser().getUid();
        } else {
            // Пользователь не авторизован
            return null;
        }
    }

}