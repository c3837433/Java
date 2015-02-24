package com.example.angessmith.goodfeed;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

// Created by AngeSSmith on 12/8/14.
// Custom calss for Parse Story class

@ParseClassName("Story")
public class StoryPost extends ParseObject {
    // Methods to set or return object values
    public String getTitle() {
        return getString("Title");
    }
    public void setTitle(String title) {
        put("Title", title);
    }

    public String getStoryText() {
        return getString("Text");
    }

    public void setStory(String story) {
        put("Text", story);
    }

    public Date getCreatedAtDate() {
        return getCreatedAt();
    }

    public void setAuthor(ParseUser user) {
        put("author", user);
    }

    public ParseFile getStoryPhoto() {
        return getParseFile("mediaThumb");
    }

    public void setStoryImage(ParseFile file) {
        put("mediaThumb", file);
    }

    public StoryAuthor getStoryAuthor() {
        return (StoryAuthor) get("author");
    }

    public String getStoryId() {
        return getObjectId();
    }

}
