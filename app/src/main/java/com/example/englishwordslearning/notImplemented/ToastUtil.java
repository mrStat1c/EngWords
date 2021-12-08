package com.example.englishwordslearning.notImplemented;

import android.content.Context;
import android.widget.Toast;

/**
 * todo Пока не используется
 * Функционал уведомлений
 */
public class ToastUtil {

    public static void showToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
