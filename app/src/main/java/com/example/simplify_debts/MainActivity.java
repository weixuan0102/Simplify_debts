package com.example.simplify_debts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ImageView addPeople;
    private Button nextButton;
    RecyclerView list;
    private ArrayList<Dictionary<String, String>> people;
    private peopleAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();

        SharedPreferences preferences = getSharedPreferences("peopleList", MODE_PRIVATE);
        Log.d(TAG, preferences.getStringSet("people", new ArraySet<>()).toString());
        people = new ArrayList<>();
        listAdapter = new peopleAdapter(people);
        addPeople.setOnClickListener(v -> addPeopleToList());
        nextButton.setOnClickListener(v -> startActivity(new Intent(this, CalculateActivity.class)));
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.addItemDecoration(new DividerItemDecoration(list.getContext(), DividerItemDecoration.VERTICAL));
        list.setAdapter(listAdapter);
    }

    private void setViews(){
        list = findViewById(R.id.peopleList);
        addPeople =  findViewById(R.id.addPerson);
        nextButton = findViewById(R.id.nextButton);
    }

    private void addPeopleToList() {
        View v = LayoutInflater.from(this).inflate(R.layout.person_dialog, null);
        EditText et = v.findViewById(R.id.name);
        new AlertDialog.Builder(this)
                .setView(v)
                .setTitle("Add a person")
                .setPositiveButton("Add", ((dialogInterface, i) -> {
                    String name = et.getText().toString();
                    if (name.length() == 0) {
                        Toast.makeText(MainActivity.this, "Name can not be empty.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Dictionary<String, String> person = new Hashtable<>();
                    person.put("uid", (UUID.randomUUID()).toString().replace("-", ""));
                    person.put("name", name);
                    people.add(person);
                    Log.d(TAG, "addPeopleToList: " + people);
                    listAdapter.notifyItemInserted(people.size()-1);
                    dialogInterface.dismiss();
                }))
                .setNegativeButton("Cancel", ((dialogInterface, i) -> {dialogInterface.dismiss();}))
                .create()
                .show();
    }

    public boolean removePerson(int pos) {
        if (people.size() < pos - 1) return false;
        people.remove(pos);
        listAdapter.notifyItemRemoved(pos);
        return true;
    }

    class peopleAdapter extends RecyclerView.Adapter<peopleAdapter.ViewHolder> {

        private final ArrayList<Dictionary<String, String>> Data;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
            public final TextView name;
            public final TextView uid;

            public ViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.name);
                uid =  (TextView) view.findViewById(R.id.uid);
                view.setOnLongClickListener(this);
            }

            @Override
            public boolean onLongClick(View v) {
                return MainActivity.this.removePerson(getAdapterPosition());
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
        @NonNull
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
            viewHolder.uid.setText("UID: " + Data.get(pos).get("uid"));
        }

    }
}