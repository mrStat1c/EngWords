package com.example.englishwordslearning.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Функционал всплывающих уведомлений
 */
public class ToastUtil {

    /** Выводит всплывающее уведомление
     * @param text Текст уведомления
     * @param context контекст
     */
    public static void showToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}