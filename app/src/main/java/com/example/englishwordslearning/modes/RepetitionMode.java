package com.example.englishwordslearning.modes;

import static com.example.englishwordslearning.Constants.GREEN;
import static com.example.englishwordslearning.Constants.YELLOW;
import static com.example.englishwordslearning.Dictionary.LOG_TAG;
import static com.example.englishwordslearning.Dictionary.availableWordList;
import static com.example.englishwordslearning.Dictionary.currentWordColor;
import static com.example.englishwordslearning.Dictionary.currentWordIndex;

import android.util.Log;

import com.example.englishwordslearning.Dictionary;

public class RepetitionMode implements Mode {

    //желтая и зеленая
    public int YELLOW_BORDER = 30;//70% шанс выпадения слова из желтой зоны, 70% шанс выпадения слова из красной зоны

    @Override
    public String getRandomEngWord() {
        String engWord;
        while (true) {
            int x = Dictionary.RANDOM.nextInt(100) + 1;//[1-100]

            if (!availableWordList(Dictionary.getEngWordsOfYellowZone()).isEmpty() && x > YELLOW_BORDER) {
                currentWordColor = YELLOW;
                currentWordIndex = Dictionary.RANDOM.nextInt(Dictionary.getEngWordsOfYellowZoneCount());
                engWord = Dictionary.checkWordLastShow(Dictionary.getEngWordsOfYellowZone().get(currentWordIndex));
                Log.d(LOG_TAG, "YELLOW zone chosen...");
                return engWord;
            }

            if (!availableWordList(Dictionary.getEngWordsOfGreenZone()).isEmpty()) {
                currentWordColor = GREEN;
                currentWordIndex = Dictionary.RANDOM.nextInt(Dictionary.getEngWordsOfGreenZoneCount());
                engWord = Dictionary.checkWordLastShow(Dictionary.getEngWordsOfGreenZone().get(currentWordIndex));
                Log.d(LOG_TAG, "GREEN zone chosen...");
                return engWord;
            } else if (Dictionary.getEngWordsOfYellowZone().isEmpty()) {
                Dictionary.changeMode(new StandartMode());
                return Dictionary.getRandomEngWord();
            }
        }
    }

    @Override
    public String getName() {
        return "Повторение";
    }
}
