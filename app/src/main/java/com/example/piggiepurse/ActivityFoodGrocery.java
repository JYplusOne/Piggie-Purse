package com.example.piggiepurse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityFoodGrocery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_grocery);
        ImageButton dryFoodsImageButton = findViewById(R.id.dryfood);
        ImageButton greensImageButton = findViewById(R.id.greens);
        ImageButton backButton = findViewById(R.id.returnBtn);

        dryFoodsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityFoodGrocery.this, ActivityDrFoods.class);
                startActivity(intent);
            }
        });

        greensImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityFoodGrocery.this, ActivityGreens.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle back button click event
                onBackPressed();
            }
        });
    }


    //override onBackPressed method to define custom behavior
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}