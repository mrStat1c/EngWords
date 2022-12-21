package com.example.englishwordslearning.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.englishwordslearning.BuildConfig;
import com.example.englishwordslearning.Dictionary;
import com.example.englishwordslearning.R;

/**
 * Отвечает за работу экрана со статистикой и переключением режимов
 */
public class StatisticActivity extends Activity {

    private Button backBtn;
    private Button resetBtn;
    private Button standartModeBtn;
    private Button learningNewModeBtn;
    private Button repetitionModeBtn;
    private TextView infoView;
    private StatisticActivityListeners listeners = new StatisticActivityListeners(this);

    /**
     * Инициализирует экран. Связывает java объекты с элементами экрана и определяет их работу
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        this.backBtn = findViewById(R.id.back_btn);
        this.resetBtn = findViewById(R.id.reset_btn);
        this.standartModeBtn = findViewById(R.id.standart_mode_btn);
        this.learningNewModeBtn = findViewById(R.id.leaning_new_mode_btn);
        this.repetitionModeBtn = findViewById(R.id.repetition_mode_btn);
        this.infoView = findViewById(R.id.tv_infoView);

        this.backBtn.setOnClickListener(this.listeners.getBackClickListener());
        this.resetBtn.setOnTouchListener(this.listeners.getResetTouchListener());
        this.standartModeBtn.setOnClickListener(this.listeners.getStandartModeListener());
        this.learningNewModeBtn.setOnClickListener(this.listeners.getLearningNewModeClickListener());
        this.repetitionModeBtn.setOnClickListener(this.listeners.getRepetitionModeClickListener());

        fillStatistic();
    }

    /**
     * Заполняет информационное окно данными
     */
    void fillStatistic() {
        int greenWordsCount = Dictionary.getEngWordsOfGreenZoneCount();
        int yellowAndRedWordsCount = Dictionary.getEngWordsOfYellowZoneCount() + Dictionary.getEngWordsOfRedZoneCount();
        int grayWordsCount = Dictionary.getEngWordsOfGrayZoneCount();
        int allWordsCount = grayWordsCount + greenWordsCount + yellowAndRedWordsCount;
        long greenWordsProportion = Math.round(Double.parseDouble(String.valueOf(greenWordsCount)) / allWordsCount * 100);
        long yellowAndRedWordsProportion = Math.round(Double.parseDouble(String.valueOf(yellowAndRedWordsCount)) / allWordsCount * 100);
        long grayWordsProportion = 100 - greenWordsProportion - yellowAndRedWordsProportion;

        String s1 = String.format("Текущий режим:\n%s\n" +
                        "------------------\n" +
                        "Статистика слов/фраз\n\n" +
                        "Всего: %s\n" +
                        "Изученных: %s (~%s%%)\n" +
                        "На изучении: %s (~%s%%)\n" +
                        "Не попадалось: %s (~%s%%)\n" +
                        "------------------\n" +
                        "Версия приложения: %s",
                Dictionary.getMode().getName(),
                allWordsCount,
                greenWordsCount, greenWordsProportion,
                yellowAndRedWordsCount, yellowAndRedWordsProportion,
                grayWordsCount, grayWordsProportion,
                BuildConfig.VERSION_NAME
        );
        this.infoView.setText(s1);
    }
}