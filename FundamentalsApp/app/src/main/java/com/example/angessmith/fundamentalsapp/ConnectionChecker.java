package com.example.angessmith.fundamentalsapp;

//Created by AngeSSmith on 9/30/14 for Java 2 Term 1410.

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionChecker {
    Context mContext;
    // Pass the context from the activity class
    public ConnectionChecker(Context _context) {
        this.mContext = _context;
    }

    public boolean canConnectInternet () {
        // Create an instance of the Connectivity manager
        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Make a request to check the network state
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        // Return whether or not we have access and a connection
        return networkInfo != null && networkInfo.isConnected();

    }

}
