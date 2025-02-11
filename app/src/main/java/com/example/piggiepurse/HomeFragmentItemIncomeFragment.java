package com.example.piggiepurse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragmentItemIncomeFragment extends Fragment {
    public static final String TAG = "HomeFragmentItemIncomeFragment";
    TextView itemCategory, itemDescription, itemAmount, itemTime, itemId, itemDate;
    ImageView itemIcon;
    Button deleteBtn;
    LinearLayout item;
    DBHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_item_income_fragment, container, false);

        itemCategory = view.findViewById(R.id.itemCategory);
        itemDescription = view.findViewById(R.id.itemDescription);
        itemAmount = view.findViewById(R.id.itemAmount);
        itemTime = view.findViewById(R.id.itemTime);
        itemIcon = view.findViewById(R.id.itemIcon);
        itemId = view.findViewById(R.id.itemId);
        itemDate = view.findViewById(R.id.itemDate);

        deleteBtn = view.findViewById(R.id.deleteBtn);
        item = view.findViewById(R.id.item);

        db = new DBHelper(requireContext());

        //retrieve data from arguments
        Bundle args = getArguments();
        if (args != null) {
            String id = args.getString("id");
            String type = args.getString("type");
            String amount = args.getString("amount");
            String category = args.getString("category");
            String date = args.getString("date");
            String time = args.getString("time");
            String description = args.getString("description");

            itemAmount.setText(amount);
            itemCategory.setText(category);
            itemTime.setText(time);
            itemDescription.setText(description);
            itemId.setText(id);
            itemDate.setText(date);

            setIconBasedOnCategory(category);
        }

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toggle visibility of deleteBtn
                if (deleteBtn.getVisibility() == View.VISIBLE) {
                    deleteBtn.setVisibility(View.GONE);
                } else {
                    deleteBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete this record?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //if user select Yes, delete item
                                String id = itemId.getText().toString();
                                int userid = HomeFragment.userid;
                                boolean checkDeleteData = db.deleteBalance(userid, Integer.parseInt(id));

                                if (checkDeleteData) {
                                    Toast.makeText(requireContext(), "Record deleted successfully.", Toast.LENGTH_SHORT).show();
                                    HomeFragment.updateIncomeExpenseBudgetValues(requireContext());
                                } else {
                                    Toast.makeText(requireContext(), "Record not deleted.", Toast.LENGTH_SHORT).show();
                                }

                                //remove item fragment
                                getParentFragmentManager().beginTransaction().remove(HomeFragmentItemIncomeFragment.this).commit();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //if user select No, close dialog
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public void setIconBasedOnCategory(String category) {
        int drawableId;

        switch (category) {
            case "Salary":
                drawableId = R.drawable.income_c_salary;
                break;
            case "Part-Time Job":
                drawableId = R.drawable.income_c_part_time_job;
                break;
            case "Family Support":
                drawableId = R.drawable.income_c_family_support;
                break;
            case "Lending & Renting":
                drawableId = R.drawable.income_c_lending_renting;
                break;
            case "Interest, Dividends":
                drawableId = R.drawable.income_c_interest_dividends;
                break;
            case "Lottery":
                drawableId = R.drawable.income_c_lottery;
                break;
            case "Refunds":
                drawableId = R.drawable.income_c_refunds;
                break;
            case "Gifts":
                drawableId = R.drawable.income_c_gifts;
                break;
            case "Sales":
                drawableId = R.drawable.income_c_sales;
                break;
            case "Others":
            default:
                drawableId = R.drawable.income_c_others;
                break;
        }
        itemIcon.setImageResource(drawableId);
    }

}
