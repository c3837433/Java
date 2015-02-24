package com.example.angessmith.goodfeed;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

 // Created by AngeSSmith on 12/2/14.

// This custom class verifies the user has internet

public class ConnectionChecker {

    // Define a context
    Context mContext;

    // Create the connection helper getting the context from the activity
    public ConnectionChecker (Context context) {
        this.mContext = context;
    }

    // Check if internet is available and connected
    public boolean canConnectInternet () {
        // Check to see if we have service
        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get the network state
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        // boolean for network has access and is connected
        return networkInfo != null && networkInfo.isConnected();
    }
}
