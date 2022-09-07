package com.example.englishwordslearning;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.time.LocalDateTime.now;

/**
 * Выполняет работу с бд
 */
public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper dbHelper;

    public static DbHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DbHelper(context);
        }
        return dbHelper;
    }

    private static final int DB_VERSION = 1;
    private static final String LOG_TAG = DbHelper.class.getSimpleName();
    private static SQLiteDatabase db;

    private DbHelper(Context context) {
        super(context, "myDb", null, DB_VERSION);
        db = getWritableDatabase();
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    /**
     * При первом запуске приложения создает бд
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        String query = "CREATE TABLE dictionary(\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "engWord TEXT,\n" +
                "transcription TEXT,\n" +
                "rusWord TEXT,\n" +
                "version TEXT,\n" +
                "zone TEXT DEFAULT 'gray',\n" +
                "lastShow TEXT,\n" +
                "tags TEXT\n" +
                ");";
        db.execSQL(query);
        query = "CREATE TABLE file_version(\n" +
                "version INTEGER\n" +
                ");";
        db.execSQL(query);
        query = "INSERT INTO file_version (version) VALUES (0);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //при необходимости добавить логику обновления бд
    }

    /**
     * Возвращает версию файла (словаря), которая хранится в бд
     */
    public int getFileVersion() {
        int version = 0;
        Cursor c = db.query("file_version", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            version = c.getInt(c.getColumnIndex("version"));
            Log.d(LOG_TAG, "file_version = " + version);
        } else {
            Log.d(LOG_TAG, "Table file_version is empty!");
        }
        c.close();
        return version;
    }

    /**
     * Возвращает Set всех слов на английском, имеющихся в бд
     */
    public Set<String> getDbEngWords() {
        Set<String> words = new HashSet<>();
        Cursor c = db.query("dictionary", null, null, null, null, null, null);
        while (c.moveToNext()) {
            words.add(c.getString(c.getColumnIndex("engWord")));
        }
        Log.d(LOG_TAG, "DB contains " + words.size() + " words...");
        c.close();
        return words;
    }

    /**
     * Возвращает Map<String, Word> всех слов, имеющихся в бд (ключ - слово на английском, значение - объект Word)
     */
    public Map<String, Word> getDbWords() {
        Map<String, Word> words = new HashMap<>();
        Cursor c = db.query("dictionary", null, null, null, null, null, null);
        while (c.moveToNext()) {
            String engWord = c.getString(c.getColumnIndex("engWord"));
            String lastShow = c.getString(c.getColumnIndex("lastShow"));
            Word word = new Word(
                    engWord,
                    c.getString(c.getColumnIndex("rusWord")),
                    c.getString(c.getColumnIndex("transcription")),
                    c.getString(c.getColumnIndex("tags")),
                    c.getString(c.getColumnIndex("zone")),
                    lastShow == null || lastShow.isEmpty() ? null : LocalDateTime.parse(lastShow)
            );
            words.put(engWord, word);
        }
        c.close();
        return words;
    }

    /**
     * Обновляет зону и дату последнего показа для слова в бд
     */
    public void updateWord(String engWord, String zone) {
        String lastShow = String.valueOf(now());
        ContentValues contentValues = new ContentValues();
        contentValues.put("zone", zone);
        contentValues.put("lastShow", lastShow);
        int updatedRows = db.update("dictionary", contentValues, "engWord = ?", new String[]{engWord});
        Log.d(LOG_TAG, "EngWord = \"" + engWord + "\", zone = \"" + zone + "\", lastShow = \"" + lastShow + "\". Updated " + updatedRows + " rows.");
    }

    /**
     * Сбрасывает прогресс всех слов в бд (сбрасывает зону на серую, удаляет время последнего показа)
     */
    public static void resetProgress() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("zone", "gray");
        contentValues.put("lastShow", "");
        db.update("dictionary", contentValues, null, null);
        db.delete("file_version", null, null);
    }
}