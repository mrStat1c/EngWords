package com.example.englishwordslearning;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView engTextView;
    private TextView rusTextView;
    private Button translateBtn;
    private Button nextBtn;
    private final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dictionary.init(this);

        this.engTextView = findViewById(R.id.tv_engView);
        this.engTextView.setText(Dictionary.getRandomEngWord());
        this.rusTextView = findViewById(R.id.tv_rusView);
        this.translateBtn = findViewById(R.id.b_translate);
        this.nextBtn = findViewById(R.id.b_next);

        OnClickListener translateBtnClick = v -> {
            String engWord = engTextView.getText().toString();
            rusTextView.setText(Dictionary.getTranslation(engWord));
            Log.d(LOG_TAG, "Выполнен перевод слова " + engWord);
        };

        OnClickListener nextBtnClicl = v -> {
            this.engTextView.setText(Dictionary.getRandomEngWord());
            this.rusTextView.setText("");
        };

        translateBtn.setOnClickListener(translateBtnClick);
        nextBtn.setOnClickListener(nextBtnClicl);
    }
}