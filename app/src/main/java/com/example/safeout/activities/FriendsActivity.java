package com.example.safeout.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.safeout.R;
import com.example.safeout.adapters.FriendInformationAdapter;
import com.example.safeout.adapters.FriendRequestAdapter;
import com.example.safeout.models.FriendInformation;
import com.example.safeout.models.FriendRequest;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    public static final String TAG = "FriendsActivity";

    private RecyclerView rvSearchResults;
    private RecyclerView rvRequests;
    private RecyclerView rvFriends;

    public List<FriendInformation> information;
    public List<FriendRequest> requests;

    private FriendInformationAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // hide action bar (this only contains the name of the app)
        getSupportActionBar().hide();
        rvFriends = findViewById(R.id.rvFriends);
        rvRequests = findViewById(R.id.rvRequests);

        information = new ArrayList<>();
        requests = new ArrayList<>();

        friendsAdapter = new FriendInformationAdapter(this, information);

        // set the adapter on the recycler view
        rvFriends.setAdapter(friendsAdapter);
        // set the layout manager on the recycler view
        rvFriends.setLayoutManager(new LinearLayoutManager(this));
        // query friends from User
        try {
            queryFriends();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void queryFriends() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        Log.d(TAG, query.get(ParseUser.getCurrentUser().getObjectId()).get("friendsList").toString());
    }

    private void createFriends() {

    }

    // TODO: Move contacts and requests to different activities
}