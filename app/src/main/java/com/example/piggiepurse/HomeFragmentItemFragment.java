package com.example.piggiepurse;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragmentItemFragment extends Fragment {
    public static final String TAG = "HomeFragmentItemFragment";
    LinearLayout itemListLayout;
    static LinearLayout innerLinear4;
    DBHelper db;

    private final long startDate;
    private final long endDate;

    public HomeFragmentItemFragment(long startDate, long endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_item_fragment, container, false);
        db = new DBHelper(requireContext());
        int userid = HomeFragment.userid;
        Cursor res = db.getBalanceDataOrderedByDateDesc(userid, startDate, endDate);
        if (res.getCount() == 0) {
            Toast.makeText(requireContext(), "No entry exists", Toast.LENGTH_SHORT).show();
        }

        while (res.moveToNext()) {
            String id = res.getString(0);
            String type = res.getString(2);
            String amount = res.getString(3);
            String category = res.getString(4);
            String date = res.getString(5);
            String time = res.getString(6);
            String description = res.getString(7);

            if (type.equals("Income")) {
                addIncomeItemFragment(id, type, amount, category, date, time, description);
            } else if (type.equals("Expense")) {
                addExpenseItemFragment(id, type, amount, category, date, time, description);
            }

        }
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemListLayout = view.findViewById(R.id.itemListLayout);
        innerLinear4 = view.findViewById(R.id.innerLinear4);
    }


    public void addIncomeItemFragment(String id, String type, String amount, String category, String date, String time, String description) {
        //create instance of income item fragment
        HomeFragmentItemIncomeFragment incomeItem = new HomeFragmentItemIncomeFragment();

        //pass data as arguments to income item fragment
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("type", type);
        args.putString("amount", amount);
        args.putString("category", category);
        args.putString("date", date);
        args.putString("time", time);
        args.putString("description", description);
        incomeItem.setArguments(args);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        //add income item fragment to item fragment container
        transaction.add(R.id.innerLinear4, incomeItem, "HomeFragmentItemIncomeFragment");
        transaction.commit();

    }


    public void addExpenseItemFragment(String id, String type, String amount, String category, String date, String time, String description) {
        //create instance of expense item fragment
        HomeFragmentItemExpenseFragment expenseItem = new HomeFragmentItemExpenseFragment();

        //pass data as arguments to expense item fragment
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("type", type);
        args.putString("amount", amount);
        args.putString("category", category);
        args.putString("date", date);
        args.putString("time", time);
        args.putString("description", description);
        expenseItem.setArguments(args);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        //add expense item fragment to item fragment container
        transaction.add(R.id.innerLinear4, expenseItem, "HomeFragmentItemExpenseFragment");
        transaction.commit();
    }
}
