package com.example.englishwordslearning.utils;

import static com.example.englishwordslearning.Constants.GRAY;
import static com.example.englishwordslearning.Constants.GREEN;
import static com.example.englishwordslearning.Constants.RED;
import static com.example.englishwordslearning.Constants.YELLOW;

import android.graphics.Color;

import com.example.englishwordslearning.Dictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * Утилитный класс для работы с цветами
 */
public class ColorUtil {

    private static final Map<String, String> colors = new HashMap<>();

    static {
        colors.put(GREEN, "#27AA43");
        colors.put(YELLOW, "#F3F325");
        colors.put(RED, "#A61515");
        colors.put(GRAY, "#808080");
    }

    public static int currentColorIntValue(){
        return Color.parseColor(colors.get(Dictionary.getCurrentWordColor()));
    }
}