package com.example.kidcodementorapp.Models;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private String email;
    private String pass;
    private Map<String, Course> userProgress; // Прогресс пользователя по курсам (название курса -> объект Course)

    public User() {
        // Инициализация прогресса пустым значением
        this.userProgress = new HashMap<>();
    }

    public User(String name, String email, String pass) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.userProgress = new HashMap<>();
    }

    // Геттеры и сеттеры для name, email, pass
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    // Геттер и сеттер для userProgress
    public Map<String, Course> getUserProgress() {
        return userProgress;
    }

    public void setUserProgress(Map<String, Course> userProgress) {
        this.userProgress = userProgress;
    }
}
