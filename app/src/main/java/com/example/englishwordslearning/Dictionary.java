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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.englishwordslearning.Constants.GRAY;
import static com.example.englishwordslearning.Constants.GREEN;
import static com.example.englishwordslearning.Constants.RED;
import static com.example.englishwordslearning.Constants.YELLOW;
import static java.time.LocalDateTime.now;

import com.example.englishwordslearning.modes.StandartMode;
import com.example.englishwordslearning.modes.Mode;

/**
 * Инициализирует данные словаря, синхронизируя данные в файле и в бд, и управляет ими
 */
public class Dictionary {

    private static final int EXCEL_FILE_VERSION = 4;
    private static Mode mode = new StandartMode();
    private static Map<String, Word> words;
    // Списки слов/выражений на английском (ключи для words)
    private static List<String> engWordsOfGrayZone = new ArrayList<>();
    private static List<String> engWordsOfGreenZone = new ArrayList<>();
    private static List<String> engWordsOfYellowZone = new ArrayList<>();
    private static List<String> engWordsOfRedZone = new ArrayList<>();
    public static String currentWordColor;
    public static int currentWordIndex;
    // Первые 300 слов должны браться только из серой зоны
    public static int startGrayWordPassed = 0;
    public static final Random RANDOM = new Random();
    public static final String LOG_TAG = Dictionary.class.getSimpleName();

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

            // Добавляем/обновляем/удаляем слова в db
            excelWords.forEach((key, word) -> {

                if (word.getUpdateAction() == null) {
                    // Добавляем новые слова
                    if (!dbWords.contains(key)) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("engWord", word.getEng());
                        contentValues.put("rusWord", word.getRus());
                        contentValues.put("transcription", word.getTranscription());
                        contentValues.put("tags", word.getTags());
                        dbHelper.getDb().insert("dictionary", null, contentValues);
                    }
                } else {
                    if (word.getUpdateAction() == Word.UpdateAction.UPDATE) {
                        // Обновляем существующие слова
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("rusWord", word.getRus());
                        contentValues.put("transcription", word.getTranscription());
                        contentValues.put("tags", word.getTags());
                        dbHelper.getDb().update("dictionary", contentValues, "engWord = ?", new String[]{word.getEng()});
                    } else {
                        // Удаляем существующие слова (если word.getUpdateAction() == Word.UpdateAction.REMOVE)
                        dbHelper.getDb().delete("dictionary", "engWord = ?", new String[]{word.getEng()});
                    }
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
        startGrayWordPassed += engWordsOfRedZone.size();
        startGrayWordPassed += engWordsOfYellowZone.size();
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
            String cellNeedUpdate;
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
                cellNeedUpdate = row.getCell(needUpdateColumnIndex) != null ?
                        row.getCell(needUpdateColumnIndex).getStringCellValue().trim() : null;
                cellTags = row.getCell(tagsColumnIndex) != null ?
                        row.getCell(tagsColumnIndex).getStringCellValue().trim() : null;
            }

            if (excelWords.containsKey(cellEng)) {
                Log.d(LOG_TAG, "The word \"" + cellEng + "\" is dublicated!");
            } else {
                excelWords.put(cellEng, new Word(
                        cellEng,
                        cellRus,
                        cellTrans,
                        cellNeedUpdate == null ?
                                null : Word.UpdateAction.hasValue(cellNeedUpdate) ?
                                Word.UpdateAction.valueOf(cellNeedUpdate) : null,
                        cellTags)
                );
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

    public static void changeMode(Mode mode) {
        Dictionary.mode = mode;
    }

    public static Mode getMode(){
        return mode;
    }

    public static String getRandomEngWord() {
        return mode.getRandomEngWord();
    }

    @SuppressWarnings("ConstantConditions")
    public static String checkWordLastShow(String engWord) {
       return availableWord(engWord) ? mode.getRandomEngWord(): engWord;
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

    public static List<String> getEngWordsOfGrayZone() {
        return engWordsOfGrayZone;
    }

    public static int getEngWordsOfGrayZoneCount() {
        return engWordsOfGrayZone.size();
    }

    public static List<String> getEngWordsOfGreenZone() {
        return engWordsOfGreenZone;
    }

    public static int getEngWordsOfGreenZoneCount() {
        return engWordsOfGreenZone.size();
    }

    public static List<String> getEngWordsOfYellowZone() {
        return engWordsOfYellowZone;
    }

    public static int getEngWordsOfYellowZoneCount() {
        return engWordsOfYellowZone.size();
    }

    public static List<String> getEngWordsOfRedZone() {
        return engWordsOfRedZone;
    }

    public static int getEngWordsOfRedZoneCount() {
        return engWordsOfRedZone.size();
    }

    public static String getCurrentWordColor() {
        return currentWordColor;
    }

    public static boolean first300WordsDistributed(){
        return startGrayWordPassed > 300;
    }

    public static void resetProgress(Context context) {
        words.clear();
        engWordsOfGrayZone.clear();
        engWordsOfGreenZone.clear();
        engWordsOfYellowZone.clear();
        engWordsOfRedZone.clear();
        startGrayWordPassed = 0;
        DbHelper.resetProgress();
        // собираем кэш (мб можно вынести в отдельную функцию т.к. вызывается 2 раза - здесь и в init)
        words = DbHelper.getInstance(context).getDbWords();
        words.values().forEach(word -> getEngWordsList(word.getZone()).add(word.getEng()));
    }

    public static List<String> availableWordList(List<String> list){
        return list.stream().filter(Dictionary::availableWord).collect(Collectors.toList());
    }
    
    private static boolean availableWord(String engWord){
        LocalDateTime lastShow = words.get(engWord).getLastShow();
        return lastShow != null && ChronoUnit.HOURS.between(lastShow, now()) < 8;
    }
}