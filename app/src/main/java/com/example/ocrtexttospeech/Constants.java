package com.example.ocrtexttospeech;

import android.graphics.Color;

import java.util.HashMap;

public class Constants {

    // Image requirements
    public static final int imageSize = 224;
    public static final int RequestCameraPermission = 1001;


    // Values for currency detection
    public static final HashMap<String, Double> values = new HashMap<>();

    static {
        values.put("Zero", 0.00);
        values.put("Hundred", 100.00);
        values.put("Fifty", 50.00);
        values.put("Twenty", 20.00);
        values.put("Ten", 10.00);
        values.put("Five", 5.00);
        values.put("Two", 2.00);
        values.put("One", 1.00);
    }

    public static final float TEXT_SIZE_DIP = 18;
    public static final float MIN_SIZE = 16.0f;
    public static final int[] COLORS = {
            Color.BLUE,
            Color.RED,
            Color.GREEN,
            Color.YELLOW,
            Color.CYAN,
            Color.MAGENTA,
            Color.WHITE,
            Color.parseColor("#55FF55"),
            Color.parseColor("#FFA500"),
            Color.parseColor("#FF8888"),
            Color.parseColor("#AAAAFF"),
            Color.parseColor("#FFFFAA"),
            Color.parseColor("#55AAAA"),
            Color.parseColor("#AA33AA"),
            Color.parseColor("#0D0068")
    };

}
