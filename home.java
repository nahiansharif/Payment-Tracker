package com.example.paymenttracker;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;




public class home extends AppCompatActivity {

    private Button plusButton;
    private TextView coins;

    int receivedPoints = 0;


    private ListView displayList;



    ArrayList<modelClass> info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //load and display data
         loadData();

        Bundle bundle = getIntent().getExtras();


//getting UI components
        displayList = findViewById(R.id.showDetailList);
        coins = findViewById(R.id.points);




//opening user input screen
        plusButton = findViewById(R.id.plusButton);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    addingTracerScreen(); }
        });

    }

        public void addingTracerScreen()
    {
        Intent intent = new Intent(this, add.class);

        startActivity(intent);
    }


    private void loadData() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);
        Gson gson = new Gson();

        String json = sharedPreferences.getString("tracker", null);

        Type type = new TypeToken<ArrayList<modelClass>>() {}.getType();

        info = gson.fromJson(json, type);

        if (info == null) {
            info = new ArrayList<>();

        }

        else
        {
            ListView displayList = findViewById(R.id.showDetailList);

            // Create a custom ArrayAdapter
            ArrayAdapter<modelClass> adapter = new ArrayAdapter<modelClass>(this, R.layout.activity_detail_list, info) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        convertView = inflater.inflate(R.layout.activity_detail_list, parent, false);
                    }

                    modelClass item = getItem(position);


                    TextView textTitle = convertView.findViewById(R.id.textTitle);
                    TextView textAmount = convertView.findViewById(R.id.textAmount);
                    TextView textDate = convertView.findViewById(R.id.textDate);
                    TextView textDescription = convertView.findViewById(R.id.textDescription);
                    Button completeButton = convertView.findViewById(R.id.completeTracker);
                    Button deleteButton = convertView.findViewById(R.id.deleteTracker);


                    textTitle.setText(item.title );
                    textAmount.setText("Amount:     $" + item.amount);
                    textDate.setText("Due Date:    " + item.date);
                    textDescription.setText("Description: " + item.description);
                    completeButton.setTag(position);
                    deleteButton.setTag(position);

                    completeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int index = (int) view.getTag();
                            if (index >= 0 && index < info.size()) {
                                info.remove(index);
                                coinsSystem(true);
                                updateData();

                            }
                        }
                    });

                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int index = (int) view.getTag();
                            if (index >= 0 && index < info.size()) {
                                info.remove(index);
                                coinsSystem(false);
                                updateData();
                            }
                        }
                    });





                    return convertView;
                }
            };

            displayList.setAdapter(adapter); // Set the adapter to the ListView

        }


    }

    public void updateData()
    {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(info);
        editor.putString("tracker", json);
        editor.apply();

        loadData();

        Intent intent = new Intent(this, home.class);
        String coinsValue = coins.getText().toString();
        int coinsInt = Integer.parseInt(coinsValue);
        intent.putExtra("sendPoints", coinsInt);
        startActivity(intent);


    }

    public void coinsSystem(Boolean complete) {
        String coinsValue = coins.getText().toString();
        int coinsInt = Integer.parseInt(coinsValue);

        if (complete) {
            coinsInt += 50;
        } else {
            coinsInt -= 25;
        }

        coins.setText(String.valueOf(coinsInt));
    }








}