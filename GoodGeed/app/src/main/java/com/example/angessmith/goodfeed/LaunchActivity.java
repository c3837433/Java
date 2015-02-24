package com.example.angessmith.goodfeed;

// Created by Angela Smith for ADP2 Term 1412 December 2, 2014
// Initial activity that checks for user logged in status
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class LaunchActivity extends Activity {

    // Define the activity properties
    public static final String TAG = "LaunchActivity";
    public static final int LAUNCH_LOG_IN = 9199821;
    public static final int LAUNCH_MAIN_VIEW = 10112001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        // register the subclasses
        ParseObject.registerSubclass(StoryPost.class);
        //ParseObject.registerSubclass(StoryAuthor.class);
        ParseUser.registerSubclass(StoryAuthor.class);
        ParseObject.registerSubclass(Flag.class);
        // Check parse when the activity is loaded
        Log.i(TAG, "Initializing Parse");
        // Initialize parse with the data keys
        Parse.initialize(this, "pkzn46AzvVVZErQnOtlRw6WBNaSNS9UjcvB2awt8", "7etJgFc29sl8qNK388cDDYEhdTzNerGkR62jOoMw");
        // Allow parse access to track app
        ParseAnalytics.trackAppOpenedInBackground(null);
        // Check if user is currently logged in
        checkAccessAndLoggIn();
    }

    // Make sure we have a network connection
    private void checkAccessAndLoggIn() {
        // Make sure we have access
        ConnectionChecker connectionHelper = new ConnectionChecker(this);
        boolean haveAccess = connectionHelper.canConnectInternet();
        if (haveAccess) {
            // Check if user is logged in
            final ParseUser user = ParseUser.getCurrentUser();
            boolean isLoggedIn = (user != null);
            Log.d(TAG, "User logged in = " + isLoggedIn);
            checkLogIn(isLoggedIn);
        } else {
            // Alert user we need access to internet
            Toast.makeText(this, "We are unable to access Good Feed. Please check your connection", Toast.LENGTH_LONG).show();
        }
    }


    // See if the user is logged in
    private void checkLogIn(boolean loggedIn) {
        if (loggedIn) {
            Log.i(TAG, "User is logged in");
            // Launch the Main Feed activity
            Intent intent = new Intent(this, MainFeedActivity.class);
            // Send the request code to add the quote upon returning
            startActivityForResult(intent, LAUNCH_MAIN_VIEW);
        } else {
            Log.i(TAG, "User is not logged in");
            // Launch the Log In Activity
            Intent intent = new Intent(this, LogInActivity.class);
            startActivityForResult(intent, LAUNCH_LOG_IN);
        }
    }

    // When return from other activities, recheck
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Either way, check log in again
        checkAccessAndLoggIn();
    }
}
