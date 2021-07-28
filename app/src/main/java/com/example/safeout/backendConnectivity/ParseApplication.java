package com.example.safeout.backendConnectivity;

import android.app.Application;

import com.example.safeout.models.FriendInformation;
import com.example.safeout.models.FriendRequest;
import com.example.safeout.models.SearchResult;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(FriendInformation.class);
        ParseObject.registerSubclass(FriendRequest.class);

        // Initializes Parse SDK as soon as the application is created
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("9RDOwmO8ezR73qpVn1zczltOaSDCWcV3niedkwZr")
                .clientKey("1IaHvBgugXVtCplseqlxEn4RFCY2jRHUXRa0gy41")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
