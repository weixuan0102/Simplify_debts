package com.example.simplify_debts;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    RecyclerView list;
    private FloatingActionButton addPeople;
    private Button nextButton;
    private peopleAdapter listAdapter;
    private SharedPreferences preferences;
    private ArrayList<String> uidList;
    private ArrayList<String> nameList;
    private Set<String> nameSet;
    private Set<String> uidSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();

        uidList = new ArrayList<>();
        nameList = new ArrayList<>();
        preferences = getSharedPreferences("peopleList", MODE_PRIVATE);
        loadPreferences();
        nextButton.setEnabled(nameList.size() >= 2);
        listAdapter = new peopleAdapter(uidList, nameList);
        addPeople.setOnClickListener(v -> addPeopleToList());
        nextButton.setOnClickListener(v -> startActivity(new Intent(this, CalculateActivity.class)));
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(listAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPreferences();
        listAdapter.notifyDataSetChanged();
    }

    private void setViews() {
        list = findViewById(R.id.peopleList);
        addPeople = findViewById(R.id.addPerson);
        nextButton = findViewById(R.id.nextButton);
    }

    private void loadPreferences() {
        nameSet = preferences.getStringSet("name", new ArraySet<>());
        uidSet = preferences.getStringSet("uid", new ArraySet<>());
        nameList.clear();
        uidList.clear();
        nameList.addAll(nameSet);
        uidList.addAll(uidSet);
        Log.d(TAG, nameSet.toString());
        Log.d(TAG, uidSet.toString());
    }

    private void savePreferences() {
        nameSet.clear();
        uidSet.clear();
        nameSet.addAll(nameList);
        uidSet.addAll(uidList);
        preferences
                .edit()
                .clear()
                .putStringSet("name", nameSet)
                .putStringSet("uid", uidSet)
                .apply();
    }

    private void addPeopleToList() {
        View v = LayoutInflater.from(this).inflate(R.layout.person_dialog, null);
        EditText et = v.findViewById(R.id.name);
        new MaterialAlertDialogBuilder(this)
                .setView(v)
                .setTitle("Add a person")
                .setPositiveButton("Add", ((dialogInterface, i) -> {
                    String name = et.getText().toString();
                    if (name.length() == 0) {
                        Toast.makeText(MainActivity.this, "Name can not be empty.", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (nameList.contains(name)) {
                        Toast.makeText(MainActivity.this, "Name duplicate.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    addPerson(name);
                    savePreferences();
                    nextButton.setEnabled(nameList.size() >= 2);
                    dialogInterface.dismiss();
                }))
                .setNegativeButton("Cancel", ((dialogInterface, i) -> dialogInterface.dismiss()))
                .create()
                .show();
    }

    public void addPerson(String name) {
        nameList.add(name);
        uidList.add((UUID.randomUUID()).toString().replace("-", ""));
        listAdapter.notifyItemInserted(uidList.size() - 1);
    }

    public boolean removePerson(int pos) {
        if (uidList.size() < pos - 1) return false;
        uidList.remove(pos);
        nameList.remove(pos);
        listAdapter.notifyItemRemoved(pos);
        return true;
    }

    class peopleAdapter extends RecyclerView.Adapter<peopleAdapter.ViewHolder> {

        private final ArrayList<String> uidList;
        private final ArrayList<String> nameList;

        public peopleAdapter(ArrayList<String> __uidList, ArrayList<String> __nameList) {
            super();
            uidList = __uidList;
            nameList = __nameList;
        }

        @Override
        public int getItemCount() {
            return uidList.size();
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
            viewHolder.name.setText(nameList.get(pos));
            viewHolder.uid.setText(uidList.get(pos));
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
            public final TextView name;
            public final TextView uid;

            public ViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.name);
                uid = view.findViewById(R.id.uid);
                view.setOnLongClickListener(this);
            }

            @Override
            public boolean onLongClick(View v) {
                boolean ret = MainActivity.this.removePerson(getAdapterPosition());
                savePreferences();
                nextButton.setEnabled(nameList.size() >= 2);
                return ret;
            }

        }

    }
}