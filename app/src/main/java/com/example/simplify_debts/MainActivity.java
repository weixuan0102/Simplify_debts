package com.example.simplify_debts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(view);
            }
        });
    }

    private EditText editText;
    private Button button;

    private void setViews(){
        editText =  findViewById(R.id.input_numbers);
        button =  findViewById(R.id.next);
    }

    private void sendMessage(View view){
        Intent intent = new Intent(this,CalculateActivity.class);
        String people = editText.getText().toString();
        intent.putExtra("PEOPLE",people);
        startActivity(intent);
    }





}