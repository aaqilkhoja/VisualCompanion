package com.example.ocrtexttospeech.welcome;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ocrtexttospeech.R;

import java.util.Locale;

public class FirstFragment extends Fragment {

    public FirstFragment() {
        // Required empty public constructor
    }

    TextToSpeech tts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initializing Text To speech
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
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(
                R.layout.fragment_first,
                container, false);

        // Waiting for fragment to load then using text to speech
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tts.speak(getString(R.string.firstPageString), TextToSpeech.QUEUE_ADD, null, null);
            }
        }, 1000);


        TextView tvInst = view.findViewById(R.id.instructions_tv);
        tvInst.setText(getString(R.string.firstPageString));


       //    if(this.isVisible())
         //    tts.speak("Welcome to the Visual Companion app. This app will help you navigate through your daily life by utilizing the capabilities of this application. To navigate to the next page, please swipe to the right.", TextToSpeech.QUEUE_ADD, null, null);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.isVisible())
            tts.speak("Welcome to the Visual Companion app!", TextToSpeech.QUEUE_ADD, null, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        tts.stop();
    }
}
