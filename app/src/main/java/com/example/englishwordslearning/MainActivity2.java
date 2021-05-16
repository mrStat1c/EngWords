package com.example.englishwordslearning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView engView = findViewById(R.id.tv_engView);
        engView.setText("Cat");
        TextView rusView = findViewById(R.id.tv_rusView);
        rusView.setText("Кот");
    }
}