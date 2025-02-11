package com.example.piggiepurse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityPersonalCare extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_care);
        ImageButton deodorantImageButton = findViewById(R.id.deodorant);
        ImageButton soapImageButton = findViewById(R.id.soap);
        ImageButton facialCareProducImageButton = findViewById(R.id.skinwash);
        ImageButton backButton = findViewById(R.id.returnBtn);

        deodorantImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityPersonalCare.this, ActivityDeodorant.class);
                startActivity(intent);
            }
        });

        soapImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityPersonalCare.this, ActivitySoaps.class);
                startActivity(intent);
            }
        });

        facialCareProducImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityPersonalCare.this, ActivityFacialCareProduct.class);
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