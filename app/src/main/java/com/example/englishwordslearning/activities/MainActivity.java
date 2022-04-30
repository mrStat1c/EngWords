package com.example.englishwordslearning.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.englishwordslearning.DbHelper;
import com.example.englishwordslearning.Dictionary;
import com.example.englishwordslearning.R;

import static com.example.englishwordslearning.Constants.GREEN;
import static com.example.englishwordslearning.Constants.RED;
import static com.example.englishwordslearning.Constants.YELLOW;

/**
 * Отвечает за работу основного экрана приложения
 */
public class MainActivity extends Activity {

    private TextView engTextView;
    private TextView rusTextView;
    private Button greenBtn;
    private Button yellowBtn;
    private Button redBtn;
    private Button infoBtn;
    private Button currentWordZoneIndicator;
    private DbHelper dbHelper;
    private final String TRANSCRIPTION_SEPARATOR = "\n------------------\n";
    private final String LOG_TAG = this.getClass().getSimpleName();

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
        presentNewWord();
        this.greenBtn = findViewById(R.id.green_btn);
        this.yellowBtn = findViewById(R.id.yellow_btn);
        this.redBtn = findViewById(R.id.red_btn);
        this.infoBtn = findViewById(R.id.info_btn);

        OnClickListener translateClick = x -> {
            String engWord = this.engTextView.getText().toString();
            rusTextView.setText(String.format("%s%s%s",
                    Dictionary.getTranslation(engWord), TRANSCRIPTION_SEPARATOR, Dictionary.getTranscription(engWord)));
        };

        OnClickListener greenBtnClick = x -> {
            String engWord = engTextView.getText().toString();
            this.dbHelper.updateWord(engWord, GREEN);
            Dictionary.changeCurrentWordZone(GREEN);
            Dictionary.updateWordLastShow(engWord);
            presentNewWord();
        };

        OnClickListener yellowBtnClick = x -> {
            String engWord = engTextView.getText().toString();
            this.dbHelper.updateWord(engWord, YELLOW);
            Dictionary.changeCurrentWordZone(YELLOW);
            Dictionary.updateWordLastShow(engWord);
            presentNewWord();
        };

        OnClickListener redBtnClick = x -> {
            String engWord = engTextView.getText().toString();
            this.dbHelper.updateWord(engWord, RED);
            Dictionary.changeCurrentWordZone(RED);
            Dictionary.updateWordLastShow(engWord);
            presentNewWord();
        };

        OnClickListener infoBtnClick = x -> {
            Intent infoActivityIntent = new Intent(this, InfoActivity.class);
            startActivity(infoActivityIntent);
        };

        this.rusTextView.setOnClickListener(translateClick);
        this.greenBtn.setOnClickListener(greenBtnClick);
        this.yellowBtn.setOnClickListener(yellowBtnClick);
        this.redBtn.setOnClickListener(redBtnClick);
        this.infoBtn.setOnClickListener(infoBtnClick);
    }

    private void presentNewWord() {
        this.engTextView.setText(Dictionary.getRandomEngWord());
        this.currentWordZoneIndicator.setBackgroundColor(Color.parseColor(bgColorValue(Dictionary.getCurrentWordColor())));
        this.rusTextView.setText("");
    }

    private String bgColorValue(String color) {
        switch (color) {
            case GREEN: {
                return "#27AA43";
            }
            case RED: {
                //для красно-желтой зоны
                return "#FF8C00";
            }
            default: {
                return "#808080";
            }
        }
    }
}