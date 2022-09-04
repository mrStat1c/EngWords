package com.example.englishwordslearning.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.englishwordslearning.R;

/**
 * Отвечает за работу экрана запуска приложения
 */
public class SplashScreenActivity extends Activity {
    private final int SPLASH_DISPLAY_DURATION = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            // По истечении времени запускаем основной экран, а Splash Screen закрываем
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            SplashScreenActivity.this.startActivity(intent);
            SplashScreenActivity.this.finish();
        }, SPLASH_DISPLAY_DURATION);
    }
}