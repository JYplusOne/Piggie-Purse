package com.example.piggiepurse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class IncomeCategory extends AppCompatActivity {
    ImageButton returnBtn;
    List<LinearLayout> categoryLayouts = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_category);

        returnBtn = findViewById(R.id.returnBtn);

        categoryLayouts.add(findViewById(R.id.l_1));
        categoryLayouts.add(findViewById(R.id.l_2));
        categoryLayouts.add(findViewById(R.id.l_3));
        categoryLayouts.add(findViewById(R.id.l_4));
        categoryLayouts.add(findViewById(R.id.l_5));
        categoryLayouts.add(findViewById(R.id.l_6));
        categoryLayouts.add(findViewById(R.id.l_7));
        categoryLayouts.add(findViewById(R.id.l_8));
        categoryLayouts.add(findViewById(R.id.l_9));
        categoryLayouts.add(findViewById(R.id.l_10));

        //set on click listener for all category layouts
        for (int i = 0; i < categoryLayouts.size(); i++) {
            final int categoryIndex = i;
            categoryLayouts.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //pass category index to retrieve category name
                    String selectedCategory = getSelectedCategoryName(categoryIndex);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("selectedCategory", selectedCategory);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            });
        }

        //action for return button
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    //retrieve category name based on category index
    private String getSelectedCategoryName(int index) {
        switch (index) {
            case 0:
                return "Salary";
            case 1:
                return "Part-Time Job";
            case 2:
                return "Family Support";
            case 3:
                return "Lending & Renting";
            case 4:
                return "Interest, Dividends";
            case 5:
                return "Lottery";
            case 6:
                return "Refunds";
            case 7:
                return "Gifts";
            case 8:
                return "Others";
            case 9:
                return "Sales";
            default:
                return "Others...";
        }
    }
}
