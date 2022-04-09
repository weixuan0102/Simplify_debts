package com.example.simplify_debts;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();

        addPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPeopleToList();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
    }

    private Button addPeople;
    private Button next;
    private RecyclerView list;
    private ArrayList<Dictionary<String, String>> people = new ArrayList<>();
    private peopleAdapter listAdapter = new peopleAdapter(people);

    private void setViews() {
        addPeople = findViewById(R.id.addPerson);
        next = findViewById(R.id.button2);
        list = findViewById(R.id.peopleList);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(listAdapter);
    }

    private void addPeopleToList() {
        Toast.makeText(this, "button", Toast.LENGTH_SHORT).show();
        Dictionary<String, String> person = new Hashtable<>();
        person.put("uid", (UUID.randomUUID()).toString());
        //need to fix
        person.put("name", "NAME");
        people.add(person);
        Log.d(TAG, "addPeopleToList: " + people.toString());
        listAdapter.notifyDataSetChanged();
    }

    private void next(){
        ArrayList<String> uid = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        for(Dictionary<String,String> d :people){
            uid.add(d.get("uid"));
            name.add(d.get("name"));
        }
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,CalculateActivity.class);
        intent.putStringArrayListExtra("uid",uid);
        intent.putStringArrayListExtra("name",name);
        startActivity(intent);
    }

    private void onItemLongClick(int pos) {
        people.remove(pos);
        listAdapter.notifyItemRemoved(pos);
    }

    class peopleAdapter extends RecyclerView.Adapter<peopleAdapter.ViewHolder> {

        LayoutInflater layoutInflater;
        private ArrayList<Dictionary<String, String>> Data;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final EditText name;
            public final TextView uid;

            public ViewHolder(View view) {
                super(view);
                name = (EditText) view.findViewById(R.id.name);
                uid = (TextView) view.findViewById(R.id.uid);

                name.setSelectAllOnFocus(true);

                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        Log.d(TAG, "beforeTextChanged: s = " + charSequence + ", start = " + i + ", count = " + i1 + ", after = " + i2);
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        Log.d(TAG, "onTextChanged: s = " + charSequence + ", start = " + i + ", before = " + i1 + ", count = " + i2);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.d(TAG, "afterTextChanged: " + s);
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            Dictionary<String, String> person = new Hashtable<>();
                            person.put("uid", (UUID.randomUUID()).toString());
                            person.put("name", s.toString());
                            people.set(pos,person);
//                            listAdapter.notifyItemChanged(pos);
//                            rename(pos,s.toString());
                        }
                    }
                };
                name.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                    @Override
                    public void onFocusChange(View v,boolean hasFocus){
                        if(hasFocus){
                            name.addTextChangedListener(textWatcher);
                        }else{
                            name.removeTextChangedListener(textWatcher);
                        }
                    }
                });


                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            onItemLongClick(pos);
                        }

                        return true;
                    }
                });
            }


        }

        public peopleAdapter(ArrayList<Dictionary<String, String>> __data) {
            super();
            Data = __data;
        }

        @Override
        public int getItemCount() {
            return Data.size();
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.people_list, viewGroup, false);
            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int pos) {
            viewHolder.name.setText(Data.get(pos).get("name"));
            viewHolder.uid.setText(Data.get(pos).get("uid"));
        }

    }
}