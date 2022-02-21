package com.example.englishwordslearning.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.englishwordslearning.BuildConfig;
import com.example.englishwordslearning.Dictionary;
import com.example.englishwordslearning.R;

public class InfoActivity extends Activity {

    private Button backBtn;
    private TextView infoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        this.backBtn = findViewById(R.id.back_btn);
        this.infoView = findViewById(R.id.tv_infoView);

        View.OnClickListener backClick = x -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivityForResult(intent, 1);
        };

        this.backBtn.setOnClickListener(backClick);

        int greenWordsCount = Dictionary.getEngWordsOfGreenZoneCount();
        int yellowOrRedWordsCount = Dictionary.getEngWordsOfWellowOrRedZoneCount();
        int grayWordsCount = Dictionary.getEngWordsOfGrayZoneCount();
        int allWordsCount = grayWordsCount + greenWordsCount + yellowOrRedWordsCount;
        long greenWordsProportion = Math.round(Double.parseDouble(String.valueOf(greenWordsCount)) / allWordsCount * 100);
        long yellowOrRedWordsProportion = Math.round(Double.parseDouble(String.valueOf(yellowOrRedWordsCount)) / allWordsCount * 100);
        long grayWordsProportion = 100 - greenWordsProportion - yellowOrRedWordsProportion;


        String s1 = String.format("Слов/фраз в словаре = %s\n" +
                        "Изученных слов = %s (~%s%%)\n" +
                        "Слов на изучении = %s (~%s%%)\n" +
                        "Слов, которые еще не попадались = %s (~%s%%)\n" +
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