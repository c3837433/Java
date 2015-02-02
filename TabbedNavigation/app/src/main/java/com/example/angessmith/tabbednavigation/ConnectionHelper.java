package com.example.angessmith.tabbednavigation;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

// Created by AngeSSmith on 10/20/14.

public class ConnectionHelper {
    // Define a context
    Context mContext;

    // Create the connection helper getting the context from the activity
    public ConnectionHelper(Context context) {
        this.mContext = context;
    }

    // Check if internet is available and connected
    public boolean canConnectInternet () {
        // Get a connectivity manager
        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check the network state
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        // boolean for network has access and is connected
        return networkInfo != null && networkInfo.isConnected();
    }
}
