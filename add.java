package com.example.paymenttracker;

import androidx.appcompat.app.AppCompatActivity;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.example.paymenttracker.modelClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;





import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class add extends AppCompatActivity {

    private Button backButton;
    private Button checkButton;

    private TextView tvTitle;
    private TextView tvAmount;
    private TextView tvDueDate;

    private EditText etTitle;
    private EditText etAmount;
    private EditText etDueDate;
    private EditText etDescription;

    ArrayList<modelClass> info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);



        loadData();

        checkButton = findViewById(R.id.successAdd);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting UI components
                etTitle = findViewById(R.id.etTitle);
                etAmount = findViewById(R.id.etAmount);
                etDueDate = findViewById(R.id.etDueDate);
                etDescription = findViewById(R.id.etDescription);
                tvTitle = findViewById(R.id.textTitle);
                tvAmount = findViewById(R.id.textAmount);
                tvDueDate = findViewById(R.id.textDueDates);

                //load the object array from the machine
                loadData();

                //storing user's input
                String title = etTitle.getText().toString();
                String amount = etAmount.getText().toString();
                String dueDate = etDueDate.getText().toString();
                String description = etDescription.getText().toString();

                //if important field is empty do nohting except turn color into red
//title
                if (title.isEmpty()) {
                    tvTitle.setTextColor(getResources().getColor(R.color.red));
                }
                else {
                    tvTitle.setTextColor(getResources().getColor(R.color.regularText));
                }
//amount
                if (amount.isEmpty()) {
                    tvAmount.setTextColor(getResources().getColor(R.color.red));
                }

                else{
                    tvAmount.setTextColor(getResources().getColor(R.color.regularText));
                }
//due date with input validation
                if (dueDate.isEmpty()) {
                    tvDueDate.setTextColor(getResources().getColor(R.color.red));
                }
// if its not in correct format, input validation
                else if (dateIV(dueDate) == false) {
                    tvDueDate.setTextColor(getResources().getColor(R.color.red));
                    dueDate = "";
                }

                else{
                    tvDueDate.setTextColor(getResources().getColor(R.color.regularText));
                }
//if description is empty, assign "N/A"
                if (description.isEmpty()) {

                    description = "N/A";
                }





// if every required items is not empty, save data and go to homescreen
                if(!title.isEmpty() && !amount.isEmpty() && !dueDate.isEmpty())
                {


                    saveData(title, amount, dueDate, description);


                    plus2home();

                }

            }
        });

        backButton = findViewById(R.id.add2home);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

             plus2home();
            }
        });
    }
    public void plus2home() {
        loadData();
        Intent intent = new Intent(this, home.class);

        startActivity(intent);
    }

    private void saveData(String retrieveTitle,String retrieveAmount,String retrieveDate,String retrieveDescription) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        info.add(new modelClass(retrieveTitle, retrieveAmount, retrieveDate, retrieveDescription));
        String json = gson.toJson(info);
        editor.putString("tracker", json);
        editor.apply();

        loadData();

    }


    private void loadData()
    {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);
        Gson gson = new Gson();

        String json =sharedPreferences.getString("tracker", null);

        Type type = new TypeToken<ArrayList<modelClass>>(){}.getType();

        info=gson.fromJson(json,type);

        if(info == null)
        {
            info = new ArrayList<>();
        }


    }



    public static Boolean dateIV(String date)
    {
        String dateFormat = "\\d{2}/\\d{2}/\\d{4}"; // Regular expression for format: mm/dd/yyyy
        return date.matches(dateFormat);

    }


}