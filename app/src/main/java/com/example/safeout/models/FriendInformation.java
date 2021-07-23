package com.example.safeout.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("FriendInformation")
public class FriendInformation extends ParseObject {
    // This class will get all contacts from the logged in user

    public static final String userId = "objectId";
    public static final String phoneNumber = "phoneNumber";
    public static final String profilePic = "profilePicture";
    public static final String userName = "username";

    public String getUserId() { return userId; }

    public String getPhoneNumber() { return phoneNumber; }

    public ParseFile getProfilePic() { return getParseFile(profilePic); }

    public String getUserName() { return userName; }

    // Will be used for uploading profile picture functionality
    public void setProfilePic(ParseFile picture) { put("profilePicture", picture); }


}
