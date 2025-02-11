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

public class HomeFragmentItemExpenseFragment extends Fragment {
    public static final String TAG = "HomeFragmentItemExpenseFragment";
    TextView itemCategory, itemDescription, itemAmount, itemTime, itemId, itemDate;
    ImageView itemIcon;
    Button deleteBtn;
    LinearLayout item;
    DBHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_item_expense_fragment, container, false);

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
                                getParentFragmentManager().beginTransaction().remove(HomeFragmentItemExpenseFragment.this).commit();
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
            case "Food & Dining":
                drawableId = R.drawable.expense_c_food_dining;
                break;
            case "Transportation":
                drawableId = R.drawable.expense_c_transportation;
                break;
            case "Housing / Rent":
                drawableId = R.drawable.expense_c_housing_rent;
                break;
            case "Utilities":
                drawableId = R.drawable.expense_c_utilities;
                break;
            case "Entertainment":
                drawableId = R.drawable.expense_c_entertainment;
                break;
            case "Groceries":
                drawableId = R.drawable.expense_c_groceries;
                break;
            case "Education":
                drawableId = R.drawable.expense_c_education;
                break;
            case "Travel":
                drawableId = R.drawable.expense_c_travel;
                break;
            case "Health & Fitness":
                drawableId = R.drawable.expense_c_health_fitness;
                break;
            case "Personal Care":
                drawableId = R.drawable.expense_c_personal_care;
                break;
            case "Gifts & Donations":
                drawableId = R.drawable.expense_c_gifts_donation;
                break;
            case "Others":
            default:
                drawableId = R.drawable.expense_c_others;
                break;
        }
        itemIcon.setImageResource(drawableId);
    }
}
