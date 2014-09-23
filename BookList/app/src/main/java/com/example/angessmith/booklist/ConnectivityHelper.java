package com.example.angessmith.booklist;

// Created by: Angela Smith 9/19/2014 for Java 1 term 1409

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityHelper {
    Context mContext;

    // Pass the context from the activity class
    public ConnectivityHelper(Context _context) {
        this.mContext = _context;
    }

    public boolean isInternetAvailable () {
        // Get the connectivity manager
        // Make sure connectivity has been added as a permission in Android Manifest
        ConnectivityManager manager =  (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Instantiate a network request to get information about the current connection
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        // Set the state to a boolean
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Yea! we have a connection
            isAvailable =  true;
        }
        return isAvailable;
    }

}
