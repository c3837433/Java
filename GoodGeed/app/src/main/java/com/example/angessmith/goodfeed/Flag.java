package com.example.angessmith.goodfeed;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;


// Created by AngeSSmith on 12/12/14.

// Custom Parse class for when users want to flag posts
@ParseClassName("Flag")
public class Flag extends ParseObject {


    public void setAuthor(ParseUser user) {
        put("flaggedBy", user);
    }

    public void setReason(String reason) {
        put("Reason", reason);
    }

    public void setStory(ParseObject story) {
        put("postFlagged", story);
    }
}

