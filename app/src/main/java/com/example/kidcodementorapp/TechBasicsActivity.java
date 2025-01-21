package com.example.kidcodementorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TechBasicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tech_basics);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Найти кнопку play
        ImageView playButton = findViewById(R.id.play_button);

        // Загрузить анимацию
        Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink_animation);

        // Применить анимацию
        playButton.startAnimation(blinkAnimation);

        // Установить обработчик клика на кнопку play
        playButton.setOnClickListener(v -> {
            // Создаем Intent для перехода на Activity BasicsInnActivity
            Intent intent = new Intent(TechBasicsActivity.this, BasicsInnActivity.class);
            startActivity(intent);
        });
    }
}
