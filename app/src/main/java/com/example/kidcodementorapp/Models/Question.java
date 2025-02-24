package com.example.kidcodementorapp.Models;

import java.util.Map;

public class Question {
    private String text;
    private Map<String, String> answers;
    private String correctAnswer;

    public Question() {} // Обязательный пустой конструктор для Firebase

    public String getText() { return text; }
    public Map<String, String> getAnswers() { return answers; }
    public String getCorrectAnswer() { return correctAnswer; }
}