package com.example.safeout.backendConnectivity;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        // Initializes Parse SDK as soon as the application is created
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("9RDOwmO8ezR73qpVn1zczltOaSDCWcV3niedkwZr")
                .clientKey("1IaHvBgugXVtCplseqlxEn4RFCY2jRHUXRa0gy41")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
