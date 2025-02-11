package com.example.piggiepurse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {
    ImageButton returnbtn;
    LinearLayout text_signup;
    Button signupbtn;
    DBHelper DB;
    EditText username, email, password, rePassword;

    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        returnbtn = findViewById(R.id.returnBtn);
        text_signup = findViewById(R.id.text_signup);
        signupbtn = findViewById(R.id.signupbtn);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password_value);
        rePassword = findViewById(R.id.repassword_value);

        DB = new DBHelper(this);

        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignUpLogin.class);
                startActivity(intent);
            }
        });

        text_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        signupbtn.setOnClickListener((v) -> {
            String user = username.getText().toString();
            String email_add = email.getText().toString();
            String pass = password.getText().toString();
            String repass = rePassword.getText().toString();

            if (user.equals("")||pass.equals("")||repass.equals(""))
                Toast.makeText(SignUp.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            else {
                if (pass.equals(repass)) {
                    Boolean checkuser = DB.checkUser1(user, email_add);
                    if(checkuser == false) {
                        Boolean insert = DB.insertUser(user, email_add, pass);
                        if (insert == true) {
                            Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent (getApplicationContext(), Login.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignUp.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(SignUp.this, "User already exists. Please go for Sign In", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SignUp.this, "Password does not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
