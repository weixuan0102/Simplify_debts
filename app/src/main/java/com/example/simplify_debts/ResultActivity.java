package com.example.simplify_debts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getData();
        setView();
        System.out.println(money);
    }

    private RecyclerView list;

    public ArrayList<String> giver = new ArrayList<>();
    public ArrayList<String> taker = new ArrayList<>();
    private ArrayList<String> money = new ArrayList<>();
    private MyResultAdapter resultAdapter;

    private void setView() {
        resultAdapter = new MyResultAdapter(money);
        list = findViewById(R.id.resultList);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(resultAdapter);

    }

    private void getData() {
        Intent intent = getIntent();
        giver = intent.getStringArrayListExtra("giver");
        taker = intent.getStringArrayListExtra("taker");
        money = intent.getStringArrayListExtra("money");

    }


    public class MyResultAdapter extends RecyclerView.Adapter<MyResultAdapter.ViewHolder> {
        private ArrayList<String> dataSet;

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView giver, taker, money;

            public ViewHolder(View view) {
                super(view);

                giver = view.findViewById(R.id.giver);
                taker = view.findViewById(R.id.taker);
                money = view.findViewById(R.id.debt);

            }

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.result_list, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.giver.setText(giver.get(position));
            holder.taker.setText(taker.get(position));
            holder.money.setText("$" + dataSet.get(position));

        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public MyResultAdapter(ArrayList<String> dataSet) {
            super();
            this.dataSet = dataSet;
        }
    }
}