package com.example.ocrtexttospeech;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.ocrtexttospeech.ml.ModelUnquant;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.text.TextRecognizer;

import org.checkerframework.checker.signedness.qual.Constant;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Locale;

public class CurrencyDetectionTwo extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {

    //Camera Objects
    private GestureDetector gDetector;
    private CameraSource cameraSource;
    private SurfaceView cameraView;

    //Money Map
    HashMap<String, Double> values = Constants.values;

    int totalAnswer = 0;

    // Text to Speech Implementation.
    private TextToSpeech tts;

    // Requesting permission for camera
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.RequestCameraPermission) {
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

        // Defining objects
        cameraView = findViewById(R.id.surfaceView);
        gDetector = new GestureDetector(this, this);
        FrameLayout frameLayout = findViewById(R.id.layout_main);

        //Making the layout clickable
        frameLayout.setOnTouchListener((view, motionEvent) -> {
            gDetector.onTouchEvent(motionEvent);

            return true;
        });

        // Initializing the text to speech
        TextToSpeech.OnInitListener listener =
                status -> {
                    if (status == TextToSpeech.SUCCESS) {
                        Log.d("TTS", "Text to Speech Engine started successfully.");
                        tts.setLanguage(Locale.US);
                    } else {
                        Log.d("TTS", "Error starting text to speech engine.");
                    }
                };
        tts = new TextToSpeech(this.getApplicationContext(), listener);

        final TextRecognizer txtRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        // Initializing the camera
        cameraSource = new CameraSource.Builder(getApplicationContext(), txtRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024) // Camera size
                .setRequestedFps(30.0f) // Frames per second
                .setAutoFocusEnabled(true)
                .build();

        // Checking for camera permissions
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                try {
                    // Creating the camera view
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CurrencyDetectionTwo.this, new String[]{Manifest.permission.CAMERA}, Constants.RequestCameraPermission);
                    }

                    cameraSource.start(cameraView.getHolder());
                } catch (IOException e) {
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
    }

    // Function when camera is clicked
    // Expected behavior: Detect currency and amount of money
    private void clickImage() {
        if (cameraSource != null) {
            cameraSource.takePicture(null, bytes -> {
                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                System.out.println(image);

                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);

                image = Bitmap.createScaledBitmap(image, Constants.imageSize, Constants.imageSize, false);
                classifyImage(image);
            });
        }
    }

    // Identifying amount money and currency
    @SuppressLint("DefaultLocale")
    public void classifyImage(Bitmap image) {
        try {
            ModelUnquant model = ModelUnquant.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * Constants.imageSize * Constants.imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int[] intValues = new int[Constants.imageSize * Constants.imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for (int i = 0; i < Constants.imageSize; i++) {
                for (int j = 0; j < Constants.imageSize; j++) {
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

                Log.i("CurrencyConfidence:", confidences[i] + ", i=" + i);
            }
            String finalAnswer = "";
            String[] classes = {"Hundred", "Fifty", "Twenty", "Ten", "Five", "Two", "One"};
            finalAnswer = classes[maxPos];

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
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            clickImage();
        } else {
            //Request camera permission if we don't have it.
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    // Function when user swipes to go back to the previous page.
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e2.getY() < e1.getY()) { // Identifying the swipe motion.
            this.onBackPressed();
            return false;
        }
        return true;
    }

    // Behavior when user has presses the back button
    @Override
    public void onBackPressed() {
        tts.stop();
        cameraSource.stop();
        tts.speak("Back to the Front Page", TextToSpeech.QUEUE_FLUSH, null, null);
        this.finish();
    }
}
