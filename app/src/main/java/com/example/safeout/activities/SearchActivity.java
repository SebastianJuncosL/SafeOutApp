package com.example.safeout.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.example.safeout.R;
import com.example.safeout.adapters.SearchResultAdapter;
import com.example.safeout.models.SearchResult;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    public static final String TAG = "SearchActivity";

    private List<ParseUser> results;
    private RecyclerView rvSearchResults;
    private SearchView searchView;
    private SearchResultAdapter searchResultAdapter;

    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().hide();

        rvSearchResults = findViewById(R.id.rvSearchResults);
        searchView = findViewById(R.id.svSearchUser);
        layoutManager = new LinearLayoutManager(this);
        rvSearchResults.setLayoutManager(layoutManager);
        results = new ArrayList<ParseUser>();

        searchResultAdapter = new SearchResultAdapter(results, this);
        rvSearchResults.setAdapter(searchResultAdapter);

        // Here's where the search goes (when tests are complete
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "Search is: " + query);
                // searchingTest(query);
                searchForUser(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    private void searchForUser(String query) {
        results.clear();
        searchResultAdapter.notifyDataSetChanged();
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseUser> dbQuery = ParseQuery.getQuery("_User");
        dbQuery.whereContains("username", query);
        dbQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, e.getMessage());
                    return;
                }
                Log.d(TAG, String.valueOf(objects.size()));
                results.addAll(objects);
                searchResultAdapter.notifyDataSetChanged();
            }
        });
        // dbQuery.selectKeys(Arrays.asList("username", "profilePicture")).whereContains("username", query);
        // List<SearchResult> res = null;
        // try {
        //    res = dbQuery.find();
        // } catch (ParseException e) {
        //     e.printStackTrace();
        // }
        // for (int i = 0; i < res.size(); i++) {
        //     results.add(new SearchResult(res.get(i).getUserName(), res.get(i).getProfilePicture().toString()));
        // }
        // searchResultAdapter.notifyDataSetChanged();

    }

    public void searchingTest(String search) {
        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereContains("username", search);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, e.getMessage());
                    return;
                }
                Log.d(TAG, "Got " + String.valueOf(objects.size()) + " matches");

                results.addAll(objects);
                searchResultAdapter.notifyDataSetChanged();
            }
        });
    }
}