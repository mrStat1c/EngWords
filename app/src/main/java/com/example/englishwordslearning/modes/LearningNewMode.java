package com.example.englishwordslearning.modes;

import static com.example.englishwordslearning.Constants.GRAY;
import static com.example.englishwordslearning.Constants.RED;
import static com.example.englishwordslearning.Dictionary.LOG_TAG;
import static com.example.englishwordslearning.Dictionary.availableWordList;
import static com.example.englishwordslearning.Dictionary.currentWordColor;
import static com.example.englishwordslearning.Dictionary.currentWordIndex;
import static com.example.englishwordslearning.Dictionary.startGrayWordPassed;

import android.util.Log;

import com.example.englishwordslearning.Dictionary;

public class LearningNewMode implements Mode {


    //серая и красная
    public int GRAY_BORDER = 40;//60% шанс выпадения слова из серой зоны, 40% шанс выпадения слова из красной зоны

    @Override
    public String getRandomEngWord() {
        String engWord;
        while (true) {
            int x = Dictionary.RANDOM.nextInt(100) + 1;//[1-100]

            if (!Dictionary.getEngWordsOfGrayZone().isEmpty() && x > GRAY_BORDER ) {
                currentWordColor = GRAY;
                currentWordIndex = Dictionary.RANDOM.nextInt(Dictionary.getEngWordsOfGreenZoneCount());
                startGrayWordPassed++;
                engWord = Dictionary.checkWordLastShow(Dictionary.getEngWordsOfGrayZone().get(currentWordIndex));
                Log.d(LOG_TAG, "GRAY zone chosen...");
                return engWord;
            }

            if (!availableWordList(Dictionary.getEngWordsOfRedZone()).isEmpty()) {
                currentWordColor = RED;
                currentWordIndex = Dictionary.RANDOM.nextInt(Dictionary.getEngWordsOfRedZoneCount());
                engWord = Dictionary.checkWordLastShow(Dictionary.getEngWordsOfRedZone().get(currentWordIndex));
                Log.d(LOG_TAG, "RED zone chosen...");
                return engWord;
            } else if (Dictionary.getEngWordsOfGrayZone().isEmpty()) {
                Dictionary.changeMode(new StandartMode());
                return Dictionary.getRandomEngWord();
            }
        }
    }

    @Override
    public String getName() {
        return "Изучение нового";
    }
}
