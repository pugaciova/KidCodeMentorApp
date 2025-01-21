package com.example.kidcodementorapp;

import android.content.Intent;
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

public class BasicsInnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_basics_inn);

        // Установка отступов для системных панелей
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Проверяем данные, переданные из BasicsTrailerActivity
        boolean isLesson2Unlocked = getIntent().getBooleanExtra("lesson2_unlocked", false);
        int lesson1Progress = getIntent().getIntExtra("lesson1_progress", 0);

        if (isLesson2Unlocked) {
            // Удалить замок у второго урока
            ImageView lessonLock2 = findViewById(R.id.lesson_lock_2);
            lessonLock2.setVisibility(View.GONE);

            // Установить прогресс-бар первого урока на 100%
            ProgressBar lessonProgress1 = findViewById(R.id.lesson_progress_1);
            lessonProgress1.setProgress(lesson1Progress);

            // Обработка нажатия на lesson_icon_2
            ImageView lessonIcon2 = findViewById(R.id.lesson_icon_2);
            lessonIcon2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Запуск BasicsLesson1
                    Intent intent = new Intent(BasicsInnActivity.this, BasicsLesson1.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        }

        // Проверяем данные, переданные из BasicsLesson1
        boolean isLesson3Unlocked = getIntent().getBooleanExtra("lesson3_unlocked", false);
        int lesson2Progress = getIntent().getIntExtra("lesson2_progress", 0);

        if (isLesson3Unlocked) {
            // Удалить замок у третьего урока
            ImageView lessonLock3 = findViewById(R.id.lesson_lock_3);
            lessonLock3.setVisibility(View.GONE);

            // Установить прогресс-бар второго урока на 100%
            ProgressBar lessonProgress2 = findViewById(R.id.lesson_progress_2);
            lessonProgress2.setProgress(lesson2Progress);

            // Обработка нажатия на lesson_icon_3
            ImageView lessonIcon3 = findViewById(R.id.lesson_icon_3);
            lessonIcon3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Запуск BasicsLesson2
                    Intent intent = new Intent(BasicsInnActivity.this, BasicsLesson2.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        }









        // Обработка нажатия на lesson_icon_1
        ImageView lessonIcon1 = findViewById(R.id.lesson_icon_1);
        lessonIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Запуск BasicsTrailerActivity
                Intent intent = new Intent(BasicsInnActivity.this, BasicsTrailerActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        // Обработка нажатия на кнопку "Мой прогресс"
        Button myProgressButton = findViewById(R.id.myprogress_button);
        myProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Запуск UserProfileActivity
                Intent intent = new Intent(BasicsInnActivity.this, UserProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }
}
