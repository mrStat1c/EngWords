package com.example.englishwordslearning;

//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WordsHolder {

    public static final Map<String, String> WORDS = new HashMap<>();

//    private  void init() {
//        try {
//            FileInputStream inputStream = new FileInputStream(new File("raw/dictionary.xlsx"));
//            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
//            HSSFSheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rowIterator = sheet.iterator();
//
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//
//                Cell cell_1 = row.getCell(0);
//                Cell cell_2 = row.getCell(1);
//                WORDS.put(cell_1.getStringCellValue(), cell_2.getStringCellValue());
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        WORDS.put("cat", "кот");
////        WORDS.put("dog", "собака");
////        WORDS.put("parrot", "попугай");
//    }


    public static void initDictionary(Context context) {
        Resources r = context.getResources();
        InputStream inputStream = r.openRawResource(R.raw.dictionary);

        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            String cell_1 = row.getCell(0).getStringCellValue();
            String cell_2 = row.getCell(1).getStringCellValue();
            Log.i("AAA", cell_1 + " added ");
            //todo добавить проверку ячеек на null
//            this.wordsHolder.WORDS.put(cell_1, cell_2);
            WordsHolder.WORDS.put(cell_1, cell_2);
        }
    }

}
