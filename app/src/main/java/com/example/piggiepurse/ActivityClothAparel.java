package com.example.piggiepurse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityClothAparel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloth_aparel);
        ImageButton dailWearsImageButton = findViewById(R.id.wearables);
        ImageButton fashionAcessoriesProducImageButton = findViewById(R.id.accesories);
        ImageButton backButton = findViewById(R.id.returnBtn);

        dailWearsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityClothAparel.this, ActivityDailyWears.class);
                startActivity(intent);
            }
        });

        fashionAcessoriesProducImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityClothAparel.this, ActivityFashionAcessories.class);
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