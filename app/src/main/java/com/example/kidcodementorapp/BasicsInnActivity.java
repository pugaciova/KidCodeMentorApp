package com.example.kidcodementorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class BasicsInnActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_basics_inn);

        // Инициализация SharedPreferences
        sharedPreferences = getSharedPreferences("LessonsProgress", MODE_PRIVATE);

        // Установка отступов для системных панелей
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Загрузка состояния уроков
        loadLessonState();

        // Обработка нажатия на уроки
        setupLessonClickListeners();

        // Обработка нажатия на кнопку "Мой прогресс"
        Button myProgressButton = findViewById(R.id.myprogress_button);
        myProgressButton.setOnClickListener(v -> {
            // Запуск UserProfileActivity
            Intent intent = new Intent(BasicsInnActivity.this, UserProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }


    private void loadLessonState() {
        // Урок Trailer
        int trailerProgress = sharedPreferences.getInt("lesson_trailer_progress", 0);
        ProgressBar trailerProgressBar = findViewById(R.id.lesson_progress_1);
        trailerProgressBar.setProgress(trailerProgress);

        // Урок 1
        int lesson1Progress = sharedPreferences.getInt("lesson_1_progress", 0);
        boolean isLesson1Unlocked = sharedPreferences.getBoolean("lesson_1_unlocked", false);
        ProgressBar lesson1ProgressBar = findViewById(R.id.lesson_progress_2);
        ImageView lesson1Lock = findViewById(R.id.lesson_lock_2);

        lesson1ProgressBar.setProgress(lesson1Progress);
        lesson1Lock.setVisibility(isLesson1Unlocked ? View.GONE : View.VISIBLE);

        // Урок 2
        int lesson2Progress = sharedPreferences.getInt("lesson_2_progress", 0);
        boolean isLesson2Unlocked = sharedPreferences.getBoolean("lesson_2_unlocked", false);
        ProgressBar lesson2ProgressBar = findViewById(R.id.lesson_progress_3);
        ImageView lesson2Lock = findViewById(R.id.lesson_lock_3);

        lesson2ProgressBar.setProgress(lesson2Progress);
        lesson2Lock.setVisibility(isLesson2Unlocked ? View.GONE : View.VISIBLE);

        // Повторите аналогично для других уроков...
    }

    private void setupLessonClickListeners() {
        // Обработка нажатия на урок Trailer
        ImageView lessonIconTrailer = findViewById(R.id.lesson_icon_1);
        lessonIconTrailer.setOnClickListener(v -> {
            Intent intent = new Intent(BasicsInnActivity.this, BasicsTrailerActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Обработка нажатия на урок 1
        ImageView lessonIcon1 = findViewById(R.id.lesson_icon_2);
        lessonIcon1.setOnClickListener(v -> {
            if (sharedPreferences.getBoolean("lesson_1_unlocked", false)) {
                Intent intent = new Intent(BasicsInnActivity.this, BasicsLesson1.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        // Обработка нажатия на урок 2
        ImageView lessonIcon2 = findViewById(R.id.lesson_icon_3);
        lessonIcon2.setOnClickListener(v -> {
            if (sharedPreferences.getBoolean("lesson_2_unlocked", false)) {
                Intent intent = new Intent(BasicsInnActivity.this, BasicsLesson2.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        // Добавьте обработчики для других уроков...
    }

    //Код для получения текущего UID
    private String getCurrentUserUid() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser().getUid();
        } else {
            // Пользователь не авторизован
            return null;
        }
    }

    public static void updateLessonState(SharedPreferences sharedPreferences, String lessonKey, int progress, String nextLessonKey) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(lessonKey + "_progress", progress);
        if (nextLessonKey != null) {
            editor.putBoolean(nextLessonKey + "_unlocked", true);
        }
        editor.apply();
    }
}
