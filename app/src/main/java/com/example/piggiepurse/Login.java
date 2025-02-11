package com.example.piggiepurse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    ImageButton returnbtn;
    LinearLayout text_signup;
    TextView textView_forgot;
    Button loginBtn;
    DBHelper DB;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        returnbtn =(ImageButton) findViewById(R.id.returnBtn);
        text_signup =(LinearLayout) findViewById(R.id.text_signup);
        textView_forgot = (TextView) findViewById(R.id.textView_forgot) ;
        loginBtn = (Button) findViewById(R.id.loginbtn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password_value);
        DB = new DBHelper(this);

        //direct user to signup/login page
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignUpLogin.class);
                startActivity(intent);
            }
        });

        //allow user to access login page via clicking the "Don’t have an account yet?" text
        text_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });

        //allow user to reset password if they forgot his/her password
        textView_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResetPassword.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener((v) -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            if (user.equals("") || pass.equals(""))
                Toast.makeText(Login.this, "Please enter all credentials", Toast.LENGTH_SHORT).show();
            else {
                Boolean checkuserpass = DB.checkusernamepassword(user, pass);
                if (checkuserpass) {
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    Cursor cursor = DB.getUserId(user, pass);
                    if (cursor.moveToFirst()) {
                        // Check if the cursor has any rows before accessing data
                        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("userid"));
                        HomeFragment.userid = id;

                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}