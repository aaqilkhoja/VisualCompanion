package com.example.ocrtexttospeech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {

    SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    TextToSpeech tts;
    Button button1;
    Button button_stop;
    String sentence;
   // private Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


    final int RequestCameraPermission = 1001;
    private GestureDetector gDetector;

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermission: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibratePulse();
        gDetector = new GestureDetector(this,this);

        cameraView = findViewById(R.id.surfaceView);
        textView = findViewById(R.id.text_View);

        View linearLayoutTouch = (LinearLayout) findViewById(R.id.layout_main);


        linearLayoutTouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


       // TextToSpeech tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
         //   @Override
           // public void onInit(int status) {

          //  }
        //});


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


        //using getApplicationContext instead of getContext. Don't know if that will affect anything while merging.

        // View.getContext(): Returns the context the view is currently running in. Usually the currently active Activity.
        // Activity.getApplicationContext(): Returns the context for the entire application (the process all the Activities are running inside of).
        // Use this instead of the current Activity context if you need a context tied to the lifecycle of the entire application, not just the current Activity.


        final TextRecognizer txtRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        //    if (!txtRecognizer.isOperational()){
        //      Log.w("MainActivity","Detected dependencies not found");
        // }else

        //{

        //start of camera builder

        cameraSource = new CameraSource.Builder(getApplicationContext(), txtRecognizer)
                .setFacing(cameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(15.0f)
                .setAutoFocusEnabled(true)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, RequestCameraPermission);
                    }

                    cameraSource.start(cameraView.getHolder());

                } catch (IOException e) {

                    e.printStackTrace();


                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

                cameraSource.stop();
            }
        });

        //end of camera builder

        //   button1.setOnClickListener(new View.OnClickListener() {
        //      @Override
        //    public void onClick(View v) {

        //    cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
        //          @Override
        //            public void surfaceCreated(@NonNull SurfaceHolder holder) {
        //                  try{
        //                        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
        //                              ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},RequestCameraPermission);
//                            }

        //                          cameraSource.start(cameraView.getHolder());
//
        //                      }catch (IOException e){
//
        //                          e.printStackTrace();
//
//
//


        //                    }
        //                  }
//
        //                @Override
        //                  public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
//
        //                  }
//
        //         @Override
        //           public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

        //         cameraSource.stop();
        //       }
        //     });


        txtRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

            }


            @Override
            public void receiveDetections(@NonNull Detector.Detections<TextBlock> detections) {

                final SparseArray<TextBlock> items = detections.getDetectedItems();
                if (items.size() != 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock item = items.valueAt(i);
                        sb.append(item.getValue());
                        sb.append("\n");

                    }
                    textView.post(new Runnable() {
                        @Override
                        public void run() {


                            textView.setText(sb.toString());
                            sentence = sb.toString();
                            //tts.speak(sb.toString(), TextToSpeech.QUEUE_FLUSH,null,null);
                            //cameraSource.release();

                        }
                    });
                }
            }
        });


//});
        linearLayoutTouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               // gDetector.onTouchEvent()
                tts.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, null);
                gDetector.onTouchEvent(event);
                return true;
            }
        });

      //  button_stop.setOnClickListener(new View.OnClickListener() {
        //    @Override
          //  public void onClick(View v) {
                //  cameraSource.release();
            //    tts.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, null);

           // }
       // });


        //  }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gDetector.onTouchEvent(event);
        return true;
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
        //TextToSpeech tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
          //  @Override
           // public void onInit(int status) {

          //  }
        //});
        Toast.makeText(MainActivity.this, "Long Press", Toast.LENGTH_SHORT);

        //Intent intent = new Intent(MainActivity.this, FrontPage.class);
       // tts.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, null);


        //  startActivity(intent);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e2.getY()<e1.getY())
        {
            tts.stop();
            cameraSource.stop();

           vibratePulse();

            tts.speak("Back to the Front Page", TextToSpeech.QUEUE_FLUSH, null, null);

            Intent intent = new Intent(MainActivity.this, FrontPage.class);
              startActivity(intent);

        }

        return false;
    }
    @Override
    public void onBackPressed(){
        tts.stop();
        cameraSource.stop();
       vibratePulse();
        tts.speak("Back to the Front Page", TextToSpeech.QUEUE_FLUSH, null, null);
        this.finish();
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


