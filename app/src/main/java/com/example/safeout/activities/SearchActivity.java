package com.example.safeout.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.example.safeout.R;
import com.example.safeout.adapters.SearchResultAdapter;
import com.example.safeout.models.SearchResult;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    public static final String TAG = "SearchActivity";

    private List<SearchResult> results;
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
        results = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        // query.selectKeys(Arrays.asList("username", "objectId"));
        ParseQuery<ParseObject> tests = query.whereContains("username", "ri");
        try {
            Log.d(TAG, String.valueOf(tests.count()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null ) {
                    Log.d(TAG, objects.size() + " users found");
                    for (int  i = 0; i < objects.size(); i++) {
                        Log.d(TAG, objects.get(i).get("profilePicture").toString());
                        results.add(new SearchResult(objects.get(i).get("username").toString()));
                    }
                }
            }
        });
        searchResultAdapter = new SearchResultAdapter(results);
        rvSearchResults.setAdapter(searchResultAdapter);

        // Here's where the search goes (when tests are complete
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchForUser(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query2 = intent.getStringExtra(SearchManager.QUERY);
            // searchForUser(query2);
        }
    }


    private void searchForUser(String query) {

    }

}