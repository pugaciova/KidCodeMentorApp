package com.example.kidcodementorapp.Models;

public class Lesson {

    private String title;
    private int progress;
    private boolean isLocked;
    private OnProgressUpdateListener progressUpdateListener;

    public interface OnProgressUpdateListener {
        void onProgressUpdated(int progress);
    }

    public Lesson(String title, int progress, boolean isLocked) {
        this.title = title;
        this.progress = progress;
        this.isLocked = isLocked;
    }

    // Геттеры и сеттеры

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (progressUpdateListener != null) {
            progressUpdateListener.onProgressUpdated(progress);  // Обновляем прогресс через слушателя
        }
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    // Добавляем метод для установки слушателя
    public void setOnProgressUpdateListener(OnProgressUpdateListener listener) {
        this.progressUpdateListener = listener;
    }
}
