package com.example.ocrtexttospeech.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ocrtexttospeech.FrontPage;
import com.example.ocrtexttospeech.R;

import java.util.Locale;

public class FourthFragment extends Fragment {

    TextToSpeech tts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextToSpeech.OnInitListener listener =
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(final int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            Log.d("TTS Intro Page", "Text to Speech Engine started successfully.");
                            tts.setLanguage(Locale.US);
                        } else {
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

        if (this.isVisible())
            tts.speak(getString(R.string.currency_detection_intro_page_instructions), TextToSpeech.QUEUE_ADD, null, null);
        else {
            tts.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        tts.stop();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater
                .inflate(
                        R.layout.fragment_first,
                        container, false);

        TextView tvTitle = view.findViewById(R.id.title_tv);
        TextView tvInstructions = view.findViewById(R.id.instructions_tv);

        tvTitle.setText(R.string.currency_detection_intro_title);
        tvInstructions.setText(R.string.currency_detection_intro_page_instructions);


        return view;
    }
}