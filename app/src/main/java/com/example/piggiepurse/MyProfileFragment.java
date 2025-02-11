package com.example.piggiepurse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MyProfileFragment extends Fragment {
    private ImageView profileImage;
    private EditText usernameEditText, passwordEditText, emailEditText;
    private Button logoutButton, deleteAccountButton;
    DBHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_profile_fragment, container, false);

        profileImage = view.findViewById(R.id.profileImage);

        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        emailEditText = view.findViewById(R.id.emailEditText);

        logoutButton = view.findViewById(R.id.logoutButton);
        deleteAccountButton = view.findViewById(R.id.deleteAccountButton);

        db = new DBHelper(requireContext());
        int userid = HomeFragment.userid;
        Cursor res = db.getUserDetails(userid);

        String username = null;
        String email = null;
        String password = null;

        while (res.moveToNext()) {
            username = res.getString(1);
            email = res.getString(2);
            password = res.getString(3);
        }

        usernameEditText.setText(username);
        emailEditText.setText(email);
        passwordEditText.setText(password);


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Confirm Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //if user select Yes, logout
                                Toast.makeText(requireContext(), "Logout Successful.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(requireContext(),SignUpLogin.class);
                                startActivity(intent);
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


        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete this account?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //if user select Yes, delete account, return to signuplogin
                                int userid = HomeFragment.userid;
                                boolean checkDeleteUser = db.deleteUser(userid);

                                if (checkDeleteUser) {
                                    Toast.makeText(requireContext(), "Account deleted successfully. Please sign up or login to an existing account", Toast.LENGTH_SHORT).show();
                                    HomeFragment.updateIncomeExpenseBudgetValues(requireContext());
                                } else {
                                    Toast.makeText(requireContext(), "Account not deleted.", Toast.LENGTH_SHORT).show();
                                }
                                Intent intent = new Intent(requireContext(),SignUpLogin.class);
                                startActivity(intent);
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
}
