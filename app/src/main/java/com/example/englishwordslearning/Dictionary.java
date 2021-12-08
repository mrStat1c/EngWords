package com.example.englishwordslearning;

import android.content.ContentValues;
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
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static com.example.englishwordslearning.Constants.GRAY;
import static com.example.englishwordslearning.Constants.GRAY_BORDER;
import static com.example.englishwordslearning.Constants.GREEN;
import static com.example.englishwordslearning.Constants.RED;
import static com.example.englishwordslearning.Constants.RED_BORDER;
import static com.example.englishwordslearning.Constants.YELLOW;
import static com.example.englishwordslearning.Constants.YELLOW_BORDER;

/**
 * Инициализирует данные словаря, синхронизируя данные в файле и в бд, и управляет ими
 */
public class Dictionary {

    private static final int EXCEL_FILE_VERSION = 3;
    private static Map<String, Word> words;
    // Списки слов/выражений на английском (ключи для words)
    private static List<String> engWordsOfGrayZone = new ArrayList<>();
    private static List<String> engWordsOfGreenZone = new ArrayList<>();
    private static List<String> engWordsOfYellowZone = new ArrayList<>();
    private static List<String> engWordsOfRedZone = new ArrayList<>();
    private static String currentWordColor;
    private static int currentWordIndex;
    // Первые 300 слов должны браться только из серой зоны
    private static int startGrayWordPassed = 0;
    private static final Random RANDOM = new Random();
    private static final String LOG_TAG = Dictionary.class.getSimpleName();

    /**
     * Инициализирует данные словаря, синхронизируя данные в файле и в бд
     */
    public static void init(Context context, DbHelper dbHelper) {

        // Сравниваем версии EXCEL_FILE_VERSION и myDb.file_version.version
        int version = dbHelper.getFileVersion();
        if (EXCEL_FILE_VERSION > version) {//если вышла новая сборка

            // Собираем слова из excel
            Map<String, Word> excelWords = getExcelWords(context);
            //собираем слова из db
            Set<String> dbWords = dbHelper.getDbEngWords();

            // Добавляем новые слова в db
            excelWords.entrySet().forEach(entry -> {
                if (!dbWords.contains(entry.getKey())) {
                    Word word = entry.getValue();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("engWord", word.getEng());
                    contentValues.put("rusWord", word.getRus());
                    contentValues.put("transcription", word.getTranscription());
                    contentValues.put("tags", word.getTags());
                    dbHelper.getDb().insert("dictionary", null, contentValues);
                }
            });

            // Обновляем версию файла в бд
            ContentValues contentValues = new ContentValues();
            contentValues.put("version", EXCEL_FILE_VERSION);
            dbHelper.getDb().update("file_version", contentValues, null, null);
        }

        //собираем финальные данные (кэш)
        words = dbHelper.getDbWords();
        words.values().forEach(word -> getEngWordsList(word.getZone()).add(word.getEng()));

        //считаем, сколько слов было уже взято из серой зоны
        startGrayWordPassed += engWordsOfGreenZone.size();
        startGrayWordPassed += engWordsOfYellowZone.size();
        startGrayWordPassed += engWordsOfRedZone.size();
    }

    /**
     * Возвращает Map<String, Word> всех слов из файла (ключ - слово на английском, значение - объект Word)
     */
    private static Map<String, Word> getExcelWords(Context context) {
        Resources r = context.getResources();
        XSSFWorkbook workbook;

        try (InputStream inputStream = r.openRawResource(R.raw.dictionary)) {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XSSFSheet sheet = workbook.getSheetAt(0);
        Map<String, Word> excelWords = new HashMap<>();

        for (Row row : sheet) {
            String cellEng;
            String cellRus;
            String cellTrans;
            String cellTags;

            int engColumnIndex = 0;
            int rusColumnIndex = 1;
            int transcriptionColumnIndex = 2;
            int tagsColumnIndex = 3;

            if (row.getCell(engColumnIndex) == null
                    || row.getCell(rusColumnIndex) == null
                    || row.getCell(transcriptionColumnIndex) == null) {
                Log.d(LOG_TAG, "Sheet contains NULL cell(s)!");
                continue;
            } else {
                cellEng = row.getCell(engColumnIndex).getStringCellValue().trim();
                cellRus = row.getCell(rusColumnIndex).getStringCellValue().trim();
                cellTrans = row.getCell(transcriptionColumnIndex).getStringCellValue().trim();
                cellTags = row.getCell(tagsColumnIndex) != null ? row.getCell(tagsColumnIndex).getStringCellValue().trim() : null;
            }

            if (excelWords.containsKey(cellEng)) {
                Log.d(LOG_TAG, "The word \"" + cellEng + "\" is dublicated!");
                continue;
            } else {
                excelWords.put(cellEng, new Word(cellEng, cellRus, cellTrans, cellTags));
            }
        }
        Log.d(LOG_TAG, "Excel file contains " + excelWords.size() + " words...");
        return excelWords;
    }

    /**
     * По названию цвета зоны возвращает List из слов на английском, находящихся в данной зоне
     */
    private static List<String> getEngWordsList(String zone) {
        switch (zone) {
            case GRAY:
                return engWordsOfGrayZone;
            case GREEN:
                return engWordsOfGreenZone;
            case YELLOW:
                return engWordsOfYellowZone;
            case RED:
                return engWordsOfRedZone;
            default:
                throw new RuntimeException("Unknown zone!");
        }
    }

    /**
     * Возвращает случайное английское слово, используя для разных зон разные вероятности
     */
    public static String getRandomEngWord() {
        while (true) {
            int x = RANDOM.nextInt(100) + 1;//[1-100]
            //todo зоны могут быть пустыми. Нужно учитывать это и как-то перераспределять (в 1.0)
            if (x > GRAY_BORDER || startGrayWordPassed <= 300) {
                if (!engWordsOfGrayZone.isEmpty()) {
                    currentWordColor = GRAY;
                    currentWordIndex = RANDOM.nextInt(engWordsOfGrayZone.size());
                    startGrayWordPassed++;
                    Log.d(LOG_TAG, "GRAY zone chosen...");
                    return engWordsOfGrayZone.get(currentWordIndex);
                }
            } else if (x > RED_BORDER) {
                if (!engWordsOfRedZone.isEmpty()) {
                    currentWordColor = RED;
                    currentWordIndex = RANDOM.nextInt(engWordsOfRedZone.size());
                    Log.d(LOG_TAG, "RED zone chosen...");
                    return engWordsOfRedZone.get(currentWordIndex);
                }
            } else if (x > YELLOW_BORDER) {
                if (!engWordsOfYellowZone.isEmpty()) {
                    currentWordColor = YELLOW;
                    currentWordIndex = RANDOM.nextInt(engWordsOfYellowZone.size());
                    Log.d(LOG_TAG, "YELLOW zone chosen...");
                    return engWordsOfYellowZone.get(currentWordIndex);
                }
            } else {
                if (!engWordsOfGreenZone.isEmpty()) {
                    currentWordColor = GREEN;
                    currentWordIndex = RANDOM.nextInt(engWordsOfGreenZone.size());
                    Log.d(LOG_TAG, "GREEN zone chosen...");
                    return engWordsOfGreenZone.get(currentWordIndex);
                }
            }
        }
    }

    /**
     * Возвращает перевод слова на русском
     */
    public static String getTranslation(String engWord) {
        return words.get(engWord).getRus();
    }

    /**
     * Возвращает транскрипцию слова
     */
    public static String getTranscription(String engWord) {
        return words.get(engWord).getTranscription();
    }

    /**
     * Для текущего английского слова (который отображается в данный момент на экране) изменить зону
     */
    public static void changeCurrentWordZone(String newZone) {
        List<String> currentWordList = getEngWordsList(currentWordColor);
        List<String> newWordList = getEngWordsList(newZone);
        newWordList.add(currentWordList.remove(currentWordIndex));
    }
}