package com.example.safeout.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class CustomMarker implements ClusterItem {

    private LatLng position;
    private String title;
    private String phoneNumber;
    private int profilePic;
    private String userId;

    public CustomMarker(LatLng position, String title, String phoneNumber, int profilePic, String userId) {
        this.position = position;
        this.title = title;
        this.phoneNumber = phoneNumber;
        this.profilePic = profilePic;
        this.userId = userId;
    }

    public CustomMarker(){}

    @Override
    public LatLng getPosition() {
        return null;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    @Override
    public String getTitle() {
        return null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
