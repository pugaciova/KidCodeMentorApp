package com.example.kidcodementorapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidcodementorapp.Models.Lesson;
import com.example.kidcodementorapp.R;
import com.example.kidcodementorapp.RealtimeDatabaseHelper;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LessonAdapter {

    private final Activity activity;
    private final List<Lesson> lessons;
    private final RealtimeDatabaseHelper databaseHelper;

    public LessonAdapter(Activity activity, List<Lesson> lessons) {
        this.activity = activity;
        this.lessons = lessons;
        this.databaseHelper = new RealtimeDatabaseHelper();  // Инициализация помощника для работы с Realtime Database
        initializeLessons();
    }

    private void initializeLessons() {
        for (int i = 0; i < lessons.size(); i++) {
            int currentIndex = i; // Локальная копия индекса
            Lesson lesson = lessons.get(currentIndex);

            // Получение id для урока, прогресса и замка
            int lessonItemId = activity.getResources().getIdentifier("lesson_item_" + (currentIndex + 1), "id", activity.getPackageName());
            int progressBarId = activity.getResources().getIdentifier("lesson_progress_" + (currentIndex + 1), "id", activity.getPackageName());
            int lockId = activity.getResources().getIdentifier("lesson_lock_" + (currentIndex + 1), "id", activity.getPackageName());

            View lessonView = activity.findViewById(lessonItemId);
            ProgressBar progressBar = activity.findViewById(progressBarId);
            ImageView lockIcon = activity.findViewById(lockId);

            if (lessonView != null && progressBar != null) {
                // Установка данных урока
                TextView titleView = lessonView.findViewById(R.id.lesson_title_1);
                TextView durationView = lessonView.findViewById(R.id.lesson_duration_1);

                if (titleView != null) titleView.setText(lesson.getTitle());

                // Загрузка прогресса из базы данных
                databaseHelper.getLessonProgress("courseId", lesson.getTitle(), progress -> {
                    progressBar.setProgress(progress);
                    lesson.setProgress(progress);  // Обновление прогресса урока
                    if (progress == 100) {
                        unlockNextLesson(currentIndex + 1); // Используем локальную копию
                    }
                });

                // Обработка доступности урока
                if (lesson.isLocked()) {
                    lockIcon.setVisibility(View.VISIBLE);
                    lessonView.setAlpha(0.5f); // Затемняем заблокированные уроки
                    lessonView.setEnabled(false);
                } else {
                    lockIcon.setVisibility(View.GONE);
                    lessonView.setAlpha(1f);
                    lessonView.setEnabled(true);
                }

                // Клик для открытия урока
                lessonView.setOnClickListener(v -> {
                    if (!lesson.isLocked()) {
                        // Логика открытия урока
                    }
                });

                // Слушатель прогресса
                lesson.setOnProgressUpdateListener(progress -> {
                    progressBar.setProgress(progress);
                    databaseHelper.saveLessonProgress("courseId", lesson); // Обновление прогресса в базе данных

                    if (progress == 100) {
                        unlockNextLesson(currentIndex + 1); // Используем локальную копию
                    }
                });
            }
        }
    }

    private void unlockNextLesson(int index) {
        if (index < lessons.size()) {
            Lesson nextLesson = lessons.get(index);
            if (nextLesson.isLocked()) {
                nextLesson.setLocked(false);

                // Обновление интерфейса для следующего урока
                int lessonItemId = activity.getResources().getIdentifier("lesson_item_" + (index + 1), "id", activity.getPackageName());
                int lockId = activity.getResources().getIdentifier("lesson_lock_" + (index + 1), "id", activity.getPackageName());

                View lessonView = activity.findViewById(lessonItemId);
                ImageView lockIcon = activity.findViewById(lockId);

                if (lessonView != null && lockIcon != null) {
                    lockIcon.setVisibility(View.GONE);
                    lessonView.setAlpha(1f);
                    lessonView.setEnabled(true);
                }

                // Обновление состояния урока в базе данных
                nextLesson.setLocked(false);
                databaseHelper.saveLessonProgress("courseId", nextLesson);
            }
        }
    }
}
