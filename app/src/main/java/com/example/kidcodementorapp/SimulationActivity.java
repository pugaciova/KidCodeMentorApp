package com.example.kidcodementorapp;

import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SimulationActivity extends AppCompatActivity {
    private TextToSpeech textToSpeech;
    private VideoView studentVideo;
    private Button option1, option2, option3, option4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation1);

        studentVideo = findViewById(R.id.student_video);
        option1 = findViewById(R.id.option_1);
        option2 = findViewById(R.id.option_2);
        option3 = findViewById(R.id.option_3);
        option4 = findViewById(R.id.option_4);

        // Настройка VideoView для показа видео
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.student_video);
        studentVideo.setVideoURI(videoUri);
        studentVideo.start();

        // Запуск синтеза речи одновременно с видео
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US);
                speakText("I have finished all tasks. What should I do next?");
            }
        });

        View.OnClickListener optionClickListener = v -> {
            Button clickedButton = (Button) v;
            String response = getResponse(clickedButton.getText().toString());
            speakText(response);
        };

        option1.setOnClickListener(optionClickListener);
        option2.setOnClickListener(optionClickListener);
        option3.setOnClickListener(optionClickListener);
        option4.setOnClickListener(optionClickListener);
    }

    private void speakText(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private String getResponse(String selectedOption) {
        switch (selectedOption) {
            case "Check additional materials":
                return "Thank you! I will check the additional materials.";
            case "Help your classmate":
                return "Alright! I will try to help my classmate.";
            case "Stay quiet for now":
                return "Hmm... okay...";
            case "Start the next assignment":
                return "Great! I will start the next assignment!";
            default:
                return "I don't understand.";
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
