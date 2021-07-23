package com.example.safeout.fragments;

import android.content.Intent;
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
import com.example.safeout.activities.FriendsActivity;
import com.example.safeout.activities.LoginActivity;
import com.example.safeout.activities.MainActivity;
import com.example.safeout.activities.SearchActivity;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class UserFragment extends Fragment {

    private Button btnLogout;
    private Button btnAddFriend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout = view.findViewById(R.id.btnLogOut);
        btnAddFriend = view.findViewById(R.id.svSearchUser);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStatus();
            }
        });

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFriendsActivity();
            }
        });
    }

    private void goToFriendsActivity() {
        Intent i = new Intent(getContext(), SearchActivity.class);
        startActivity(i);
    }

    private void logOut() {
        ParseUser.logOut();
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
        ((MainActivity)getActivity()).finish();
    }

    private void resetStatus() {
        // Database Class goes in getQuery
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        // objectId -> can be retrieved from ParseUser.getCurrentUser().getObjectId()
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
                ParseGeoPoint emptyLocation = new ParseGeoPoint();
                emptyLocation.setLatitude(0.0);
                emptyLocation.setLongitude(0.0);
                object.put("currentStatus", "(undefined)");
                // TODO: Uncomment location reset when logging out
                // object.put("currentLocation", emptyLocation);
                //All other fields will remain the same
                try {
                    object.save();
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
                Log.d("StatusFragment", object.get("currentStatus").toString());
            } else {
                // something went wrong
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (object.get("currentStatus") == "(undefined)") {
                logOut();
            }
        });
    }
}
