package com.example.englishwordslearning;

import android.content.Context;
import android.content.res.Resources;

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

    public static void init(Context context) {
        Resources r = context.getResources();
        XSSFWorkbook workbook;

        try (InputStream inputStream = r.openRawResource(R.raw.dictionary)) {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            String cell_1 = row.getCell(0).getStringCellValue();
            String cell_2 = row.getCell(1).getStringCellValue();

            //todo добавить проверку ячеек на null
            WORDS.put(cell_1, cell_2);
            ENG_WORDS.add(cell_1);
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
