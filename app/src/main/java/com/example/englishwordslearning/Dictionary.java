package com.example.englishwordslearning;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Dictionary {

    /**
     * key - слово/выражение на английском, value - перевод на русском
     */
    private static final Map<String, String> WORDS = new HashMap<>();

    /**
     * Список слов/выражений на английском (ключи для WORDS)
     */
    private static final List<String> ENG_WORDS = new ArrayList<>();
    private static int dictionarySize = 0;
    private static Random random = new Random();
    private static final String LOG_TAG = Dictionary.class.getSimpleName();

    public static void init(Context context) {
        Resources r = context.getResources();
        XSSFWorkbook workbook;

        try (InputStream inputStream = r.openRawResource(R.raw.dictionary)) {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XSSFSheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            String cell1;
            String cell2;

            if (row.getCell(0) == null || row.getCell(1) == null) {
                Log.e(LOG_TAG, "Sheet contains NULL cell(s)!");
                continue;
            } else {
                cell1 = row.getCell(0).getStringCellValue().trim();
                cell2 = row.getCell(1).getStringCellValue();
            }

            if (WORDS.containsKey(cell1)) {
                Log.e(LOG_TAG, "The word \"" + cell1 + "\" is dublicated!");
                continue;
            }

            WORDS.put(cell1, cell2);
            ENG_WORDS.add(cell1);
            dictionarySize = ENG_WORDS.size();
        }
    }

    public static String getRandomEngWord() {
        return ENG_WORDS.get(random.nextInt(dictionarySize));
    }

    public static String getTranslation(String engWord) {
        return WORDS.get(engWord);
    }

}