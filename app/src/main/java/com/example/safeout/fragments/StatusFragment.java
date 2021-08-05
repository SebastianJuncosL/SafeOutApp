package com.example.safeout.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.safeout.R;
import com.example.safeout.activities.MainActivity;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class StatusFragment extends Fragment {

    private Button btnSafe;
    private Button btnAlert;
    private Button btnDanger;
    private RelativeLayout rlStatusFragmentBackground;

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
        btnDanger = view.findViewById(R.id.btnDanger);
        rlStatusFragmentBackground = view.findViewById(R.id.rlStatusFragmentBackground);

        btnSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSafe();
            }
        });
        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlert();
            }
        });
        btnDanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDanger();
            }
        });

    }

    public void setSafe() {
        doAnimation(getResources().getColor(R.color.safeGreen));

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
            }
        });
    }

    public void setAlert() {
        doAnimation(getResources().getColor(R.color.alertOrange));

        // Database Class goes in getQuery
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        // objectId -> can be retrieved from ParseUser.getCurrentUser().getObjectId()
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (object, e) -> {
            if (e == null) {
                object.put("currentStatus", "Alert");
                //All other fields will remain the same
                object.saveInBackground();
                Log.d("StatusFragment", object.get("currentStatus").toString());
            } else {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDanger() {
        doAnimation(getResources().getColor(R.color.dangerRed));
        // Database Class goes in getQuery
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        // objectId -> can be retrieved from ParseUser.getCurrentUser().getObjectId()
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
                object.put("currentStatus", "Danger");
                //All other fields will remain the same
                object.saveInBackground();
                Log.d("StatusFragment", object.get("currentStatus").toString());
            } else {
                // something went wrong
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doAnimation(int color) {
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        rlStatusFragmentBackground.startAnimation(fadeIn);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rlStatusFragmentBackground.setBackgroundColor(color);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                rlStatusFragmentBackground.startAnimation(fadeOut);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlStatusFragmentBackground.setBackgroundColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    // TODO: check if there's an active location before letting the user change it's status
}
