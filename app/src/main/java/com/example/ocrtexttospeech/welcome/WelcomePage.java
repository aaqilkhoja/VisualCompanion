package com.example.ocrtexttospeech.welcome;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ocrtexttospeech.R;

import java.util.Locale;

public class WelcomePage extends AppCompatActivity {

    private TextToSpeech tts;

    @Override
    protected void onCreate(
            Bundle savedInstanceState) {


        //Initializing text to speech
        TextToSpeech.OnInitListener listener =
                status -> {
                    if (status == TextToSpeech.SUCCESS) {
                        Log.d("TTS Intro Page", "Text to Speech Engine started successfully.");
                        tts.setLanguage(Locale.US);
                    } else {
                        Log.d("TTS Intro Page", "Error starting text to speech engine.");
                    }
                };

        tts = new TextToSpeech(this, listener);
        super.onCreate(savedInstanceState);

        // Set the content of the activity
        // layout file
        setContentView(R.layout.activity_welcome_page);
        ViewPager2 viewPager = findViewById(R.id.viewPager2);

        // Create an adapter that
        // knows which fragment should
        // be shown on each page
        FragmentPageAdapter stateAdapter = new FragmentPageAdapter(getSupportFragmentManager(), getLifecycle());

        stateAdapter.addFragment(new FirstFragment());
        stateAdapter.addFragment(new SecondFragment());
        stateAdapter.addFragment(new ThirdFragment());
        stateAdapter.addFragment(new FourthFragment());
        stateAdapter.addFragment(new FifthFragment());
        stateAdapter.addFragment(new LastFragment());

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("IntroPage", 1);
        editor.apply();

        listener = status -> {
            if (status == TextToSpeech.SUCCESS) {
                Log.d("TTS Intro Page", "Text to Speech Engine started successfully.");
                tts.setLanguage(Locale.US);
            } else {
                Log.d("TTS Intro Page", "Error starting text to speech engine.");
            }
        };
        tts = new TextToSpeech(this, listener);

        // Set the adapter onto
        // the view pager
        viewPager.setAdapter(stateAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    public void vibratePulse() {
        long[] pattern = {0, 100, 200, 100, 200, 100};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(pattern, -1);
        }
    }

    public void vibrateNow(long millis) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(millis);
        }
    }

    @Override
    public void onBackPressed() {
        tts.stop();
        vibratePulse();
        tts.speak("Back to the Front Page", TextToSpeech.QUEUE_FLUSH, null, null);
        this.finish();
    }
}