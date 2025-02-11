package com.example.piggiepurse;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SummaryFragment extends Fragment {
    public static final String TAG = "SummaryFragment";
    private DBHelper dbHelper;
    private BarChart barChart;
    private PieChart pieChart;
    private PieChart incomePieChart;
    private Spinner dropDownDateRange;
    private long startDate = 0;
    private long endDate = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.summary_fragment, container, false);

        barChart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);
        incomePieChart = view.findViewById(R.id.incomePieChart);
        dropDownDateRange = view.findViewById(R.id.dropDownDateRange);

        dbHelper = new DBHelper(getActivity());

        //set listener for date range filter
        dropDownDateRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                if (i == 1) {
                    //set date range for the last 24 hours
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    startDate = calendar.getTimeInMillis();
                    endDate = startDate + 86400000; //24 hours in milliseconds

                } else if (i == 2) {
                    //set date range for the current week
                    calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    startDate = calendar.getTimeInMillis();

                    calendar.add(Calendar.DAY_OF_WEEK, 7);
                    endDate = calendar.getTimeInMillis();

                } else if (i == 3) {
                    //set date range for the current month
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    startDate = calendar.getTimeInMillis();

                    calendar.add(Calendar.MONTH, 1);
                    endDate = calendar.getTimeInMillis();

                } else {
                    //no filter selected, set startDate and endDate to 0
                    startDate = endDate = 0;
                }

                //log statements for debugging
                Log.d(TAG, "Selected filter: " + i);
                Log.d(TAG, "Start date: " + startDate + ", End date: " + endDate);

                //refresh fragment content based on the selected date range
                refreshFragmentContent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return view;
    }


    private void refreshFragmentContent() {
        int userid = HomeFragment.userid;
        //fetch data based on the selected date range
        Cursor cursor;
        if (startDate > 0 || endDate > 0) {
            cursor = dbHelper.getBalanceDataOrderedByDateDesc(userid, startDate, endDate);
        } else {
            cursor = dbHelper.getBalanceDataOrderedByDateDesc(userid);
        }

        //bar chart logic
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> barLabels = new ArrayList<>();

        float totalExpense = 0.0f;
        float totalIncome = 0.0f;
        float balance = 0.0f;

        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(2);
                float amount = Float.parseFloat(cursor.getString(3));

                if (type.equals("Expense")) {
                    totalExpense += amount;
                } else if (type.equals("Income")) {
                    totalIncome += amount;
                }
            } while (cursor.moveToNext());

            balance = totalIncome - totalExpense;
        }

        barEntries.add(new BarEntry(0, totalExpense, "Expense"));
        barEntries.add(new BarEntry(1, totalIncome, "Income"));
        barEntries.add(new BarEntry(2, balance, "Balance"));

        barLabels.add("Expense");
        barLabels.add("Income");
        barLabels.add("Balance");

        BarDataSet barDataSet = new BarDataSet(barEntries, " ");
        barDataSet.setColors(new int[]{getResources().getColor(R.color.expenseColor),
                getResources().getColor(R.color.incomeColor),
                getResources().getColor(R.color.budgetColor)});

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.getLegend().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(barLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        barChart.setDescription(null);

        barChart.invalidate();

        //pie chart for expenses
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        cursor.moveToFirst();

        HashMap<String, Float> expenseMap = new HashMap<>(); // to store the sum of expenses for each category

        do {
            String type = cursor.getString(2);
            if (type.equals("Expense")) {
                String category = cursor.getString(4);
                float amount = Float.parseFloat(cursor.getString(3));

                // Check if the category is already in the map
                if (expenseMap.containsKey(category)) {
                    // If yes, add the amount to the existing sum
                    expenseMap.put(category, expenseMap.get(category) + amount);
                } else {
                    // If no, add a new entry to the map
                    expenseMap.put(category, amount);
                }
            }
        } while (cursor.moveToNext());

        // Now, add the entries to the pie chart
        for (Map.Entry<String, Float> entry : expenseMap.entrySet()) {
            pieEntries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, " ");
        int[] customColors = new int[]{
                Color.parseColor("#fd6f7a"),
                Color.parseColor("#f7db91"),
                Color.parseColor("#ff963f"),
                Color.parseColor("#57a5ff"),
                Color.parseColor("#fdd5d7"),
                Color.parseColor("#fceed4"),
                Color.parseColor("#ffae6b"),
                Color.parseColor("#f5f5f5"),
                Color.parseColor("#fc5661"),
                Color.parseColor("#f3ba2d"),
                Color.parseColor("#ff8119"),
                Color.parseColor("#0077ff")
        };

        pieDataSet.setColors(customColors);
        pieDataSet.setValueTextColor(Color.BLACK); //set data label color to black
        pieDataSet.setValueTextSize(12f);
        pieChart.setDrawHoleEnabled(false); //disable the hole to make it a whole pie, not donut chart

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));

        pieChart.setDescription(null);

        pieChart.setData(pieData);
        pieChart.invalidate();

        // pie chart for Income
        ArrayList<PieEntry> incomePieEntries = new ArrayList<>();
        cursor.moveToFirst();

        HashMap<String, Float> incomeMap = new HashMap<>(); // to store the sum of income for each category

        do {
            String type = cursor.getString(2);
            if (type.equals("Income")) {
                String category = cursor.getString(4);
                float amount = Float.parseFloat(cursor.getString(3));

                // Check if the category is already in the map
                if (incomeMap.containsKey(category)) {
                    // If yes, add the amount to the existing sum
                    incomeMap.put(category, incomeMap.get(category) + amount);
                } else {
                    // If no, add a new entry to the map
                    incomeMap.put(category, amount);
                }
            }
        } while (cursor.moveToNext());

        // Now, add the entries to the income pie chart
        for (Map.Entry<String, Float> entry : incomeMap.entrySet()) {
            incomePieEntries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet incomePieDataSet = new PieDataSet(incomePieEntries, " ");
        incomePieDataSet.setColors(customColors);
        incomePieDataSet.setValueTextColor(Color.BLACK);
        incomePieDataSet.setValueTextSize(12f);

        PieData incomePieData = new PieData(incomePieDataSet);
        incomePieData.setValueFormatter(new PercentFormatter(incomePieChart));

        incomePieChart.setDrawHoleEnabled(false);
        incomePieChart.setDescription(null);

        incomePieChart.setData(incomePieData);
        incomePieChart.invalidate();
    }
}