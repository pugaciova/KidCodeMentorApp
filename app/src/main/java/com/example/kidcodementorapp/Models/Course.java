package com.example.kidcodementorapp.Models;

import java.util.List;

public class Course {

    private String title;    // Название курса
    private List<Lesson> lessons;  // Список уроков

    // Пустой конструктор для Firebase
    public Course() {
    }

    // Конструктор с параметрами
    public Course(String title, List<Lesson> lessons) {
        this.title = title;
        this.lessons = lessons;
    }

    // Геттеры и сеттеры
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
