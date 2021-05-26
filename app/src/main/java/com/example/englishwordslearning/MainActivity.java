package com.example.englishwordslearning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView engView;
    TextView rusView;
    Button translateBtn;
    Button nextBtn;
    private final String LOG_TAG = this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.engView = findViewById(R.id.tv_engView);
        engView.setText("Cat");
        this.rusView = findViewById(R.id.tv_rusView);
        rusView.setText("Кот");
        this.translateBtn = findViewById(R.id.b_translate);
        this.nextBtn = findViewById(R.id.b_next);

        OnClickListener translateBtnClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                rusView.setText("Здесь будет перевод слова \"" + engView.getText() + "\"");
                Log.d(LOG_TAG, "Выполнен перевод слова " + engView.getText());
            }
        };

        OnClickListener nextBtnClicl = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rusView.getText().equals("Cleaned")) {
                    rusView.setText("Cleaned");
                } else {
                    showToast("Already cleaned!");
                }
            }
        };

        translateBtn.setOnClickListener(translateBtnClick);
        nextBtn.setOnClickListener(nextBtnClicl);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}