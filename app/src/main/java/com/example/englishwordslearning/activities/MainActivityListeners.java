package com.example.englishwordslearning.activities;

import static com.example.englishwordslearning.Constants.GREEN;
import static com.example.englishwordslearning.Constants.RED;
import static com.example.englishwordslearning.Constants.YELLOW;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;

import com.example.englishwordslearning.DbHelper;
import com.example.englishwordslearning.Dictionary;

import java.util.Locale;

/**
 * Предоставляет обработчики событий для элементов MainActivity
 */
public class MainActivityListeners implements TextToSpeech.OnInitListener {

    private MainActivity activity;
    private TextToSpeech mTextToSpeech;
    private boolean mIsInit;

    public MainActivityListeners(MainActivity activity) {
        this.activity = activity;
    }

    /**
     * @return Предоставляет обработчик клика по зоне вывода перевода слова
     */
    View.OnClickListener getTranslateClickListener(TextView rusTextView) {
        return x -> {
            if (rusTextView.getText().toString().isEmpty()) {
                String engWord = this.activity.getEngText();
                rusTextView.setText(String.format("%s%s%s",
                        Dictionary.getTranslation(engWord),
                        "\n------------------\n",
                        Dictionary.getTranscription(engWord)));
            } else {
                //todo doesn't work. Speak() returns -1 (ERROR)
                this.mTextToSpeech = new TextToSpeech(this.activity, this);
                this.mTextToSpeech.speak("Hello", TextToSpeech.QUEUE_FLUSH, null, "someId");
            }
        };
    }

    /**
     * @return Предоставляет обработчик клика по кнопке "Знаю"
     */
    View.OnClickListener getGreenBtnClickListener(DbHelper dbHelper) {
        return x -> {
            String engWord = this.activity.getEngText();
            String newColor = Dictionary.currentWordColor.equals(RED) ? YELLOW : GREEN;
            dbHelper.updateWord(engWord, newColor);
            Dictionary.changeCurrentWordZone(newColor);
            Dictionary.updateWordLastShow(engWord);
            Dictionary.resetRedCounter(engWord);
            this.activity.presentNewWord();
        };
    }

    /**
     * @return Предоставляет обработчик клика по кнопке "Нужно повторить"
     */
    View.OnClickListener getYellowBtnClickListener(DbHelper dbHelper) {
        return x -> {
            String engWord = this.activity.getEngText();
            dbHelper.updateWord(engWord, YELLOW);
            Dictionary.changeCurrentWordZone(YELLOW);
            Dictionary.updateWordLastShow(engWord);
            Dictionary.resetRedCounter(engWord);
            this.activity.presentNewWord();
        };
    }

    /**
     * @return Предоставляет обработчик клика по кнопке "Не знаю"
     */
    View.OnClickListener getRedBtnClickListener(DbHelper dbHelper) {
        return x -> {
            String engWord = this.activity.getEngText();
            dbHelper.updateWord(engWord, RED);
            Dictionary.changeCurrentWordZone(RED);
            Dictionary.updateWordLastShow(engWord);
            Dictionary.incrementRedCounter(engWord);
            this.activity.presentNewWord();
        };
    }

    /**
     * @return Предоставляет обработчик клика по кнопке перехода к окну статистики
     */
    View.OnClickListener getStatisticBtnClickListener() {
        return x -> {
            Intent infoActivityIntent = new Intent(this.activity, StatisticActivity.class);
            this.activity.startActivity(infoActivityIntent);
        };
    }

    /**
     * @param status TextToSpeech.OnInitListener implementation
     */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Locale locale = new Locale("eng");
            int result = mTextToSpeech.setLanguage(locale);
            if (result == TextToSpeech.LANG_MISSING_DATA) {
                this.mIsInit = false;
            } else {
                this.mIsInit = true;
            }
        } else {
            this.mIsInit = false;
        }
    }

}