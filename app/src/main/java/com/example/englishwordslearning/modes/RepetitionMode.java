package com.example.englishwordslearning.modes;

import static com.example.englishwordslearning.Constants.GREEN;
import static com.example.englishwordslearning.Constants.YELLOW;
import static com.example.englishwordslearning.Dictionary.availableWordList;

import com.example.englishwordslearning.Dictionary;
import com.example.englishwordslearning.utils.RandomUtil;

/**
 * Режим работы "Повторение"
 */
public class RepetitionMode implements Mode {

    private int YELLOW_BORDER = 30;//70% шанс выпадения слова из желтой зоны, 30% шанс выпадения слова из зеленой зоны

    @Override
    public String getRandomEngWord() {

        while (true) {

            if (!availableWordList(Dictionary.getEngWordsOfYellowZone()).isEmpty() && RandomUtil.success(YELLOW_BORDER)) {
                return Dictionary.chooseWordZone(YELLOW);
            }

            if (!availableWordList(Dictionary.getEngWordsOfGreenZone()).isEmpty()) {
                return Dictionary.chooseWordZone(GREEN);
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