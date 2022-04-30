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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static com.example.englishwordslearning.Constants.GRAY;
import static com.example.englishwordslearning.Constants.GREEN;
import static com.example.englishwordslearning.Constants.RED;
import static com.example.englishwordslearning.Constants.YELLOW;
import static java.time.LocalDateTime.now;

/**
 * Инициализирует данные словаря, синхронизируя данные в файле и в бд, и управляет ими
 */
public class Dictionary {

    private static final int EXCEL_FILE_VERSION = 4;
    private static Map<String, Word> words;
    // Списки слов/выражений на английском (ключи для words)
    private static List<String> engWordsOfGrayZone = new ArrayList<>();
    private static List<String> engWordsOfGreenZone = new ArrayList<>();
    private static List<String> engWordsOfYellowOrRedZone = new ArrayList<>();
    private static String currentWordColor;
    private static int currentWordIndex;
    // Первые 300 слов должны браться только из серой зоны
    private static int startGrayWordPassed = 0;
    private static final Random RANDOM = new Random();
    public static int GRAY_BORDER = 60;//40% шанс выпадения слова из серой зоны
    public static int YELLOW_RED_BORDER = 5;//55% шанс выпадения слова из желтой или красной зоны
    //5%  шанс выпадения слова из зеленой зоны
    private static final String LOG_TAG = Dictionary.class.getSimpleName();

    /**
     * Инициализирует данные словаря, синхронизируя данные в файле и в бд
     */
    public static void init(Context context, DbHelper dbHelper) {

        // Если кэш уже собран, переинициилизировать не нужно
        if (words != null) return;

        // Сравниваем версии EXCEL_FILE_VERSION и myDb.file_version.version
        int version = dbHelper.getFileVersion();
        if (EXCEL_FILE_VERSION > version) {//если вышла новая сборка

            // Собираем слова из excel
            Map<String, Word> excelWords = getExcelWords(context);
            //собираем слова из db
            Set<String> dbWords = dbHelper.getDbEngWords();

            // Добавляем новые слова в db
            excelWords.entrySet().forEach(entry -> {
                Word word = entry.getValue();
                if (!dbWords.contains(entry.getKey())) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("engWord", word.getEng());
                    contentValues.put("rusWord", word.getRus());
                    contentValues.put("transcription", word.getTranscription());
                    contentValues.put("tags", word.getTags());
                    dbHelper.getDb().insert("dictionary", null, contentValues);
                } else if (word.isNeedUpdate()) {
                    // Обновляем необходимые существующие слова
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("rusWord", word.getRus());
                    contentValues.put("transcription", word.getTranscription());
                    contentValues.put("tags", word.getTags());
                    dbHelper.getDb().update("dictionary", contentValues, "engWord = ?", new String[]{word.getEng()});
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
        startGrayWordPassed += engWordsOfYellowOrRedZone.size();
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
            boolean cellNeedUpdate;
            String cellTags;

            int engColumnIndex = 0;
            int rusColumnIndex = 1;
            int transcriptionColumnIndex = 2;
            int needUpdateColumnIndex = 3;
            int tagsColumnIndex = 4;

            if (row.getCell(engColumnIndex) == null
                    || row.getCell(rusColumnIndex) == null
                    || row.getCell(transcriptionColumnIndex) == null) {
                Log.d(LOG_TAG, "Sheet contains NULL cell(s)!");
                continue;
            } else {
                cellEng = row.getCell(engColumnIndex).getStringCellValue().trim();
                cellRus = row.getCell(rusColumnIndex).getStringCellValue().trim();
                cellTrans = row.getCell(transcriptionColumnIndex).getStringCellValue().trim().replace("®", "(r)");
                cellNeedUpdate = row.getCell(needUpdateColumnIndex) != null;
                cellTags = row.getCell(tagsColumnIndex) != null ? row.getCell(tagsColumnIndex).getStringCellValue().trim() : null;
            }

            if (excelWords.containsKey(cellEng)) {
                Log.d(LOG_TAG, "The word \"" + cellEng + "\" is dublicated!");
            } else {
                excelWords.put(cellEng, new Word(cellEng, cellRus, cellTrans, cellNeedUpdate, cellTags));
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
            case RED:
                return engWordsOfYellowOrRedZone;
            default:
                throw new RuntimeException("Unknown zone!");
        }
    }

    /**
     * Возвращает случайное английское слово, используя для разных зон разные вероятности
     */
    public static String getRandomEngWord() {
        String engWord;
        while (true) {
            int x = RANDOM.nextInt(100) + 1;//[1-100]

            if (!engWordsOfGrayZone.isEmpty() && (x > GRAY_BORDER || startGrayWordPassed <= 300)) {
                currentWordColor = GRAY;
                currentWordIndex = RANDOM.nextInt(engWordsOfGrayZone.size());
                startGrayWordPassed++;
                engWord = checkWordLastShow(engWordsOfGrayZone.get(currentWordIndex));
                Log.d(LOG_TAG, "GRAY zone chosen...");
                return engWord;
            }

            if (x > YELLOW_RED_BORDER && !engWordsOfYellowOrRedZone.isEmpty()) {
                currentWordColor = RED;
                currentWordIndex = RANDOM.nextInt(engWordsOfYellowOrRedZone.size());
                engWord = checkWordLastShow(engWordsOfYellowOrRedZone.get(currentWordIndex));
                Log.d(LOG_TAG, "YELLOW or RED zone chosen...");
                return engWord;
            }

            if (!engWordsOfGreenZone.isEmpty()) {
                currentWordColor = GREEN;
                currentWordIndex = RANDOM.nextInt(engWordsOfGreenZone.size());
                engWord = checkWordLastShow(engWordsOfGreenZone.get(currentWordIndex));
                Log.d(LOG_TAG, "GREEN zone chosen...");
                return engWord;
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private static String checkWordLastShow(String engWord) {
        LocalDateTime lastShow = words.get(engWord).getLastShow();
        if (lastShow != null && ChronoUnit.HOURS.between(lastShow, now(ZoneId.of("Europe/Moscow"))) < 8) {
            return getRandomEngWord();
        } else {
            return engWord;
        }
    }

    /**
     * Возвращает перевод слова на русском
     */
    @SuppressWarnings("ConstantConditions")
    public static String getTranslation(String engWord) {
        return words.get(engWord).getRus();
    }

    /**
     * Возвращает транскрипцию слова
     */
    @SuppressWarnings("ConstantConditions")
    public static String getTranscription(String engWord) {
        return words.get(engWord).getTranscription();
    }

    /**
     * Для текущего английского слова (отображается в данный момент на экране) изменить зону
     */
    public static void changeCurrentWordZone(String newZone) {
        List<String> currentWordList = getEngWordsList(currentWordColor);
        List<String> newWordList = getEngWordsList(newZone);
        newWordList.add(currentWordList.remove(currentWordIndex));
    }

    @SuppressWarnings("ConstantConditions")
    public static void updateWordLastShow(String engWord) {
        words.get(engWord).setLastShow(now());
    }

    public static int getEngWordsOfGrayZoneCount() {
        return engWordsOfGrayZone.size();
    }

    public static int getEngWordsOfGreenZoneCount() {
        return engWordsOfGreenZone.size();
    }

    public static int getEngWordsOfWellowOrRedZoneCount() {
        return engWordsOfYellowOrRedZone.size();
    }

    public static String getCurrentWordColor(){
        return currentWordColor;
    }
}