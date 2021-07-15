package com.example.safeout.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.safeout.R;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class StatusFragment extends Fragment {

    private Button btnSafe;
    private Button btnAlert;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSafe = view.findViewById(R.id.btnSafe);
        btnAlert = view.findViewById(R.id.btnAlert);
        btnSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readObject();
            }
        });

        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateObject();
            }
        });
    }

    public void readObject() {
        // Database Class goes in getQuery
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");

        // objectId -> can be retrieved from ParseUser.getCurrentUser().getObjectId()
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
                object.put("currentStatus", "Safe");
                //All other fields will remain the same
                object.saveInBackground();
                Log.d("StatusFragment", object.get("currentStatus").toString());

            } else {
                // something went wrong
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("StatusFragment", object.getObjectId());
            }
        });
    }

    public void updateObject() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (object, e) -> {
            if (e == null) {
                object.put("currentStatus", "(undefined)");
                //All other fields will remain the same
                object.saveInBackground();
                Log.d("StatusFragment", object.get("currentStatus").toString());

            } else {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
