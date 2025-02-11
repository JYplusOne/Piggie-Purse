package com.example.piggiepurse;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SetBudget extends AppCompatActivity {
    ImageButton returnBtn;
    Button continueBtn;
    EditText inputBudget, budgetDescription;
    Switch notifyToggle;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_budget);

        returnBtn = findViewById(R.id.returnBtn);
        continueBtn = findViewById(R.id.continueBtn);

        inputBudget = findViewById(R.id.inputBudget);
        budgetDescription = findViewById(R.id.budgetDescription);

        notifyToggle = findViewById(R.id.notifyToggle);

        db = new DBHelper(this);

        int userid = HomeFragment.userid;
        //retrieve latest budget information from  database
        Cursor cursor = db.getBudgetData(userid);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String latestBudget = cursor.getString(cursor.getColumnIndex("budget"));
            @SuppressLint("Range") String latestDescription = cursor.getString(cursor.getColumnIndex("description"));
            @SuppressLint("Range") String latestToggle = cursor.getString(cursor.getColumnIndex("toggle"));

            inputBudget.setText(latestBudget);
            budgetDescription.setText(latestDescription);
            notifyToggle.setChecked(Boolean.parseBoolean(latestToggle));
        }
        cursor.close();

        //action for return button
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //action for continue button
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    float floatBudget = Float.parseFloat(inputBudget.getText().toString());

                    String budget = String.format("%.2f", floatBudget);
                    String description = budgetDescription.getText().toString();
                    String toggle = String.valueOf(notifyToggle.isChecked());

                    if (floatBudget == 0.00) {
                        throw new IllegalArgumentException("Amount cannot be zero");
                    }

                    int userid = HomeFragment.userid;
                    Boolean checkInsertData = db.insertOrUpdateBudget(userid, budget, description, toggle);

                    if (checkInsertData == true) {
                        Toast.makeText(SetBudget.this, "Budget set successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SetBudget.this, "Budget not set.", Toast.LENGTH_SHORT).show();
                    }

                    String budgetAmount = inputBudget.getText().toString();
                    HomeFragment.updateBudget(budgetAmount, SetBudget.this);

                    finish();

                } catch (NumberFormatException e) {
                    Toast.makeText(SetBudget.this, "Invalid amount. Please enter a valid number.", Toast.LENGTH_SHORT).show();

                } catch (IllegalArgumentException e) {
                    Toast.makeText(SetBudget.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
