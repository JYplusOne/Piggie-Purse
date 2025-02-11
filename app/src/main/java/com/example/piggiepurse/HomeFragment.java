package com.example.piggiepurse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";
    private static final int REQUEST_CODE_SET_INCOME_EXPENSE = 1;
    public static int userid = 1;

    ImageButton viewProfile, addNewBtn, addIncomeBtn, addExpenseBtn;
    LinearLayout budgetSetArea;
    FrameLayout itemFragmentContainer;
    static LinearLayout budgetHiddenText, budgetProgressBarArea;
    static TextView budgetSet, budgetValueNumber, remainingBudgetValueNumber, incomeMyrValue, expenseMyrValue, balanceMyrValue;
    static ProgressBar budgetProgressBar;
    static DBHelper db;

    private boolean rotated = false;
    private static AlertDialog alertDialog;

    ImageView datePicker;
    int dateYear = -1;
    int dateMonth = -1;
    int dateDay = -1;

    Spinner dropDownDateRange;
    String selectedDatePickerDate;
    private static long startDate = 0;
    private static long endDate = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewProfile = view.findViewById(R.id.viewProfile);

        budgetSetArea = view.findViewById(R.id.linear3);

        addNewBtn = view.findViewById(R.id.addNewBtn);
        addIncomeBtn = view.findViewById(R.id.addIncomeBtn);
        addExpenseBtn = view.findViewById(R.id.addExpenseBtn);

        budgetSet = view.findViewById(R.id.budgetSet);
        budgetValueNumber = view.findViewById(R.id.budgetValueNumber);
        remainingBudgetValueNumber = view.findViewById(R.id.remainingBudgetValueNumber);
        budgetProgressBarArea = view.findViewById(R.id.inner2Linear3);
        budgetHiddenText = view.findViewById(R.id.inner2Linear4);
        budgetProgressBar = view.findViewById(R.id.budgetProgressBar);

        itemFragmentContainer = view.findViewById(R.id.itemFragmentContainer);

        incomeMyrValue = view.findViewById(R.id.incomeMyrValue);
        expenseMyrValue = view.findViewById(R.id.expenseMyrValue);
        balanceMyrValue = view.findViewById(R.id.balanceMyrValue);

        datePicker = view.findViewById(R.id.datePicker);
        dropDownDateRange = view.findViewById(R.id.dropDownDateRange);

        refreshFragmentContent();

        //link to view profile fragment
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomePage.bottomNavigationBar.setSelectedItemId(R.id.nav_my_profile);
            }
        });

        //link to view profile fragment
        budgetSetArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SetBudget.class);
                startActivity(intent);
            }
        });

        //yellow main add button
        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //rotated to set to false by default (when declared)
                //toggle rotation state (changing from true to false, or false to true)
                rotated = !rotated;

                //rotate button by 45 degrees (clockwise if rotated is true, anticlockwise if rotated is false)
                float rotation = rotated ? 45 : 0;
                view.animate().rotation(rotation).start();

                //toggle visibility of addIncomeBtn and addExpenseBtn
                if (addIncomeBtn.getVisibility() == view.VISIBLE) {
                    //change to invisible if it is currently visible
                    addIncomeBtn.setVisibility(view.INVISIBLE);
                    addExpenseBtn.setVisibility(view.INVISIBLE);
                } else {
                    //change to visible if it is currently invisible
                    addIncomeBtn.setVisibility(view.VISIBLE);
                    addExpenseBtn.setVisibility(view.VISIBLE);
                }
            }
        });

        //blue add income button
        addIncomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), SetIncome.class), REQUEST_CODE_SET_INCOME_EXPENSE);
            }
        });

        //red add expense button
        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), SetExpense.class), REQUEST_CODE_SET_INCOME_EXPENSE);
            }
        });

        //date picker icon
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get current date
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                //create DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        //update selected date
                        dateYear = year;
                        dateMonth = month;
                        dateDay = day;

                        //store and display selected date
                        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", month + 1, day, year);
                        selectedDatePickerDate = selectedDate;
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

        //select date range drop down
        dropDownDateRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                if (i == 1) {
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    startDate = calendar.getTimeInMillis();
                    endDate = startDate + 86400000;
                } else if (i == 2) {
                    calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    startDate = calendar.getTimeInMillis();

                    calendar.add(Calendar.DAY_OF_WEEK, 7);
                    endDate = calendar.getTimeInMillis();
                } else if (i == 3) {
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    startDate = calendar.getTimeInMillis();

                    calendar.add(Calendar.MONTH, 1);
                    endDate = calendar.getTimeInMillis();
                } else {
                    startDate = endDate = 0;
                }

                refreshFragmentContent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    public static void updateBudget(String budget, Context context) {
        //update budget value
        float floatBudget = Float.parseFloat(budget);
        String amount = String.format("%.2f", floatBudget);

        budgetValueNumber.setText(amount);

        //calculate and update remaining value
        String remaining = calculateRemaining(budget);
        remainingBudgetValueNumber.setText(remaining);

        //set visibility of budgetValueNumber and remainingBudgetValueNumber
        //set visibility of budgetSet and budgetProgressBarArea
        if (!budgetValueNumber.getText().toString().equals("0") && !budgetValueNumber.getText().toString().equals("")) {
            budgetHiddenText.setVisibility(View.VISIBLE);
            budgetSet.setVisibility(View.GONE);
            budgetProgressBarArea.setVisibility(View.VISIBLE);
        } else {
            budgetHiddenText.setVisibility(View.INVISIBLE);
            budgetSet.setVisibility(View.VISIBLE);
            budgetProgressBarArea.setVisibility(View.GONE);
        }

        // calculate and update progress bar
        int progressValue = calculateProgressValue(budget, remaining, context);
        budgetProgressBar.setProgress(progressValue);
    }

    public static String calculateRemaining(String budget) {
        float budgetFloat = Float.parseFloat(budget);
        float expense = Float.parseFloat(expenseMyrValue.getText().toString());

        float remain = budgetFloat - expense;
        String remaining = String.format("%.2f", remain);

        return remaining;
    }

    public static int calculateProgressValue(String budget, String remaining, Context context) {
        float budgetFloat = Float.parseFloat(budget);
        float remainFloat = Float.parseFloat(remaining);

        int progress = (int) ((budgetFloat - remainFloat) / budgetFloat * 100);

        int userid = HomeFragment.userid;
        Cursor cursor2 = db.getBudgetData(userid);
        if (cursor2.moveToFirst()) {
            @SuppressLint("Range") String latestToggle = cursor2.getString(cursor2.getColumnIndex("toggle"));

            //set alert to only appear if the notify is toggled
            if (latestToggle.equals("true")) {
                if (progress >= 90 && progress < 100) {
                    showAlert(context, "approach");
                } else if (progress >= 100) {
                    showAlert(context, "exceed");
                }
            }
        }
        return progress;
    }

    private static void showAlert(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (message.equals("approach")) {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_budget_approach, null);
            builder.setView(dialogView);

            Button notedButton = dialogView.findViewById(R.id.notedBtn);
            notedButton.setOnClickListener(v -> {
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            });

            alertDialog = builder.create();
            alertDialog.show();

        } else if (message.equals("exceed")) {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_budget_exceed, null);
            builder.setView(dialogView);

            Button notedButton = dialogView.findViewById(R.id.notedBtn);
            notedButton.setOnClickListener(v -> {
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            });

            alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SET_INCOME_EXPENSE && resultCode == Activity.RESULT_OK) {
            //refresh HomeFragment when SetExpense activity returns
            refreshFragmentContent();
        }
    }

    //refresh HomeFragment content
    private void refreshFragmentContent() {
        db = new DBHelper(requireContext());

        int userid = HomeFragment.userid;
        //handling income and expense
        //income and expense item thumbnails sorted by date and time in descending order (display latest at top)
        Cursor cursor = db.getBalanceDataOrderedByDateDesc(userid, startDate, endDate);

        if (cursor.getCount() > 0) {
            //if database is not empty, set initial fragment as HomeFragmentItemFragment
            getChildFragmentManager().beginTransaction().replace(R.id.itemFragmentContainer, new HomeFragmentItemFragment(startDate, endDate)).commit();
        } else {
            //if database is empty, set initial fragment as HomeFragmentNoItemFragment
            getChildFragmentManager().beginTransaction().replace(R.id.itemFragmentContainer, new HomeFragmentNoItemFragment()).commit();
        }

        float totalIncome = 0.0f;
        float totalExpense = 0.0f;

        //calculate expense
        //iterate through records in database
        while (cursor.moveToNext()) {
            String type = cursor.getString(2);
            String amount = cursor.getString(3);
            String category = cursor.getString(4);
            String date = cursor.getString(5);
            String time = cursor.getString(6);
            String description = cursor.getString(7);

            if (type.equals("Income")) {
                float incomeAmount = Float.parseFloat(amount);
                totalIncome += incomeAmount;

            } else if (type.equals("Expense")) {
                float expenseAmount = Float.parseFloat(amount);
                totalExpense += expenseAmount;
            }
        }

        //format totalIncome to 2 decimal values, set text of incomeMyrValue
        String formattedTotalIncome = String.format(Locale.getDefault(), "%.2f", totalIncome);
        incomeMyrValue.setText(formattedTotalIncome);

        //format totalExpense to 2 decimal values, set text of expenseMyrValue
        String formattedTotalExpense = String.format(Locale.getDefault(), "%.2f", totalExpense);
        expenseMyrValue.setText(formattedTotalExpense);

        //calculate balance
        String formattedTotalBalance = String.valueOf(Float.parseFloat(formattedTotalIncome) - Float.parseFloat(formattedTotalExpense));
        formattedTotalBalance = String.format(Locale.getDefault(), "%.2f", Float.parseFloat(formattedTotalBalance));
        balanceMyrValue.setText(formattedTotalBalance);

        cursor.close();


        //handling budget and progress bar
        Cursor cursor2 = db.getBudgetData(userid);
        if (cursor2.moveToFirst()) {
            @SuppressLint("Range") int id = cursor2.getInt(cursor2.getColumnIndex("id"));
            @SuppressLint("Range") String latestBudget = cursor2.getString(cursor2.getColumnIndex("budget"));
            @SuppressLint("Range") String latestDescription = cursor2.getString(cursor2.getColumnIndex("description"));
            @SuppressLint("Range") String latestToggle = cursor2.getString(cursor2.getColumnIndex("toggle"));

            budgetValueNumber.setText(latestBudget);

            String remaining = calculateRemaining(latestBudget);
            remainingBudgetValueNumber.setText(remaining);

            //set visibility of budgetValueNumber and remainingBudgetValueNumber
            //set visibility of budgetSet and budgetProgressBarArea
            if (!budgetValueNumber.getText().toString().equals("0")&&!budgetValueNumber.getText().toString().equals("")) {
                budgetHiddenText.setVisibility(View.VISIBLE);
                budgetSet.setVisibility(View.GONE);
                budgetProgressBarArea.setVisibility(View.VISIBLE);
            } else {
                budgetHiddenText.setVisibility(View.INVISIBLE);
                budgetSet.setVisibility(View.VISIBLE);
                budgetProgressBarArea.setVisibility(View.GONE);
            }

            //calculate and update progress bar
            int progressValue = calculateProgressValue(latestBudget, remaining, requireContext());
            budgetProgressBar.setProgress(progressValue);
        }
        cursor2.close();

        //set visibility of budgetValueNumber and remainingBudgetValueNumber
        //set visibility of budgetSet and budgetProgressBarArea
        if (!budgetValueNumber.getText().toString().equals("0")&&!budgetValueNumber.getText().toString().equals("")) {
            budgetHiddenText.setVisibility(View.VISIBLE);
            budgetSet.setVisibility(View.GONE);
            budgetProgressBarArea.setVisibility(View.VISIBLE);
        } else {
            budgetHiddenText.setVisibility(View.INVISIBLE);
            budgetSet.setVisibility(View.VISIBLE);
            budgetProgressBarArea.setVisibility(View.GONE);
        }
    }


    //update income, expense, budget values and progress bar after a record is deleted
    public static void updateIncomeExpenseBudgetValues(Context context) {
        //handling income and expense
        int userid = HomeFragment.userid;
        Cursor cursor3 = db.getBalanceDataOrderedByDateDesc(userid, startDate, endDate);
        float totalIncome = 0.0f;
        float totalExpense = 0.0f;

        //calculate expense
        //iterate through records in database
        while (cursor3.moveToNext()) {
            String type = cursor3.getString(2);
            String amount = cursor3.getString(3);
            String category = cursor3.getString(4);
            String date = cursor3.getString(5);
            String time = cursor3.getString(6);
            String description = cursor3.getString(7);

            if (type.equals("Income")) {
                float incomeAmount = Float.parseFloat(amount);
                totalIncome += incomeAmount;

            } else if (type.equals("Expense")) {
                float expenseAmount = Float.parseFloat(amount);
                totalExpense += expenseAmount;
            }
        }

        //format totalIncome to 2 decimal values, set text of incomeMyrValue
        String formattedTotalIncome = String.format(Locale.getDefault(), "%.2f", totalIncome);
        incomeMyrValue.setText(formattedTotalIncome);

        //format totalExpense to 2 decimal values, set text of expenseMyrValue
        String formattedTotalExpense = String.format(Locale.getDefault(), "%.2f", totalExpense);
        expenseMyrValue.setText(formattedTotalExpense);

        //calculate balance
        String formattedTotalBalance = String.valueOf(Float.parseFloat(formattedTotalIncome) - Float.parseFloat(formattedTotalExpense));
        formattedTotalBalance = String.format(Locale.getDefault(), "%.2f", Float.parseFloat(formattedTotalBalance));
        balanceMyrValue.setText(formattedTotalBalance);

        cursor3.close();


        //handling budget and progress bar
        Cursor cursor4 = db.getBudgetData(userid);
        if (cursor4.moveToFirst()) {
            @SuppressLint("Range") int id = cursor4.getInt(cursor4.getColumnIndex("id"));
            @SuppressLint("Range") String latestBudget = cursor4.getString(cursor4.getColumnIndex("budget"));
            @SuppressLint("Range") String latestDescription = cursor4.getString(cursor4.getColumnIndex("description"));
            @SuppressLint("Range") String latestToggle = cursor4.getString(cursor4.getColumnIndex("toggle"));

            budgetValueNumber.setText(latestBudget);

            String remaining = calculateRemaining(latestBudget);
            remainingBudgetValueNumber.setText(remaining);

            //set visibility of budgetValueNumber and remainingBudgetValueNumber
            //set visibility of budgetSet and budgetProgressBarArea
            if (!budgetValueNumber.getText().toString().equals("0")&&!budgetValueNumber.getText().toString().equals("")) {
                budgetHiddenText.setVisibility(View.VISIBLE);
                budgetSet.setVisibility(View.GONE);
                budgetProgressBarArea.setVisibility(View.VISIBLE);
            } else {
                budgetHiddenText.setVisibility(View.INVISIBLE);
                budgetSet.setVisibility(View.VISIBLE);
                budgetProgressBarArea.setVisibility(View.GONE);
            }

            //calculate and update progress bar
            int progressValue = calculateProgressValue(latestBudget, remaining, context);
            budgetProgressBar.setProgress(progressValue);
        }

        cursor4.close();
    }
}