package com.example.englishwordslearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView engView;
    TextView rusView;
    Button translateBtn;
    Button nextBtn;
    private final String LOG_TAG = this.getClass().getSimpleName();
    WordsHolder wordsHolder = new WordsHolder();
    List<String> ENG_WORDS;
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WordsHolder.initDictionary(this);
        this.ENG_WORDS = new ArrayList<>(wordsHolder.WORDS.keySet());


        this.engView = findViewById(R.id.tv_engView);
        this.engView.setText(getRandomEngWord());
        this.rusView = findViewById(R.id.tv_rusView);
        this.translateBtn = findViewById(R.id.b_translate);
        this.nextBtn = findViewById(R.id.b_next);

        OnClickListener translateBtnClick = v -> {
            String engWord = engView.getText().toString();
            rusView.setText(this.wordsHolder.WORDS.get(engWord));
            Log.d(LOG_TAG, "Выполнен перевод слова " + engWord);
        };

        OnClickListener nextBtnClicl = v -> {
            this.engView.setText(getRandomEngWord());
            this.rusView.setText("");

        };

        translateBtn.setOnClickListener(translateBtnClick);
        nextBtn.setOnClickListener(nextBtnClicl);
    }

    private String getRandomEngWord(){
        return ENG_WORDS.get(this.random.nextInt(ENG_WORDS.size()));
    }
}