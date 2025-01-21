package com.example.kidcodementorapp;

import com.example.kidcodementorapp.Models.Lesson;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RealtimeDatabaseHelper {

    private DatabaseReference database;
    private String userId;

    public RealtimeDatabaseHelper() {
        database = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Получаем UID текущего пользователя
    }

    // Метод для сохранения прогресса урока
    public void saveLessonProgress(String courseId, Lesson lesson) {
        database.child("users").child(userId).child("progress").child(courseId).child("lessons")
                .child(lesson.getTitle())
                .setValue(lesson)
                .addOnSuccessListener(aVoid -> {
                    // Успешное сохранение
                    System.out.println("Progress saved successfully!");
                })
                .addOnFailureListener(e -> {
                    // Ошибка при сохранении
                    System.err.println("Error saving progress: " + e.getMessage());
                });
    }

    // Метод для получения прогресса урока
    public void getLessonProgress(String courseId, String lessonTitle, ProgressCallback callback) {
        database.child("users").child(userId).child("progress").child(courseId).child("lessons")
                .child(lessonTitle)
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    // Если данные существуют, отправляем прогресс
                    if (dataSnapshot.exists()) {
                        Lesson lesson = dataSnapshot.getValue(Lesson.class);
                        if (lesson != null) {
                            callback.onProgressReceived(lesson.getProgress());
                        }
                    } else {
                        callback.onProgressReceived(0); // Если данных нет, возвращаем 0 прогресса
                    }
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error retrieving progress: " + e.getMessage());
                });
    }

    // Интерфейс обратного вызова для получения прогресса
    public interface ProgressCallback {
        void onProgressReceived(int progress);
    }
}
