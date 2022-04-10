package com.example.simplify_debts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
    }

    private Button addPeople;
    private RecyclerView list;
    private ArrayList<Dictionary<String, String>> people = new ArrayList<>();
    private peopleAdapter listAdapter = new peopleAdapter(people);

    private void setViews(){
        addPeople =  findViewById(R.id.addPerson);
        list = findViewById(R.id.peopleList);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(listAdapter);
    }

    private void addPeopleToList() {
        Toast.makeText(this, "button", Toast.LENGTH_SHORT).show();
        Dictionary<String, String> person = new Hashtable<>();
        person.put("uid", (UUID.randomUUID()).toString());
        person.put("name", "NAME");
        people.add(person);
        Log.d(TAG, "addPeopleToList: " + people.toString());
        listAdapter.notifyDataSetChanged();
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
            viewHolder.name.setText(Data.get(pos).get("name"));
            viewHolder.uid.setText(Data.get(pos).get("uid"));
        }

    }
}