package com.example.safeout.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.example.safeout.R;
import com.example.safeout.adapters.FriendRequestAdapter;
import com.example.safeout.models.FriendInformation;
import com.example.safeout.models.FriendRequest;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    public static final String TAG = "FriendsActivity";

    private RecyclerView rvSearchResults;
    private RecyclerView rvRequests;
    private RecyclerView rvFriends;

    public List<FriendInformation> information;
    public List<FriendRequest> requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // hide action bar (this only contains the name of the app)
        getSupportActionBar().hide();

        information = new ArrayList<>();
        requests = new ArrayList<>();
    }
}