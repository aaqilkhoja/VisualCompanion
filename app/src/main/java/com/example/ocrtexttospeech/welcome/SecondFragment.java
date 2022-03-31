package com.example.ocrtexttospeech.welcome;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ocrtexttospeech.R;

import java.util.Locale;


public class SecondFragment extends Fragment {


    public SecondFragment() {
        // Required empty public constructor
    }

    TextToSpeech tts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        tts = new TextToSpeech(getActivity(), listener);

        tts.speak("Welcome to the Visual Companion app!", TextToSpeech.QUEUE_ADD, null, null);
    }


    @Override
    public void onResume() {
        super.onResume();

        if(this.isVisible())
            tts.speak("Welcome to the Visual Companion app!", TextToSpeech.QUEUE_ADD, null, null);

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater
                .inflate(
                        R.layout.fragment_second,
                        container, false);
    }
}