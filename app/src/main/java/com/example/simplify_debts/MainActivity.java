package com.example.simplify_debts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private final ArrayList<Dictionary<String, String>> people = new ArrayList<>();
    private final peopleAdapter listAdapter = new peopleAdapter(people);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();

        addPeople.setOnClickListener(v -> addPeopleToList());
        nextButton.setOnClickListener(v -> startActivity(new Intent(this, CalculateActivity.class)));
    }

    private void setViews(){
        addPeople =  findViewById(R.id.addPerson);
        nextButton = findViewById(R.id.nextButton);
        RecyclerView list = findViewById(R.id.peopleList);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.addItemDecoration(new DividerItemDecoration(list.getContext(), DividerItemDecoration.VERTICAL));
        list.setAdapter(listAdapter);
    }

    private void addPeopleToList() {
        Toast.makeText(this, "button", Toast.LENGTH_SHORT).show();
        Dictionary<String, String> person = new Hashtable<>();
        person.put("uid", (UUID.randomUUID()).toString().replace("-", ""));
        person.put("name", "NAME");
        people.add(person);
        Log.d(TAG, "addPeopleToList: " + people);
        listAdapter.notifyItemInserted(people.size()-1);
    }

    class peopleAdapter extends RecyclerView.Adapter<peopleAdapter.ViewHolder> {

        LayoutInflater layoutInflater;
        private ArrayList<Dictionary<String, String>> Data;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView name;
            public final TextView uid;

            public ViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.name);
                uid =  (TextView) view.findViewById(R.id.uid);
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
            viewHolder.name.setText((pos + 1) + ". " + Data.get(pos).get("name"));
            viewHolder.uid.setText("UID: " + Data.get(pos).get("uid"));
        }

    }
}