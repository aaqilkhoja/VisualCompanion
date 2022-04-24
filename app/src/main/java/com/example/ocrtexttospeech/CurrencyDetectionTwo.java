package com.example.ocrtexttospeech;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.ocrtexttospeech.ml.ModelUnquant;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.text.TextRecognizer;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Locale;

public class CurrencyDetectionTwo extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener{

    ImageView imageView;
    CameraSource cameraSource;
    SurfaceView cameraView;
    Button captureImage;
    int imageSize = 224;
    //private GestureDetector gDetector;

    final int RequestCameraPermission = 1001;

    HashMap<String, Double> values = new HashMap<>();

    int totalAnswer = 0;

    // Text to Speech Implementation.
    TextToSpeech tts;

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RequestCameraPermission) {
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_detection_two);

        //gDetector = new GestureDetector(this,this);

        cameraView = findViewById(R.id.surfaceView);
        captureImage = findViewById(R.id.button3);
        imageView = findViewById(R.id.imageView);


        values.put("Zero", 0.00);
        values.put("Hundred", 100.00);
        values.put("Fifty", 50.00);
        values.put("Twenty", 20.00);
        values.put("Ten", 10.00);
        values.put("Five", 5.00);
        values.put("Two", 2.00);
        values.put("One", 1.00);


        TextToSpeech.OnInitListener listener =
                status -> {
                    if(status==TextToSpeech.SUCCESS) {
                        Log.d("TTS", "Text to Speech Engine started successfully.");
                        tts.setLanguage(Locale.US);
                    }else{
                        Log.d("TTS", "Error starting text to speech engine.");
                    }
                };
        tts = new TextToSpeech(this.getApplicationContext(),listener);

        final TextRecognizer txtRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();


        cameraSource = new CameraSource.Builder(getApplicationContext(), txtRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(30.0f)
                .setAutoFocusEnabled(true)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(CurrencyDetectionTwo.this, new String[]{Manifest.permission.CAMERA}, RequestCameraPermission);
                    }

                    cameraSource.start(cameraView.getHolder());
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        captureImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    clickImage();
                } else {
                    //Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
    }

    private void clickImage(){
        if (cameraSource != null){
            cameraSource.takePicture(null, bytes -> {
                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                System.out.println(image);

                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            });
        }
    }


    @SuppressLint("DefaultLocale")
    public void classifyImage(Bitmap image) {
        try {
            ModelUnquant model = ModelUnquant.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }

                Log.i("CurrencyConfidence:", confidences[i] + ", i=" + i );
            }
            String finalAnswer;
            String[] classes = {"Hundred", "Fifty", "Twenty", "Ten", "Five", "Two", "One"};
            if(maxConfidence > 0.7) {
                finalAnswer = classes[maxPos];
            }
            else{
                finalAnswer = "Zero";
            }

            System.out.println("Final Answer: " + finalAnswer);


            if (values.containsKey(finalAnswer))
                totalAnswer += values.get(finalAnswer);

            System.out.println("Total Answer: " + totalAnswer);

            String ttsOutput = "There is " + finalAnswer + " here. Total is now " + totalAnswer + " dollars";
            tts.speak(ttsOutput, TextToSpeech.QUEUE_FLUSH, null, null);

            StringBuilder s = new StringBuilder();
            for (int i = 0; i < classes.length; i++) {
                s.append(String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100));
            }
            System.out.println("I got here");


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //gDetector.onTouchEvent(event);
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

        Toast.makeText(CurrencyDetectionTwo.this, "Long Press", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e2.getY()<e1.getY())
        {
            tts.stop();
            cameraSource.stop();

            tts.speak("Back to the Front Page", TextToSpeech.QUEUE_FLUSH, null, null);

            Intent intent = new Intent(CurrencyDetectionTwo.this, FrontPage.class);
            startActivity(intent);

        }

        return false;
    }

}
