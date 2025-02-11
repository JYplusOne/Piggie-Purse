package com.example.piggiepurse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityFootWear extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_wear);
        ImageButton sandalImageButton = findViewById(R.id.sandal);
        ImageButton cariumaImageButton = findViewById(R.id.cariuma);
        ImageButton backButton = findViewById(R.id.returnBtn);

        sandalImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityFootWear.this, ActivitySandal.class);
                startActivity(intent);
            }
        });

        cariumaImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityFootWear.this, ActivityCariuma.class);
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