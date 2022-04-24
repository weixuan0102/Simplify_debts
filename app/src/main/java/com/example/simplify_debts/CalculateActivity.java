package com.example.simplify_debts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Set;

public class CalculateActivity extends AppCompatActivity {

    private static final String TAG = "CalculateActivity";

    private MyTransactionsAdapter transactionsAdapter;
    private ImageView addButton;
    private Button previousButton;
    private Button calculateButton;

    private ArrayList<Integer> debt_list = new ArrayList<>();
    private ArrayList<String> giver_list = new ArrayList<>();
    private ArrayList<String> receiver_list = new ArrayList<>();

    private Set<String> nameSet = new ArraySet<>();
    private Set<String> uidSet = new ArraySet<>();
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<String> uidList = new ArrayList<>();

    private RecyclerView list;

    private ArrayList<String>uid;
    private ArrayList<String>name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        setView();

        SharedPreferences preferences = getSharedPreferences("peopleList", MODE_PRIVATE);
        nameSet = preferences.getStringSet("name", new ArraySet<>());
        uidSet = preferences.getStringSet("uid", new ArraySet<>());
        nameList.clear();
        uidList.clear();
        nameList.addAll(nameSet);
        uidList.addAll(uidSet);

        transactionsAdapter = new MyTransactionsAdapter(giver_list, receiver_list, debt_list);
        addButton.setOnClickListener(v -> addTransaction());
        previousButton.setOnClickListener(v -> finish());
        calculateButton.setOnClickListener(v -> cal());
        list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        list.setAdapter(transactionsAdapter);
    }

    private void setView(){
        previousButton = findViewById(R.id.previous);
        calculateButton = findViewById(R.id.calculate);
        addButton = findViewById(R.id.addTransactions);
        list = findViewById(R.id.Transactions);
    }

    private void addTransaction(){
        View v = LayoutInflater.from(this).inflate(R.layout.transaction_dialog, null);
        Spinner nameSpinnerSrc = v.findViewById(R.id.person_spinner_source);
        Spinner nameSpinnerDst= v.findViewById(R.id.person_spinner_dest);
        TextInputEditText moneyInput = v.findViewById(R.id.TextInputMoney);

        nameSpinnerSrc.setAdapter(new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, nameList));
        nameSpinnerDst.setAdapter(new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, nameList));
        nameSpinnerSrc.setSelection(0);
        nameSpinnerDst.setSelection(1);

        new MaterialAlertDialogBuilder(this)
            .setView(v)
            .setTitle("Add a transaction")
            .setPositiveButton("Add", (dialogInterface, i) -> {
                String m = moneyInput.getText().toString();
                if (m.isEmpty()) {
                    Toast.makeText(CalculateActivity.this, "debt can not be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Integer money = Integer.parseInt(m);
                addTran(money, nameSpinnerSrc.getSelectedItem().toString(), nameSpinnerDst.getSelectedItem().toString());
                dialogInterface.dismiss();
            })
            .setNegativeButton("Cancel", ((dialogInterface, i) -> dialogInterface.dismiss()))
            .create()
            .show();

    }

    public void addTran(Integer money, String giver, String receiver) {
        debt_list.add(money);
        giver_list.add(giver);
        receiver_list.add(receiver);
        transactionsAdapter.notifyItemInserted(debt_list.size() - 1);
    }

    public boolean removeTran(int pos) {
        if (debt_list.size() <= pos) return false;
        debt_list.remove(pos);
        giver_list.remove(pos);
        receiver_list.remove(pos);
        return true;
    }

    private void cal(){
        ArrayList<Integer> giver_index = new ArrayList<>();
        ArrayList<Integer> receiver_index = new ArrayList<>();

        for (int i = 0;i<debt_list.size();i++) {
            giver_index.add(nameList.indexOf(giver_list.get(i)));
            receiver_index.add(nameList.indexOf(receiver_list.get(i)));
        }

        Intent intent;
        intent = new Main().createGraph(nameList.toArray(new String[0]), debt_list.size(),giver_index, receiver_index, debt_list);
        intent.setClass(CalculateActivity.this,ResultActivity.class);
        startActivity(intent);
    }

    public class MyTransactionsAdapter extends RecyclerView.Adapter<MyTransactionsAdapter.ViewHolder>{
        private ArrayList<String> recv_list;
        private ArrayList<String> give_list;
        private ArrayList<Integer> debt_list;

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
            private TextView giver, receiver, money;

            public ViewHolder(View v){
                super(v);
                giver = v.findViewById(R.id.giver);
                receiver = v.findViewById(R.id.receviver);
                money = v.findViewById(R.id.money);
            }

            @Override
            public boolean onLongClick(View v) {
                return CalculateActivity.this.removeTran(getAdapterPosition());
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_list,viewGroup,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
            holder.giver.setText(give_list.get(pos));
            holder.receiver.setText(recv_list.get(pos));
            holder.money.setText(debt_list.get(pos).toString());
        }

        @Override
        public int getItemCount() {
            return debt_list.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public <T, G, E>MyTransactionsAdapter (T __giver_list, G __receiver_list, E __debt_list){
            super();
            this.debt_list = (ArrayList<Integer>) __debt_list;
            this.give_list = (ArrayList<String>) __giver_list;
            this.recv_list = (ArrayList<String>) __receiver_list;
        }
    }



}