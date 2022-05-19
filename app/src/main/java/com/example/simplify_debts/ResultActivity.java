package com.example.simplify_debts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    public ArrayList<String> giver = new ArrayList<>();
    public ArrayList<String> taker = new ArrayList<>();
    public ArrayList<String> giver_max_flow = new ArrayList<>();
    public ArrayList<String> taker_max_flow = new ArrayList<>();
    public ArrayList<String> giver_greedy = new ArrayList<>();
    public ArrayList<String> taker_greedy = new ArrayList<>();
    private RecyclerView list;
    private final ArrayList<String> money = new ArrayList<>();
    private ArrayList<String> money_max_flow = new ArrayList<>();
    private ArrayList<String> money_greedy = new ArrayList<>();
    private MyResultAdapter resultAdapter;
    private Button share, previous, finish;
    private Spinner selectMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getData();
        setView();
        System.out.println(money);
    }

    private void setView() {
        resultAdapter = new MyResultAdapter(money);
        share = findViewById(R.id.share);
        previous = findViewById(R.id.previous);
        finish = findViewById(R.id.finish);
        list = findViewById(R.id.resultList);
        selectMethods = findViewById(R.id.methods);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(resultAdapter);

        share.setOnClickListener(v -> {
            list.measure(
                    View.MeasureSpec.makeMeasureSpec(list.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            Bitmap b = Bitmap.createBitmap(list.getWidth(), list.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            list.draw(new Canvas(b));

            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), b, null, null));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, null));
        });

        ArrayList<String> methodsList = new ArrayList<>();
        methodsList.add("Greedy Algorithm");
        methodsList.add("Max Flow Algorithm");
        selectMethods.setAdapter(new ArrayAdapter<>(
                this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                methodsList
        ));

        selectMethods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                giver.clear();
                taker.clear();
                money.clear();
                if (position == 0) {
                    giver.addAll(giver_greedy);
                    taker.addAll(taker_greedy);
                    money.addAll(money_greedy);
                } else {
                    giver.addAll(giver_max_flow);
                    taker.addAll(taker_max_flow);
                    money.addAll(money_max_flow);
                }
                Log.d("Res", Integer.toString(position));

                resultAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        previous.setOnClickListener(v -> finish());
        finish.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
    }

    private void getData() {
        Intent intent = getIntent();
        giver_max_flow = intent.getStringArrayListExtra("giver_max_flow");
        taker_max_flow = intent.getStringArrayListExtra("taker_max_flow");
        money_max_flow = intent.getStringArrayListExtra("money_max_flow");
        giver_greedy = intent.getStringArrayListExtra("giver_greedy");
        taker_greedy = intent.getStringArrayListExtra("taker_greedy");
        money_greedy = intent.getStringArrayListExtra("money_greedy");

        boolean selection = giver_greedy.size() < giver_max_flow.size();
        giver.addAll(selection ? giver_greedy : giver_max_flow);
        taker.addAll(selection ? taker_greedy : taker_max_flow);
        money.addAll(selection ? money_greedy : money_max_flow);
    }


    public class MyResultAdapter extends RecyclerView.Adapter<MyResultAdapter.ViewHolder> {
        private final ArrayList<String> dataSet;

        public MyResultAdapter(ArrayList<String> dataSet) {
            super();
            this.dataSet = dataSet;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_list, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.giverView.setText(giver.get(position));
            holder.receiverView.setText(taker.get(position));
            holder.moneyView.setText(dataSet.get(position));
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView giverView;
            private final TextView receiverView;
            private final TextView moneyView;

            public ViewHolder(View view) {
                super(view);

                giverView = view.findViewById(R.id.giver);
                receiverView = view.findViewById(R.id.receviver);
                moneyView = view.findViewById(R.id.money);

            }

        }
    }
}