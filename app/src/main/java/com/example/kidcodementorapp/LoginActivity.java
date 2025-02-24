package com.example.kidcodementorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    private EditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        loginButton.setOnClickListener(view -> workLoginWindow());

        TextView signupText = findViewById(R.id.signupText);
        signupText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void workLoginWindow() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Введите email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Пароль должен содержать не менее 6 символов", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    // После успешного входа синхронизируем прогресс из Firebase
                    syncProgressFromFirebase();
                    startActivity(new Intent(LoginActivity.this, AboutActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Ошибка авторизации: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void syncProgressFromFirebase() {
        String userId = auth.getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(userId).child("courses").child("course_1")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        // Загружаем прогресс из Firebase и сохраняем в SharedPreferences
                        boolean lesson1Completed = task.getResult()
                                .child("lessons_completed")
                                .child("lesson_1")
                                .getValue(Boolean.class) != null;
                        int courseProgress = task.getResult()
                                .child("progress")
                                .getValue(Integer.class);

                        SharedPreferences sharedPreferences = getSharedPreferences("LessonsProgress", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("lesson_1_unlocked", lesson1Completed);
                        editor.putInt("lesson_1_progress", lesson1Completed ? 100 : 0);
                        editor.apply();
                    } else {
                        System.out.println("Ошибка при загрузке данных из Firebase: " + task.getException());
                    }
                });
    }
}