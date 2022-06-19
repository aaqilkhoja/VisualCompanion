package com.example.ocrtexttospeech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ocrtexttospeech.objectDetection.DetectorActivity;
import com.example.ocrtexttospeech.welcome.WelcomePage;

import java.util.Locale;


public class FrontPage extends AppCompatActivity {

    private Button currency_button, ocr_button, object_button, fourth_button;
    private TextToSpeech tts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frontpage);

        // Defining the UI objects
        currency_button = findViewById(R.id.currency_detection_button);
        ocr_button = findViewById(R.id.text_to_speech_button);
        object_button = findViewById(R.id.object_detection_button);
        fourth_button = findViewById(R.id.misc_button);


        // Creating the local database access
        SharedPreferences mPrefs = this.getPreferences(MODE_PRIVATE);
        int defaultValue = 0;
        int introPage = mPrefs.getInt("IntroPage", defaultValue);

        // If user launches the app for the first time,
        // it will show the introductory page
        if (introPage == 0) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putInt("IntroPage", 1);
            editor.apply();

            startActivity(new Intent(FrontPage.this, WelcomePage.class));
        }

        // Initializing the text to speech library
        TextToSpeech.OnInitListener listener =
                status -> {
                    if (status == TextToSpeech.SUCCESS) {
                        Log.d("TTS", "Text to Speech Engine started successfully.");
                        tts.setLanguage(Locale.US);
                    } else {
                        Log.d("TTS", "Error starting text to speech engine.");
                    }
                };
        tts = new TextToSpeech(this.getApplicationContext(), listener);

        currency_button.setOnLongClickListener(v -> {
            tts.speak("Currency Detection", TextToSpeech.QUEUE_FLUSH, null, null);
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            startActivity(new Intent(FrontPage.this, CurrencyDetectionTwo.class));
            return true;
        });

        ocr_button.setOnLongClickListener(v -> {
            vibrateNow(1000);
            tts.speak("OCR", TextToSpeech.QUEUE_FLUSH, null, null);
            Intent intent = new Intent(FrontPage.this, MainActivity.class);
            startActivity(intent);
            return true;
        });


        object_button.setOnLongClickListener(v -> {
            vibrateNow(1000);
            tts.speak("Object detection", TextToSpeech.QUEUE_FLUSH, null, null);
            startActivity(new Intent(FrontPage.this, DetectorActivity.class));
            return true;
        });


        fourth_button.setOnLongClickListener(v -> {
            vibrateNow(1000);
            tts.speak("Hear the Introduction Again", TextToSpeech.QUEUE_FLUSH, null, null);
            startActivity(new Intent(FrontPage.this, WelcomePage.class));
            return true;
        });
    }

    // Function to provide haptic feedback
    public void vibrateNow(long millis) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(millis);
        }
    }
}
