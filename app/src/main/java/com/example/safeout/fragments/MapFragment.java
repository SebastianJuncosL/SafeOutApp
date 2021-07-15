package com.example.safeout.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.safeout.R;
import com.example.safeout.activities.MapTestingActivity;

public class MapFragment extends Fragment {

    private Button btnGoToMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnGoToMap = view.findViewById(R.id.btnGoToMap);
        btnGoToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMap();
            }
        });

    }

    private void goToMap() {
        Intent i = new Intent(getContext(), MapTestingActivity.class);
        startActivity(i);
    }
}
