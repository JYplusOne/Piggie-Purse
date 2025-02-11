package com.example.piggiepurse;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {
    public static BottomNavigationView bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        bottomNavigationBar = findViewById(R.id.bottomNavigationBar);

        //set initial fragment as HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();

        //change fragment in bottom navigation bar
        bottomNavigationBar.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                switchToFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_summary) {
                switchToFragment(new SummaryFragment());
                return true;
            } else if (itemId == R.id.nav_shop_assistant) {
                switchToFragment(new ShopAssistantFragment());
                return true;
            } else if (itemId == R.id.nav_my_profile) {
                switchToFragment(new MyProfileFragment());
                return true;
            } else {
                return false;
            }
        });
    }


    //switch fragment depending on selected item in bottom navigation bar
    private void switchToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
