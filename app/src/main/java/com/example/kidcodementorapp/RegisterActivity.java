package com.example.kidcodementorapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kidcodementorapp.Models.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnSuccessListener;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton;
    private EditText nameInput, emailInput, passwordInput, confirmInput;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference users;
    private TextView signupText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Инициализация полей ввода и кнопки
        registerButton = findViewById(R.id.registerButton);
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmInput = findViewById(R.id.confirmInput);
        signupText = findViewById(R.id.signupText);

        // Инициализация Firebase Auth и Database
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        // Set up login link click listener to return to LoginActivity
        signupText.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finish RegisterActivity so the user can't go back to it
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workRegisterWindow();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    private void workRegisterWindow() {
        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmInput.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show();
            return;
        }
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
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }

        // Изменяем цвет кнопки на время регистрации и отключаем её
        registerButton.setBackgroundColor(getResources().getColor(R.color.button_pressed_color));
        registerButton.setEnabled(false);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(RegisterActivity.this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                    // Создаем объект пользователя для базы данных
                    User user = new User();
                    user.setEmail(email);
                    user.setName(name);
                    user.setPass(password);

                    // Запись пользователя в Firebase Realtime Database
                    users.child(user.getEmail().replace(".", ","))
                            .setValue(user)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Пользователь добавлен в базу данных!", Toast.LENGTH_SHORT).show();

                                    // Восстанавливаем исходный цвет кнопки и делаем её активной
                                    registerButton.setBackgroundColor(getResources().getColor(R.color.button_default_color));
                                    registerButton.setEnabled(true);

                                    // Переход на LoginActivity
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Ошибка при записи в базу данных
                                    Toast.makeText(RegisterActivity.this, "Ошибка записи в базу данных: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    registerButton.setBackgroundColor(getResources().getColor(R.color.button_default_color));
                                    registerButton.setEnabled(true);
                                }
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Ошибка регистрации: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    registerButton.setBackgroundColor(getResources().getColor(R.color.button_default_color));
                    registerButton.setEnabled(true);
                });
    }



}
