package com.example.angessmith.goodfeed;

// Created by AngeSSmith on 12/8/14.
// Custom class for Parse user class
import com.parse.ParseFile;
import com.parse.ParseUser;

public class StoryAuthor extends ParseUser {

    // Methods to return each posts author information
    public String getUserName() {
        return getString("UserFullName");
    }

    public ParseFile getProfilePic() {
        return getParseFile("ProfilePicture");
    }

    public String getFacebookId () {
        return getString("FacebookId");
    }

    public String getAuthorId () {
        return  getObjectId();
    }
}
