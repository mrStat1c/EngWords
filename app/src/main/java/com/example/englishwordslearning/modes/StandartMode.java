package com.example.englishwordslearning.modes;

import static com.example.englishwordslearning.Constants.GRAY;
import static com.example.englishwordslearning.Constants.GREEN;
import static com.example.englishwordslearning.Constants.RED;
import static com.example.englishwordslearning.Constants.YELLOW;
import static com.example.englishwordslearning.Dictionary.LOG_TAG;
import static com.example.englishwordslearning.Dictionary.availableWordList;
import static com.example.englishwordslearning.Dictionary.currentWordColor;
import static com.example.englishwordslearning.Dictionary.currentWordIndex;
import static com.example.englishwordslearning.Dictionary.startGrayWordPassed;

import android.util.Log;

import com.example.englishwordslearning.Dictionary;

public class StandartMode implements Mode{

    public int GRAY_BORDER = 60;//40% шанс выпадения слова из серой зоны
    public int RED_BORDER = 35;//25% шанс выпадения слова из красной зоны
    public int YELLOW_BORDER = 10;//25% шанс выпадения слова из желтой зоны, 10% шанс выпадения слова из зеленой зоны

    @Override
    public String getRandomEngWord() {
        String engWord;
        while (true) {
            int x = Dictionary.RANDOM.nextInt(100) + 1;//[1-100]

            if (!Dictionary.getEngWordsOfGrayZone().isEmpty() && (x > GRAY_BORDER || !Dictionary.first300WordsDistributed())) {
                currentWordColor = GRAY;
                currentWordIndex = Dictionary.RANDOM.nextInt(Dictionary.getEngWordsOfGrayZoneCount());
                startGrayWordPassed++;
                engWord = Dictionary.checkWordLastShow(Dictionary.getEngWordsOfGrayZone().get(currentWordIndex));
                Log.d(LOG_TAG, "GRAY zone chosen...");
                return engWord;
            }

            if (x > RED_BORDER && !availableWordList(Dictionary.getEngWordsOfRedZone()).isEmpty()) {
                currentWordColor = RED;
                currentWordIndex = Dictionary.RANDOM.nextInt(Dictionary.getEngWordsOfRedZoneCount());
                engWord = Dictionary.checkWordLastShow(Dictionary.getEngWordsOfRedZone().get(currentWordIndex));
                Log.d(LOG_TAG, "RED zone chosen...");
                return engWord;
            }

            if (x > YELLOW_BORDER && !availableWordList(Dictionary.getEngWordsOfYellowZone()).isEmpty()) {
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
            }
        }
    }

    @Override
    public String getName() {
        return "Стандартный";
    }
}
