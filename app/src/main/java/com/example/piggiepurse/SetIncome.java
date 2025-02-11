package com.example.piggiepurse;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class SetIncome extends AppCompatActivity {
    EditText inputIncome, incomeDescription;
    LinearLayout dateSelectArea, timeSelectArea, categorySelectArea;
    ImageButton returnBtn;
    Button continueBtn;
    TextView timeSelect, dateSelect, categorySelect;
    int timeHour = -1;
    int timeMinute = -1;
    int dateYear = -1;
    int dateMonth = -1;
    int dateDay = -1;
    DBHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_income);

        inputIncome = findViewById(R.id.inputIncome);

        categorySelectArea = findViewById(R.id.linear3);
        dateSelectArea = findViewById(R.id.innerLinear3);
        timeSelectArea = findViewById(R.id.innerLinear4);

        categorySelect = findViewById(R.id.categorySelect);
        dateSelect = findViewById(R.id.dateSelect);
        timeSelect = findViewById(R.id.timeSelect);
        incomeDescription = findViewById(R.id.incomeDescription);

        returnBtn = findViewById(R.id.returnBtn);
        continueBtn = findViewById(R.id.continueBtn);

        db = new DBHelper(this);

        //action for select income category
        categorySelectArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), IncomeCategory.class);
                //pass currently selected category using extra parameter
                intent.putExtra("selectedCategory", categorySelect.getText().toString());
                startActivityForResult(intent, 1); // Request code can be any unique integer
            }
        });

        //action for select date
        dateSelectArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get current date
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                //create DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(SetIncome.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        //update selected date
                        dateYear = year;
                        dateMonth = month;
                        dateDay = day;

                        //store and display selected date
                        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", month + 1, day, year);
                        dateSelect.setText(selectedDate);
                    }
                }, dateYear != -1 ? dateYear : currentYear,
                        dateMonth != -1 ? dateMonth : currentMonth,
                        dateDay != -1 ? dateDay : currentDay
                        //use stored date or current date if not set
                );

                //set transparent background
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                //show DatePickerDialog
                datePickerDialog.show();
            }
        });


        //action for select time
        timeSelectArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get current time
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                //create TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(SetIncome.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        //update selected time
                        timeHour = hour;
                        timeMinute = minute;

                        //store and display selected time
                        String time;
                        if (hour >= 12) {
                            if (hour > 12) {
                                hour -= 12;
                            }
                            time = String.format(Locale.getDefault(), "%2d : %02d PM", hour, minute);
                        } else {
                            if (hour == 0) {
                                hour = 12;
                            }
                            time = String.format(Locale.getDefault(), "%2d : %02d AM", hour, minute);
                        }
                        timeSelect.setText(time);
                    }
                }, timeHour != -1 ? timeHour : currentHour,
                        timeMinute != -1 ? timeMinute : currentMinute, false
                        //use stored time or current time if not set
                );

                //set transparent background
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                //show TimePickerDialog
                timePickerDialog.show();
            }
        });

        //action for return button
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //action for continue button (INSERT NEW INCOME DATA)
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    float floatAmount = Float.parseFloat(inputIncome.getText().toString());

                    int userid = HomeFragment.userid;
                    String type = "Income";
                    String amount = String.format("%.2f", floatAmount);
                    String category = categorySelect.getText().toString();
                    String date = dateSelect.getText().toString();
                    String time = timeSelect.getText().toString();
                    String description = incomeDescription.getText().toString();

                    if (category.equals("Click to select") || category.equals("")) {
                        throw new IllegalArgumentException("Category cannot be empty");
                    }
                    if (date.equals("Click to select") || date.equals("")) {
                        throw new IllegalArgumentException("Date cannot be empty");
                    }
                    if (time.equals("Click to select") || time.equals("")) {
                        throw new IllegalArgumentException("Time cannot be empty");
                    }
                    if (floatAmount == 0.00) {
                        throw new IllegalArgumentException("Amount cannot be zero");
                    }

                    Boolean checkInsertData = db.insertBalance(userid, type, amount, category, date, time, description);

                    if (checkInsertData) {
                        Toast.makeText(SetIncome.this, "New income record inserted successfully.", Toast.LENGTH_SHORT).show();

                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();

                    } else {
                        Toast.makeText(SetIncome.this, "Income record not inserted.", Toast.LENGTH_SHORT).show();

                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(SetIncome.this, "Invalid amount. Please enter a valid number.", Toast.LENGTH_SHORT).show();

                } catch (IllegalArgumentException e) {
                    Toast.makeText(SetIncome.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            //check if result is from IncomeCategory activity
            String selectedCategory = data.getStringExtra("selectedCategory");
            categorySelect.setText(selectedCategory);
        }
    }
}
