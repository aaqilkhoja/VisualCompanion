package com.example.ocrtexttospeech.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.ocrtexttospeech.FrontPage;
import com.example.ocrtexttospeech.R;

public class ThirdFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {

        View view = inflater
                .inflate(
                        R.layout.fragment_third,
                        container, false);

        Button finishBtn = view.findViewById(R.id.finish);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FrontPage.class));
            }
        });


        return view;
    }
}