package com.example.simplify_debts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private Button share, previous, finish;

    private void setView() {
        resultAdapter = new MyResultAdapter(money);
        share = findViewById(R.id.share);
        previous = findViewById(R.id.previous);
        finish = findViewById(R.id.finish);
        list = findViewById(R.id.resultList);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(resultAdapter);

        share.setOnClickListener(v->{
            list.measure(
                    View.MeasureSpec.makeMeasureSpec(list.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            Bitmap b = Bitmap.createBitmap(list.getWidth(),list.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            list.draw(new Canvas(b));

            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), b, null,null));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, null));
        });

        previous.setOnClickListener(v -> finish());
        finish.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
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
            private TextView giver, receiver, money;

            public ViewHolder(View view) {
                super(view);

                giver = view.findViewById(R.id.giver);
                receiver = view.findViewById(R.id.receviver);
                money = view.findViewById(R.id.money);

            }

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_list, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.giver.setText(giver.get(position));
            holder.receiver.setText(taker.get(position));
            holder.money.setText(dataSet.get(position));

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