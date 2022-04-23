package com.example.ocrtexttospeech.welcome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ocrtexttospeech.R;

import java.util.Locale;

public class WelcomePage extends AppCompatActivity {

    TextToSpeech tts;


    @Override
    protected void onCreate(
            Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Set the content of the activity
        // to use the activity_main.xml
        // layout file
        setContentView(R.layout.activity_welcome_page);

        // Find the view pager that will
        // allow the user to swipe
        // between fragments
        //ViewPager viewPager = findViewById(R.id.viewpager);

        ViewPager2 viewPager = findViewById(R.id.viewPager2);

        // Create an adapter that
        // knows which fragment should
        // be shown on each page

        FragmentPageAdapter stateAdapter = new FragmentPageAdapter(getSupportFragmentManager(), getLifecycle());

        stateAdapter.addFragment(new FirstFragment());
        stateAdapter.addFragment(new SecondFragment());
        stateAdapter.addFragment(new ThirdFragment());

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("IntroPage", 1);
        editor.apply();

        int introPage = sharedPref.getInt("IntroPage", 0);

        Log.d("INTRO PAGE LOG 2", "Intro Page: " + introPage);



        ///

        TextToSpeech.OnInitListener listener =
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(final int status) {
                        if(status==TextToSpeech.SUCCESS) {
                            Log.d("TTS Intro Page", "Text to Speech Engine started successfully.");
                            tts.setLanguage(Locale.US);
                        }else{
                            Log.d("TTS Intro Page", "Error starting text to speech engine.");
                        }
                    }
                };

        tts = new TextToSpeech(this, listener);


        //tts.speak("Welcome to the Visual Companion app!", TextToSpeech.QUEUE_ADD, null, null);



        // Set the adapter onto
        // the view pager
        viewPager.setAdapter(stateAdapter);
    }
}