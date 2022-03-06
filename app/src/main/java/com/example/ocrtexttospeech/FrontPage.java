package com.example.ocrtexttospeech;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Locale;


public class FrontPage extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {
    Button currency_button, ocr_button, object_button, fourth_button;
    TextToSpeech tts;

    private GestureDetector gDetector;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gDetector.onTouchEvent(event);
        return true;
    }

    float x1, x2, y1, y2;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frontpage);
        currency_button = (Button) findViewById(R.id.currency_detection_button);
        ocr_button = (Button) findViewById(R.id.text_to_speech_button);
        object_button = (Button) findViewById(R.id.object_detection_button);
        fourth_button = (Button) findViewById(R.id.misc_button);



        TextToSpeech.OnInitListener listener =
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(final int status) {
                        if(status==TextToSpeech.SUCCESS) {
                            Log.d("TTS", "Text to Speech Engine started successfully.");
                            tts.setLanguage(Locale.US);
                        }else{
                            Log.d("TTS", "Error starting text to speech engine.");
                        }
                    }
                };
        tts = new TextToSpeech(this.getApplicationContext(),listener);


        currency_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // gDetector.onTouchEvent()
                tts.speak("Currency Detection", TextToSpeech.QUEUE_FLUSH, null, null);
             //   gDetector.onTouchEvent(event);
                return true;
            }
        });


        ocr_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // gDetector.onTouchEvent()
                tts.speak("Optical Character Recognition", TextToSpeech.QUEUE_FLUSH, null, null);
              //  gDetector.onTouchEvent(event);
                return true;
            }
        });

       object_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // gDetector.onTouchEvent()
                tts.speak("Object Detection", TextToSpeech.QUEUE_FLUSH, null, null);
             //   gDetector.onTouchEvent(event);
                return true;
            }
        });

        fourth_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // gDetector.onTouchEvent()
                tts.speak("Miscellanious Button", TextToSpeech.QUEUE_FLUSH, null, null);
              //  gDetector.onTouchEvent(event);
                return true;
            }
        });

       // View linearLayoutTouch = (LinearLayout) findViewById(R.id.swipe_main);

    //
        //   gDetector = new GestureDetector(this,this);

     //   linearLayoutTouch.setOnTouchListener(new View.OnTouchListener() {
     //       @Override
    //        public boolean onTouch(View v, MotionEvent event) {

    //            gDetector.onTouchEvent(event);
    //            return true;
    //        }
    //    });

        //View frontPageTouch = (LinearLayout) findViewById(R.id.swipe_main);
      //  frontPageTouch.setOnTouchListener(new OnSwipeTouchListener (FrontPage.this));

    }

    /*public boolean OnTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1=event.getX();
                y1=event.getY();
                break;

            case MotionEvent.ACTION_UP:
                x2=event.getX();
                y2=event.getY();

                if(x1<x2){
                    //swipe left
                    Intent intent = new Intent(FrontPage.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(FrontPage.this, "Left", Toast.LENGTH_SHORT).show();
                }
                else if(x2>x1){
                    //swipe right
                    Toast.makeText(FrontPage.this, "Right", Toast.LENGTH_SHORT).show();

                }*/

               // if(y1<y2){
                    //swipe up
                 //   Toast.makeText(FrontPage.this, "Up", Toast.LENGTH_SHORT).show();
               // }

                //if(y2>y1){
                    //swipe down
                 //   Toast.makeText(FrontPage.this, "Down", Toast.LENGTH_SHORT).show();
               // }






    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

      //  if(e1.getX()<e2.getX())
      //  {
      //      Intent intent = new Intent(FrontPage.this, MainActivity.class);
      //      startActivity(intent);
      //  }


        return false;
    }
}
