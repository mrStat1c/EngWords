package com.example.englishwordslearning.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.englishwordslearning.BuildConfig;
import com.example.englishwordslearning.Dictionary;
import com.example.englishwordslearning.R;
import com.example.englishwordslearning.ToastUtil;


public class InfoActivity extends Activity {

    private Button backBtn;
    private Button resetBtn;
    private TextView infoView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        this.backBtn = findViewById(R.id.back_btn);
        this.resetBtn = findViewById(R.id.reset_btn);
        this.infoView = findViewById(R.id.tv_infoView);

        View.OnClickListener backClick = x -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivityForResult(intent, 1);
        };

        View.OnTouchListener resetTouch = (v, event) -> {
            if (v.isPressed() && event.getAction() == MotionEvent.ACTION_UP) {
                long eventDuration = event.getEventTime() - event.getDownTime();
                if (eventDuration > 3000) {
                    Dictionary.resetProgress(this);
                    recreate();
                    ToastUtil.showToast("Прогресс сброшен", this);
                } else {
                    ToastUtil.showToast("Чтобы сбросить прогресс, удерживайте более 3 секунд", this);
                }
            }
            return false;
        };

        this.backBtn.setOnClickListener(backClick);
        this.resetBtn.setOnTouchListener(resetTouch);

        int greenWordsCount = Dictionary.getEngWordsOfGreenZoneCount();
        int yellowOrRedWordsCount = Dictionary.getEngWordsOfWellowOrRedZoneCount();
        int grayWordsCount = Dictionary.getEngWordsOfGrayZoneCount();
        int allWordsCount = grayWordsCount + greenWordsCount + yellowOrRedWordsCount;
        long greenWordsProportion = Math.round(Double.parseDouble(String.valueOf(greenWordsCount)) / allWordsCount * 100);
        long yellowOrRedWordsProportion = Math.round(Double.parseDouble(String.valueOf(yellowOrRedWordsCount)) / allWordsCount * 100);
        long grayWordsProportion = 100 - greenWordsProportion - yellowOrRedWordsProportion;


        String s1 = String.format("Статистика слов/фраз\n\n" +
                        "Всего: %s\n" +
                        "Изученных: %s (~%s%%)\n" +
                        "На изучении: %s (~%s%%)\n" +
                        "Не попадалось: %s (~%s%%)\n" +
                        "\n------------------\n\n" +
                        "Версия приложения = %s",
                Dictionary.getEngWordsOfGrayZoneCount()
                        + Dictionary.getEngWordsOfGreenZoneCount()
                        + Dictionary.getEngWordsOfWellowOrRedZoneCount(),
                Dictionary.getEngWordsOfGreenZoneCount(), greenWordsProportion,
                Dictionary.getEngWordsOfWellowOrRedZoneCount(), yellowOrRedWordsProportion,
                Dictionary.getEngWordsOfGrayZoneCount(), grayWordsProportion,
                BuildConfig.VERSION_NAME
        );

        this.infoView.setText(s1);
    }

}