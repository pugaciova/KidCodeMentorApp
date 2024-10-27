package com.example.kidcodementorapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageView);
        TextView projectTitle = findViewById(R.id.projectTitle);
        Button startButton = findViewById(R.id.startButton);

        // Загружаем анимацию
        Animation fadeInScale = AnimationUtils.loadAnimation(this, R.anim.fade_in_scale);

        // Устанавливаем видимость и запускаем анимацию для ImageView, TextView и Button
        imageView.setVisibility(View.VISIBLE);
        projectTitle.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.VISIBLE);

        imageView.startAnimation(fadeInScale);
        projectTitle.startAnimation(fadeInScale);
        startButton.startAnimation(fadeInScale);

        // Обработчик нажатия на кнопку Start
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
