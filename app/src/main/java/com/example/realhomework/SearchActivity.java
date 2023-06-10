package com.example.realhomework;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "Net";


    ImageButton button1;

    ImageButton button2;

    EditText edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        button1 = findViewById(R.id.btn1);
        button2 = findViewById(R.id.btn2);
        edit = findViewById(R.id.search_view_edit_text);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this,MainActivity2.class);
                String Bv = edit.getText().toString();
                intent.putExtra("url",Bv);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this,CompareActivity.class);
                startActivity(intent);
            }
        });
    }
}
