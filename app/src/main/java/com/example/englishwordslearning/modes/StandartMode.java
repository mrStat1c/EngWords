package com.example.englishwordslearning.modes;

import static com.example.englishwordslearning.Constants.GRAY;
import static com.example.englishwordslearning.Constants.GREEN;
import static com.example.englishwordslearning.Constants.RED;
import static com.example.englishwordslearning.Constants.YELLOW;
import static com.example.englishwordslearning.Dictionary.availableWordList;

import com.example.englishwordslearning.Dictionary;
import com.example.englishwordslearning.utils.RandomUtil;

/**
 * Режим работы "Стандартный"
 */
public class StandartMode implements Mode {

    private int GRAY_BORDER = 60;//40% шанс выпадения слова из серой зоны
    private int RED_BORDER = 35;//25% шанс выпадения слова из красной зоны
    private int YELLOW_BORDER = 10;//25% шанс выпадения слова из желтой зоны, 10% шанс выпадения слова из зеленой зоны

    @Override
    public String getRandomEngWord() {
        while (true) {

            if (!Dictionary.getEngWordsOfGrayZone().isEmpty() && (RandomUtil.success(GRAY_BORDER) || !Dictionary.first300WordsDistributed())) {
                return Dictionary.chooseWordZone(GRAY);
            }

            if (RandomUtil.success(RED_BORDER) && !availableWordList(Dictionary.getEngWordsOfRedZone()).isEmpty()) {
                return Dictionary.chooseWordZone(RED);
            }

            if (RandomUtil.success(YELLOW_BORDER) && !availableWordList(Dictionary.getEngWordsOfYellowZone()).isEmpty()) {
                return Dictionary.chooseWordZone(YELLOW);
            }

            if (!availableWordList(Dictionary.getEngWordsOfGreenZone()).isEmpty()) {
                return Dictionary.chooseWordZone(GREEN);
            }
        }
    }

    @Override
    public String getName() {
        return "Стандартный";
    }
}