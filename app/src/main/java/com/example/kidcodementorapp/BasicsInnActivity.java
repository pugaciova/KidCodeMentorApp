package com.example.kidcodementorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BasicsInnActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_basics_inn);

        // Инициализация FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Инициализация SharedPreferences
        sharedPreferences = getSharedPreferences("LessonsProgress", MODE_PRIVATE);

        // Проверяем, сменился ли пользователь
        checkUserChange();

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

    @Override
    protected void onResume() {
        super.onResume();
        // Синхронизация прогресса из Firebase при каждом открытии Activity
        syncProgressFromFirebase();
    }

    private void syncProgressFromFirebase() {
        String userId = getCurrentUserUid();
        if (userId == null) {
            Log.d("FirebaseSync", "Пользователь не авторизован, загрузка прогресса прервана.");
            return;
        }

        DatabaseReference database = FirebaseDatabase.getInstance("https://kidcodementorapp-default-rtdb.europe-west1.firebasedatabase.app").getReference("users")
                .child(userId).child("courses").child("course_1");

        Log.d("FirebaseSync", "Запрос данных из Firebase для userId: " + userId);

        database.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot snapshot = task.getResult();
                Log.d("FirebaseSync", "Данные успешно получены из Firebase: " + snapshot.getValue());

                boolean lesson0Completed = snapshot.child("lessons_completed/lesson_0").getValue(Boolean.class) != null &&
                        snapshot.child("lessons_completed/lesson_0").getValue(Boolean.class);
                boolean lesson1Completed = snapshot.child("lessons_completed/lesson_1").getValue(Boolean.class) != null &&
                        snapshot.child("lessons_completed/lesson_1").getValue(Boolean.class);
                boolean lesson2Completed = snapshot.child("lessons_completed/lesson_2").getValue(Boolean.class) != null &&
                        snapshot.child("lessons_completed/lesson_2").getValue(Boolean.class);
                boolean lesson3Completed = snapshot.child("lessons_completed/lesson_3").getValue(Boolean.class) != null &&
                        snapshot.child("lessons_completed/lesson_3").getValue(Boolean.class);
                boolean lesson4Completed = snapshot.child("lessons_completed/lesson_4").getValue(Boolean.class) != null &&
                        snapshot.child("lessons_completed/lesson_4").getValue(Boolean.class);
                boolean lesson5Completed = snapshot.child("lessons_completed/lesson_5").getValue(Boolean.class) != null &&
                        snapshot.child("lessons_completed/lesson_5").getValue(Boolean.class);

                int progress = snapshot.child("progress").getValue(Integer.class) != null ?
                        snapshot.child("progress").getValue(Integer.class) : 0;

                Log.d("FirebaseSync", "lesson_0_completed: " + lesson0Completed);
                Log.d("FirebaseSync", "lesson_1_completed: " + lesson1Completed);
                Log.d("FirebaseSync", "lesson_2_completed: " + lesson2Completed);
                Log.d("FirebaseSync", "lesson_3_completed: " + lesson3Completed);
                Log.d("FirebaseSync", "lesson_4_completed: " + lesson4Completed);
                Log.d("FirebaseSync", "lesson_5_completed: " + lesson4Completed);
                Log.d("FirebaseSync", "Общий прогресс: " + progress);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("lesson_1_unlocked", lesson0Completed);
                editor.putBoolean("lesson_2_unlocked", lesson1Completed);
                editor.putBoolean("lesson_3_unlocked", lesson2Completed);
                editor.putBoolean("lesson_4_unlocked", lesson3Completed);
                editor.putBoolean("lesson_5_unlocked", lesson3Completed);
                editor.putInt("progress", progress);

                // Обновляем прогресс Trailer (100% если завершен)
                editor.putInt("lesson_trailer_progress", lesson0Completed ? 100 : 0);
                editor.apply();

                Log.d("FirebaseSync", "Данные сохранены в SharedPreferences.");

                // После загрузки обновляем UI
                loadLessonState();
            } else {
                Log.e("FirebaseSync", "Ошибка загрузки данных: ", task.getException());
            }
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

        // Урок 3
        int lesson3Progress = sharedPreferences.getInt("lesson_3_progress", 0);
        boolean isLesson3Unlocked = sharedPreferences.getBoolean("lesson_3_unlocked", false);
        ProgressBar lesson3ProgressBar = findViewById(R.id.lesson_progress_4);
        ImageView lesson3Lock = findViewById(R.id.lesson_lock_4);
        lesson3ProgressBar.setProgress(lesson3Progress);
        lesson3Lock.setVisibility(isLesson3Unlocked ? View.GONE : View.VISIBLE);

        // Урок 4
        int lesson4Progress = sharedPreferences.getInt("lesson_4_progress", 0);
        boolean isLesson4Unlocked = sharedPreferences.getBoolean("lesson_4_unlocked", false);
        ProgressBar lesson4ProgressBar = findViewById(R.id.lesson_progress_5);
        ImageView lesson4Lock = findViewById(R.id.lesson_lock_5);
        lesson4ProgressBar.setProgress(lesson4Progress);
        lesson4Lock.setVisibility(isLesson4Unlocked ? View.GONE : View.VISIBLE);

        // Добавьте аналогично для других уроков...

        // Тест
        int lesson5Progress = sharedPreferences.getInt("lesson_5_progress", 0);
        boolean isLesson5Unlocked = sharedPreferences.getBoolean("lesson_5_unlocked", false);
        ProgressBar lesson5ProgressBar = findViewById(R.id.lesson_progress_6);
        ImageView lesson5Lock = findViewById(R.id.lesson_lock_6);
        lesson5ProgressBar.setProgress(lesson5Progress);
        lesson5Lock.setVisibility(isLesson5Unlocked ? View.GONE : View.VISIBLE);

        // Добавьте аналогично для других уроков...
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

        // Обработка нажатия на урок 3
        ImageView lessonIcon3 = findViewById(R.id.lesson_icon_4);
        lessonIcon3.setOnClickListener(v -> {
            if (sharedPreferences.getBoolean("lesson_3_unlocked", false)) {
                Intent intent = new Intent(BasicsInnActivity.this, BasicsLesson3.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        // Обработка нажатия на урок 4
        ImageView lessonIcon4 = findViewById(R.id.lesson_icon_5);
        lessonIcon4.setOnClickListener(v -> {
            if (sharedPreferences.getBoolean("lesson_4_unlocked", false)) {
                Intent intent = new Intent(BasicsInnActivity.this, BasicsLesson4.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        // Обработка нажатия на урок 5 (Quiz)
        ImageView lessonIcon5 = findViewById(R.id.lesson_icon_6);
        lessonIcon5.setOnClickListener(v -> {
            if (sharedPreferences.getBoolean("lesson_5_unlocked", false)) {
                Intent intent = new Intent(BasicsInnActivity.this, QuizActivity1.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    // Получение UID текущего пользователя
    private String getCurrentUserUid() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            Log.d("Firebase", "Текущий userId: " + userId);
            return userId;
        } else {
            Log.d("Firebase", "Пользователь не авторизован.");
            return null;
        }
    }


    // Статический метод для обновления состояния урока
    public static void updateLessonState(SharedPreferences sharedPreferences, String lessonKey, int progress, String nextLessonKey) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(lessonKey + "_progress", progress);
        if (nextLessonKey != null) {
            editor.putBoolean(nextLessonKey + "_unlocked", true);
        }
        editor.apply();
    }
    private void markLessonAsCompleted(String lessonId) {
        String userId = getCurrentUserUid();
        if (userId == null) return;

        DatabaseReference database = FirebaseDatabase.getInstance("https://kidcodementorapp-default-rtdb.europe-west1.firebasedatabase.app").getReference("users")
                .child(userId).child("courses").child("course_1").child("lessons_completed");

        database.child(lessonId).setValue(true)
                .addOnSuccessListener(aVoid -> {
                    // Обновляем локальное хранилище
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(lessonId + "_unlocked", true);

                    // Если это Trailer, ставим progress 100%
                    if ("lesson_0".equals(lessonId)) {
                        editor.putInt("lesson_trailer_progress", 100);
                    }

                    editor.apply();

                    // Обновляем UI
                    loadLessonState();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Ошибка обновления данных: " + e.getMessage());
                });
    }


    private void clearLocalProgress() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Очищаем все сохраненные данные
        editor.apply();
    }

    private void checkUserChange() {
        String currentUserId = getCurrentUserUid();
        String savedUserId = sharedPreferences.getString("last_user_id", "");

        if (!currentUserId.equals(savedUserId)) {
            clearLocalProgress(); // Очищаем локальные данные перед загрузкой новых
            syncProgressFromFirebase(); // Загружаем актуальный прогресс
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("last_user_id", currentUserId);
            editor.apply();
        }
    }




}