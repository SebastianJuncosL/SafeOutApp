package com.example.safeout.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.safeout.R;

public class AddFriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        // hide action bar (this only contains the name of the app)
        getSupportActionBar().hide();
    }
}