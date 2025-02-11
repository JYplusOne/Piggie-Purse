package com.example.piggiepurse;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityDeodorant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deodorant);
        ImageButton backButton = findViewById(R.id.returnBtn);
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