package com.example.simplify_debts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalculateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
//        setContentView(linearLayout);
        setView();
        getMessage();

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private Button previousButton;
//    LinearLayout linearLayout = findViewById(R.id.linearLayout);


    private int people;

    private void setView(){
        previousButton = findViewById(R.id.previous);
    }


    private void getMessage(){
        Intent intent = getIntent();
        people = Integer.parseInt(intent.getStringExtra("PEOPLE"));

    }

    private void addInputTexts(int people){

    }

}