package com.example.safeout.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.parse.ParseFile;

public class MyMarkers implements ClusterItem {

    private LatLng position;
    private String title;
    private String snippet;

    private ParseFile userPicture;

    public MyMarkers() {}

    public MyMarkers(LatLng position, String title, String snippet, ParseFile userPicture) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.userPicture = userPicture;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public ParseFile getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(ParseFile userPicture) {
        this.userPicture = userPicture;
    }

    @Override
    public LatLng getPosition() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
