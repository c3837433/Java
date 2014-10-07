package com.example.angessmith.fundamentalsapp;


// Created by AngeSSmith on 9/30/14.

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPHelper {
    public static final String TAG = "HTTPHelper";

    public static String getData(Main context, String param) {

        JSONObject jsonObject;
        String dataString = "";
        try {
            // set the passed in string as a url
            URL url = new URL(param);
            // Open a URL connection
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            // Set up the connection properties
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            // Begin the connection
            connection.connect();
            // Check the response
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "response code = " + responseCode);
            // If it is good get the data
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                InputStream inputStream = connection.getInputStream();
                // Get a string of the data
                dataString = IOUtils.toString(inputStream);
                // close the stream and connection
                inputStream.close();
                connection.disconnect();
            }
            else {
                // Inform user data not available
                Toast.makeText(context, "Online data not available", Toast.LENGTH_LONG).show();
            }
        }
        // URL Exception
        catch (MalformedURLException exception){
            // if the url is bad
            Log.e(TAG, "URL Exception: ", exception);
        }
        // HttpConnection Exception
        catch (IOException exception) {
            Log.e(TAG, "Connection Exception: ", exception);
        }

        // Return the data string
        return dataString;
    }
}
