package com.example.englishwordslearning.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.englishwordslearning.R;

public class SplashScreenActivity extends Activity {
    // Время в милесекундах, в течение которого будет отображаться Splash Screen
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            // По истечении времени, запускаем главный активити, а Splash Screen закрываем
            Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
            SplashScreenActivity.this.startActivity(mainIntent);
            SplashScreenActivity.this.finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}
