package com.example.simplify_debts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class CalculateActivity extends AppCompatActivity {

    private static final String TAG = "CalculateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        setView();
        getMessage();

        transactionsAdapter = new MyTransactionsAdapter(debt);
        addButton.setOnClickListener(v -> addTransaction());
        previousButton.setOnClickListener(v -> finish());
        calculateButton.setOnClickListener(v -> cal());
        list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        list.setAdapter(transactionsAdapter);
    }

    private MyTransactionsAdapter transactionsAdapter;
    private ImageView addButton;
    private Button previousButton;
    private Button calculateButton;
    //    private ArrayList<String> transactions = new ArrayList<>();
    private ArrayList<Integer> debt = new ArrayList<>();
    private ArrayList<Integer> giver = new ArrayList<>();
    private ArrayList<Integer> taker = new ArrayList<>();
//    LinearLayout linearLayout = findViewById(R.id.linearLayout);
    private RecyclerView list;


    private ArrayList<String>uid;
    private ArrayList<String>name;

    private void setView(){
        previousButton = findViewById(R.id.previous);
        calculateButton = findViewById(R.id.calculate);
        addButton = findViewById(R.id.addTransactions);
        list = findViewById(R.id.Transactions);
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
//        itemTouchHelper.attachToRecyclerView(list);
    }


    private void getMessage(){
        Intent intent = getIntent();
        uid = intent.getStringArrayListExtra("uid");
        name = intent.getStringArrayListExtra("name");
        System.out.println(uid);
        System.out.println(name);
    }

    private void addTransaction(){
        View v = LayoutInflater.from(this).inflate(R.layout.transaction_dialog, null);
        new MaterialAlertDialogBuilder(this)
            .setView(v)
            .setTitle("Add a transaction")
            .setPositiveButton("Add", (dialogInterface, i) -> {
                giver.add(0);
                taker.add(0);
                debt.add(0);
                transactionsAdapter.notifyDataSetChanged();
                dialogInterface.dismiss();
            })
            .setNegativeButton("Cancel", ((dialogInterface, i) -> dialogInterface.dismiss()))
            .create()
            .show();

    }

//    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {
//
//        @Override
//        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//            Toast.makeText(CalculateActivity.this, "on Move", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        @Override
//        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
//            Toast.makeText(CalculateActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
//            //Remove swiped item from list and notify the RecyclerView
//            int position = viewHolder.getAdapterPosition();
//            if(position!=RecyclerView.NO_POSITION){
//                giver.remove(position);
//                taker.remove(position);
//                debt.remove(position);
//                transactionsAdapter.notifyDataSetChanged();
//            }
//        }
//    };

    private void onItemLongClick(int pos) {
        debt.remove(pos);
        giver.remove(pos);
        taker.remove(pos);
        transactionsAdapter.notifyItemRemoved(pos);
    }

    private void cal(){
        System.out.println(giver);
        System.out.println(taker);
        System.out.println(debt);

        Main main = new Main();
        Intent intent;
        intent = main.createGraph(name.toArray(new String[0]),giver.size(),giver,taker,debt);
        intent.setClass(CalculateActivity.this,ResultActivity.class);
        startActivity(intent);

    }

    private void setSpinner(Spinner spinner,ArrayList<String> list){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,list);
        spinner.setAdapter(adapter);
    }

    public class MyTransactionsAdapter extends RecyclerView.Adapter<MyTransactionsAdapter.ViewHolder>{
        private ArrayList<Integer> dataSet;

        class ViewHolder extends RecyclerView.ViewHolder{
            private Spinner p1,p2;
            private EditText money;

            public ViewHolder(View view){
                super(view);

//                p1 = view.findViewById(R.id.p1);
//                p2 = view.findViewById(R.id.p2);
//                money = view.findViewById(R.id.money);
//


//                p1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        int pos = getAdapterPosition();
//                        if(pos!=RecyclerView.NO_POSITION){
//                            giver.set(pos,p1.getSelectedItemPosition());
////                            giver.set(pos,p1.getSelectedItem().toString());
//                        }
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                    }
//                });
//
//                p2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        int pos = getAdapterPosition();
//                        if(pos!=RecyclerView.NO_POSITION){
//                            taker.set(pos,p2.getSelectedItemPosition());
////                            taker.set(pos,p2.getSelectedItem().toString());
//                        }
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                    }
//                });

//                TextWatcher textWatcher = new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        Log.d(TAG, "beforeTextChanged: s = " + charSequence + ", start = " + i + ", count = " + i1 + ", after = " + i2);
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        Log.d(TAG, "onTextChanged: s = " + charSequence + ", start = " + i + ", before = " + i1 + ", count = " + i2);
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        Log.d(TAG, "afterTextChanged: " + s);
//                        int pos = getAdapterPosition();
//                        if (pos != RecyclerView.NO_POSITION) {
//                            if(!s.toString().equals("")){
//                                dataSet.set(pos,Integer.parseInt(s.toString()));
//                            }
//                        }
//                    }
//                };
//
//                money.setOnFocusChangeListener(new View.OnFocusChangeListener(){
//                    @Override
//                    public void onFocusChange(View v,boolean hasFocus){
//                        if(hasFocus){
//                            money.addTextChangedListener(textWatcher);
//                        }else{
//                            money.removeTextChangedListener(textWatcher);
//                        }
//                    }
//                });
//
//                view.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View view) {
//                        int pos = getAdapterPosition();
//                        if (pos != RecyclerView.NO_POSITION) {
//                            onItemLongClick(pos);
//                        }
//
//                        return true;
//                    }
//                });
            }

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_list,viewGroup,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            setSpinner(holder.p1,name);
//            setSpinner(holder.p2,name);
            holder.p1.setSelection(giver.get(position));
            holder.p2.setSelection(taker.get(position));
            holder.money.setText(dataSet.get(position).toString());
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public MyTransactionsAdapter(ArrayList<Integer> dataSet){
            super();
            this.dataSet = dataSet;
        }
    }



}