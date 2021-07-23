package com.example.safeout.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("SearchResult")
public class SearchResult extends ParseObject {
    private String userName;
    private String profilePicture;

    public SearchResult() {}

    public SearchResult(String userName, String profilePicture) {
        this.userName = userName;
        this.profilePicture = profilePicture;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ParseFile getProfilePicture() {
        return getParseFile(profilePicture);
    }

    public void setProfilePicture(ParseFile profilePicture) {
        put("profilePicture", profilePicture);
    }
}
