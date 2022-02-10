package com.example.englishwordslearning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.Button;
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
    private Button greenBtn;
    private Button yellowBtn;
    private Button redBtn;
    private Button infoBtn;
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
        this.infoBtn = findViewById(R.id.info_btn);

        OnClickListener translateClick = x -> {
            String engWord = this.engTextView.getText().toString();
            rusTextView.setText(String.format("%s%s%s",
                    Dictionary.getTranslation(engWord), TRANSCRIPTION_SEPARATOR, Dictionary.getTranscription(engWord)));
        };

        OnClickListener greenBtnClick = x -> {
            String engWord = engTextView.getText().toString();
            this.dbHelper.updateWord(engWord, GREEN);
            Dictionary.changeCurrentWordZone(GREEN);
            Dictionary.updateWordLastShow(engWord);
            presentNewWord();
        };

        OnClickListener yellowBtnClick = x -> {
            String engWord = engTextView.getText().toString();
            this.dbHelper.updateWord(engWord, YELLOW);
            Dictionary.changeCurrentWordZone(YELLOW);
            Dictionary.updateWordLastShow(engWord);
            presentNewWord();
        };

        OnClickListener redBtnClick = x -> {
            String engWord = engTextView.getText().toString();
            this.dbHelper.updateWord(engWord, RED);
            Dictionary.changeCurrentWordZone(RED);
            Dictionary.updateWordLastShow(engWord);
            presentNewWord();
        };

        OnClickListener infoBtnClick = x -> {
            Intent infoActivityIntent = new Intent(this, InfoActivity.class);
//            infoActivityIntent.putExtra(Intent.EXTRA_TEXT, "lalala");
            startActivity(infoActivityIntent);
        };

        this.rusTextView.setOnClickListener(translateClick);
        this.greenBtn.setOnClickListener(greenBtnClick);
        this.yellowBtn.setOnClickListener(yellowBtnClick);
        this.redBtn.setOnClickListener(redBtnClick);
        this.infoBtn.setOnClickListener(infoBtnClick);
    }

    private void presentNewWord() {
        this.engTextView.setText(Dictionary.getRandomEngWord());
        this.rusTextView.setText("");
    }
}