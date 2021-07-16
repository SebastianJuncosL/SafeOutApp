package com.example.safeout.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.safeout.R;
import com.google.android.gms.maps.GoogleMap;

public class MapTestingActivity extends AppCompatActivity {

    public static final String TAG = "MapTestingActivity";
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_testing);
    }
}