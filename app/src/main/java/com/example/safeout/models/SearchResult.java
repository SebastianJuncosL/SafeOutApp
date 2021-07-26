package com.example.safeout.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("SearchResult")
public class SearchResult extends ParseObject {
    public String userName;
    public String profilePicture;

    public SearchResult() {}

    public SearchResult(String userName) { this.userName = userName; }

    public SearchResult(String userName, String profilePicture) {
        this.userName = userName;
        this.profilePicture = profilePicture;
    }

    public String getUserName() {
        return userName;
    }

    public ParseFile getProfilePicture() {
        return getParseFile(profilePicture);
    }
}
