package com.example.ocrtexttospeech.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ocrtexttospeech.FrontPage;
import com.example.ocrtexttospeech.R;

import java.util.Locale;

public class LastFragment extends Fragment {

    TextToSpeech tts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextToSpeech.OnInitListener listener =
                status -> {
                    if (status == TextToSpeech.SUCCESS) {
                        Log.d("TTS Intro Page", "Text to Speech Engine started successfully.");
                        tts.setLanguage(Locale.US);
                    } else {
                        Log.d("TTS Intro Page", "Error starting text to speech engine.");
                    }
                };

        tts = new TextToSpeech(getActivity(), listener);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (this.isVisible())
            tts.speak(getString(R.string.last_page_intro_instructions), TextToSpeech.QUEUE_ADD, null, null);
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
                        R.layout.fragment_last,
                        container, false);

        TextView tvTitle = view.findViewById(R.id.title_tv);
        TextView tvInstructions = view.findViewById(R.id.instructions_tv);

        tvTitle.setText(R.string.last_page_intro_title);
        tvInstructions.setText(R.string.last_page_intro_instructions);

        LinearLayout finish_button = view.findViewById(R.id.finish);
        finish_button.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), FrontPage.class)));

        return view;
    }
}