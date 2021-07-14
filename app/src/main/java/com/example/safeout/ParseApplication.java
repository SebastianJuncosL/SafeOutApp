package com.example.safeout;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initializes Parse SDK as soon as the application is created
        Parse.initialize(new Parse.Configuration.Builder(this)
            .applicationId("kOd3HxQiou5CjgTI4tSiQEwtYR8NQ0JdoHdUX38u")
            .clientKey("cJ8WmQ9spbjEnV8BHNphr6cfASenBmRg8T50ilT6")
            .server("https://parseapi.back4app.com")
            .build()
        );
    }
}
