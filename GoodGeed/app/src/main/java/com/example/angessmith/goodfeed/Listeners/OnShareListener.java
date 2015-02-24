package com.example.angessmith.goodfeed.Listeners;

// Created by AngeSSmith on 12/3/14.

// On share listener allows the activity access to the text in the Edit Text views when the user
// presses "Share" on the action bar

public interface OnShareListener {
    public void prepareStoryForShare(String title, String story);
}
