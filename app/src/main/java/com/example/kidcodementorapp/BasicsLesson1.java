package com.example.kidcodementorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
            // Обновить прогресс текущего урока и разблокировать следующий
            BasicsInnActivity.updateLessonState(sharedPreferences, "lesson_1", 100, "lesson_2");

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
}
