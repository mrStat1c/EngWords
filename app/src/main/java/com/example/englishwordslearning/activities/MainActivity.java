package com.example.englishwordslearning.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.englishwordslearning.DbHelper;
import com.example.englishwordslearning.Dictionary;
import com.example.englishwordslearning.R;

import static com.example.englishwordslearning.utils.ColorUtil.currentColorIntValue;

/**
 * Отвечает за работу основного экрана приложения
 */
public class MainActivity extends Activity {

    private TextView engTextView;
    private TextView rusTextView;
    private Button greenBtn;
    private Button yellowBtn;
    private Button redBtn;
    private Button statisticBtn;
    private Button currentWordZoneIndicator;
    private TextView redCounterTextView;
    private DbHelper dbHelper;
    private MainActivityListeners listeners = new MainActivityListeners(this);

    /**
     * Инициализирует основной экран. Связывает java объекты с элементами экрана и определяет их работу
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.dbHelper = DbHelper.getInstance(this);
        Dictionary.init(this, this.dbHelper);

        this.engTextView = findViewById(R.id.tv_engView);
        this.rusTextView = findViewById(R.id.tv_rusView);
        this.currentWordZoneIndicator = findViewById(R.id.current_word_zone_indicator);
        this.redCounterTextView = findViewById(R.id.red_cnt_view);
        presentNewWord();
        this.greenBtn = findViewById(R.id.green_btn);
        this.yellowBtn = findViewById(R.id.yellow_btn);
        this.redBtn = findViewById(R.id.red_btn);
        this.statisticBtn = findViewById(R.id.statistic_btn);

        this.rusTextView.setOnClickListener(this.listeners.getTranslateClickListener(this.rusTextView));
        this.greenBtn.setOnClickListener(this.listeners.getGreenBtnClickListener(this.dbHelper));
        this.yellowBtn.setOnClickListener(this.listeners.getYellowBtnClickListener(this.dbHelper));
        this.redBtn.setOnClickListener(this.listeners.getRedBtnClickListener(this.dbHelper));
        this.statisticBtn.setOnClickListener(this.listeners.getStatisticBtnClickListener());
    }

    String getEngText() {
        return this.engTextView.getText().toString();
    }

    /**
     * Выводит новое слово на экран
     */
    void presentNewWord() {
        this.engTextView.setText(Dictionary.getRandomEngWord());
        this.currentWordZoneIndicator.setBackgroundColor(currentColorIntValue());
        this.rusTextView.setText("");
        this.redCounterTextView.setText(Dictionary.difficultWord(getEngText()) ?
                Dictionary.getRedCounter(getEngText()) + "!" : "");
    }
}