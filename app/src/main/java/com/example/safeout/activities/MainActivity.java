package com.example.safeout.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.safeout.R;
import com.example.safeout.fragments.MapFragment;
import com.example.safeout.fragments.StatusFragment;
import com.example.safeout.fragments.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // hide action bar (this only contains the name of the app)
        getSupportActionBar().hide();

        // Inflate navbar into main activity
        BottomNavigationView bottomNavigationView = findViewById(R.id.navBar);
        // Start home fragment when app opens up
        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new MapFragment()).commit();
        // switch case for switching between screens (fragments)
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.status:
                        selectedFragment = new StatusFragment();
                        break;
                    case R.id.map:
                        selectedFragment = new MapFragment();
                        break;
                    case R.id.user:
                        selectedFragment = new UserFragment();
                        break;
                }
                // Open selected fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, selectedFragment).commit();
                return true;
            }
        });

    }
}