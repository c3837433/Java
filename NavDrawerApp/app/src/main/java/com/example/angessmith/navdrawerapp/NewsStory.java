package com.example.angessmith.navdrawerapp;

import java.io.Serializable;

/**
 * Created by AngeSSmith on 10/19/14.
 */
public class NewsStory implements Serializable {

    // Create the unique id
    private static final long SERIAL_VERSION_UID = 264352789266376484L;

    //  Define the story properties
    private String mTitle;
    private String mTimeStamp;
    private String mStory;
    private int mImageId;

    // Build the stories with a factory
    public static NewsStory newInstance(String _title, String _timeStamp, String _story, int _image) {
        // Create the story
        NewsStory story = new NewsStory();
        // set the properties
        story.setTitle(_title);
        story.setStory(_story);
        story.setTimeStamp(_timeStamp);
        story.setImageId(_image);
        // return the story
        return story;
    }

    public NewsStory() {
        mImageId = 0;
        mStory = mTimeStamp = mTitle = "";
    }

    // Define the getters and setters
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String _title) {
        this.mTitle = _title;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }
    public void setTimeStamp(String _timeStamp) {
        this.mTimeStamp = _timeStamp;
    }

    public String getStory() {
        return mStory;
    }
    public void setStory(String _story) {
        this.mStory = _story;
    }

    public int getImageId() {
        return mImageId;
    }
    public void setImageId(int _image) {
        this.mImageId = _image;
    }


}
