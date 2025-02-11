package com.example.piggiepurse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPassword extends AppCompatActivity {
    ImageButton returnBtn;
    Button resetBtn, backloginBtn;
    EditText username, email, password, rePassword;
    DBHelper DB;

    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpassword);

        returnBtn = findViewById(R.id.returnBtn);
        resetBtn = findViewById(R.id.resetpasswordbtn);
        backloginBtn = findViewById(R.id.backtologinbtn);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.newpassword_value);
        rePassword = findViewById(R.id.repeatnewpassword_value);

        DB = new DBHelper(this);

        //arrow button that redirects user to login page
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });

        //back to login button that redirects user to login page
        backloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String email_add = email.getText().toString();
                String pass = password.getText().toString();
                String repass = rePassword.getText().toString();

                if (user.equals("")||pass.equals("")||repass.equals(""))
                    Toast.makeText(ResetPassword.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else {
                    if (pass.equals(repass)) {
                        Boolean checkuser = DB.checkUser2(user, email_add);
                        if(checkuser == true) {
                            Boolean update = DB.updateUser(user, email_add, pass);
                            if (update == true) {
                                Toast.makeText(ResetPassword.this, "Reset password Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent (getApplicationContext(), Login.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ResetPassword.this, "Reset password Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(ResetPassword.this, "Invalid username or email", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(ResetPassword.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
