package com.example.realhomework;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class CompareActivity extends AppCompatActivity {

    private static final String TAG = "Net";

    ImageButton btn1;

    EditText edit1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        btn1 = findViewById(R.id.btn1);
        edit1 = findViewById(R.id.edt1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CompareActivity.this,MainActivity4.class);
                String up = edit1.getText().toString();
                intent.putExtra("up",up);
                startActivity(intent);
            }
        });
    }
}