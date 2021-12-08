package com.example.englishwordslearning;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.englishwordslearning.Constants.GREEN;
import static com.example.englishwordslearning.Constants.RED;
import static com.example.englishwordslearning.Constants.YELLOW;

/**
 * Отвечает за работу основного экрана приложения
 */
public class MainActivity extends Activity {

    private TextView engTextView;
    private TextView rusTextView;
    private ImageView greenBtn;
    private ImageView yellowBtn;
    private ImageView redBtn;
    private DbHelper dbHelper;
    private final String TRANSCRIPTION_SEPARATOR = "\n------------------\n";
    private final String LOG_TAG = this.getClass().getSimpleName();

    /**
     * Инициализирует основной экран. Связывает java объекты с элементами экрана и определяет их работу
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.dbHelper = new DbHelper(this);
        Dictionary.init(this, this.dbHelper);

        this.engTextView = findViewById(R.id.tv_engView);
        this.rusTextView = findViewById(R.id.tv_rusView);
        presentNewWord();
        this.greenBtn = findViewById(R.id.green_btn);
        this.yellowBtn = findViewById(R.id.yellow_btn);
        this.redBtn = findViewById(R.id.red_btn);

        OnClickListener translateClick = x -> {
            String engWord = this.engTextView.getText().toString();
            rusTextView.setText(String.format("%s%s%s",
                    Dictionary.getTranslation(engWord), TRANSCRIPTION_SEPARATOR, Dictionary.getTranscription(engWord)));
        };

        OnClickListener greenBtnClick = x -> {
            this.dbHelper.updateWordZone(engTextView.getText().toString(), GREEN);
            Dictionary.changeCurrentWordZone(GREEN);
            presentNewWord();
        };

        OnClickListener yellowBtnClick = x -> {
            this.dbHelper.updateWordZone(engTextView.getText().toString(), YELLOW);
            Dictionary.changeCurrentWordZone(YELLOW);
            presentNewWord();
        };

        OnClickListener redBtnClick = x -> {
            this.dbHelper.updateWordZone(engTextView.getText().toString(), RED);
            Dictionary.changeCurrentWordZone(RED);
            presentNewWord();
        };

        this.rusTextView.setOnClickListener(translateClick);
        this.greenBtn.setOnClickListener(greenBtnClick);
        this.yellowBtn.setOnClickListener(yellowBtnClick);
        this.redBtn.setOnClickListener(redBtnClick);
    }

    private void presentNewWord() {
        this.engTextView.setText(Dictionary.getRandomEngWord());
        this.rusTextView.setText("");
    }
}