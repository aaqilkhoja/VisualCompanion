package com.example.ocrtexttospeech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
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

import com.example.ocrtexttospeech.objectDetection.DetectorActivity;
import com.example.ocrtexttospeech.welcome.WelcomePage;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frontpage);
        currency_button = (Button) findViewById(R.id.currency_detection_button);
        ocr_button = (Button) findViewById(R.id.text_to_speech_button);
        object_button = (Button) findViewById(R.id.object_detection_button);
        fourth_button = (Button) findViewById(R.id.misc_button);


        SharedPreferences mPrefs = this.getPreferences(this.MODE_PRIVATE);
        int defaultValue = 0;
        int introPage = mPrefs.getInt("IntroPage", defaultValue);


        Log.d("INTRO PAGE LOG", "Intro Page: " + introPage);

        if (introPage == 0 || true) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putInt("IntroPage", 1);
            editor.apply();

            startActivity(new Intent(FrontPage.this, WelcomePage.class));
        }


//        if(!mPrefs.getAll().containsKey("IntroPage")){
//            startActivity(new Intent(FrontPage.this, WelcomePage.class));
//        }


        TextToSpeech.OnInitListener listener =
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(final int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            Log.d("TTS", "Text to Speech Engine started successfully.");
                            tts.setLanguage(Locale.US);
                        } else {
                            Log.d("TTS", "Error starting text to speech engine.");
                        }
                    }
                };
        tts = new TextToSpeech(this.getApplicationContext(), listener);

        currency_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak("Currency Detection", TextToSpeech.QUEUE_FLUSH, null, null);
                //gDetector.onTouchEvent(event);
                // return true;
            }
        });



        currency_button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(FrontPage.this, "Long press clicked", Toast.LENGTH_LONG).show();
                startActivity(new Intent(FrontPage.this, CurrencyDetection.class));
                return true;
            }
        });


        ocr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak("OCR", TextToSpeech.QUEUE_FLUSH, null, null);
                //gDetector.onTouchEvent(event);
                // return true;

            }
        });

        ocr_button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Toast.makeText(FrontPage.this, "Long press clicked", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(FrontPage.this, MainActivity.class);
                startActivity(intent);


                return true;
            }
        });


        object_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak("Object detection", TextToSpeech.QUEUE_FLUSH, null, null);
                //gDetector.onTouchEvent(event);
                // return true;
            }
        });

        object_button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(FrontPage.this, DetectorActivity.class));
                Toast.makeText(FrontPage.this, "Object long press clicked", Toast.LENGTH_LONG).show();
                return true;
            }
        });


        fourth_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak("Hear the Introduction Again", TextToSpeech.QUEUE_FLUSH, null, null);
                //gDetector.onTouchEvent(event);
                // return true;
            }
        });

        fourth_button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                vibrateNow(1000);
                Toast.makeText(FrontPage.this, "Introduction Button Clicked", Toast.LENGTH_LONG).show();
               startActivity(new Intent(FrontPage.this, WelcomePage.class));
                return true;
            }
        });

      /*  currency_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // gDetector.onTouchEvent()
                tts.speak("Currency Detection", TextToSpeech.QUEUE_FLUSH, null, null);
                gDetector.onTouchEvent(event);
                return true;
            }
        });*/


       /* ocr_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // gDetector.onTouchEvent()
                tts.speak("Optical Character Recognition", TextToSpeech.QUEUE_FLUSH, null, null);
              //  gDetector.onTouchEvent(event);
                return true;
            }
        });*/
/*

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
*/

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

    public void vibratePulse(){
        long [] pattern = {0,100,200,100,200,100};
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else{
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(pattern,-1);
        }
    }

    public void vibrateNow(long millis){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else{
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(millis);
        }
    }
}
