package com.example.englishwordslearning.modes;

import static com.example.englishwordslearning.Constants.GRAY;
import static com.example.englishwordslearning.Constants.RED;
import static com.example.englishwordslearning.Dictionary.availableWordList;

import com.example.englishwordslearning.Dictionary;
import com.example.englishwordslearning.utils.RandomUtil;

/**
 * Режим работы "Изучение нового"
 */
public class LearningNewMode implements Mode {

    private int GRAY_BORDER = 40;//60% шанс выпадения слова из серой зоны, 40% шанс выпадения слова из красной зоны

    @Override
    public String getRandomEngWord() {

        while (true) {

            if (!Dictionary.getEngWordsOfGrayZone().isEmpty() && RandomUtil.success(GRAY_BORDER)) {
                return Dictionary.chooseWordZone(GRAY);
            }

            if (!availableWordList(Dictionary.getEngWordsOfRedZone()).isEmpty()) {
                return Dictionary.chooseWordZone(RED);
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