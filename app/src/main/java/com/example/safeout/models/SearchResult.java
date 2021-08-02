package com.example.safeout.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Collections;
import java.util.List;

@ParseClassName("SearchResult")
public class SearchResult extends ParseObject {

    public static final String USERNAME = "username";
    public static final String PROFILE_PIC = "profilePicture";
    public static final String OBJECT_ID = "objectId";

    public String getUserName() {
        return getString(USERNAME);
    }

    public ParseFile getProfilePicture() {
        return getParseFile(PROFILE_PIC);
    }

    public String getObjectId() { return getString(OBJECT_ID); }
}
