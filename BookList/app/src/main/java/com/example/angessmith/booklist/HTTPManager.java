package com.example.angessmith.booklist;

// Created by: Angela Smith 9/23/2014 for Java 1 term 1409

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

public class HTTPManager {
    public static final String TAG = "HTTP Manager";

    public static JSONObject getApiData(String url) {
        JSONObject jsonObject;
        String listData = "";
        try {
            // Set in the url to the passed in string
            URL bookListsUrl = new URL(url);
            // Open a connection for the HTTP resource
            HttpURLConnection connection = (HttpURLConnection)bookListsUrl.openConnection();
            // Set the properties
            connection.setRequestMethod("GET");
            // Set the connect and read timeouts to 12 seconds each
            connection.setConnectTimeout(12000);
            connection.setReadTimeout(12000);

            // Make the connection
            connection.connect();

            // Check the response code returned
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                // Create the input stream
                InputStream inputStream = connection.getInputStream();
                // Use the apache library to read the data
                listData = IOUtils.toString(inputStream);
                // close the stream
                inputStream.close();
                // close the connection
                connection.disconnect();
            }
            else {
                // Alert the user we cannot get data right now.
               // Toast.makeText(MainActivity.this, "We are unable to get the information right now.", Toast.LENGTH_LONG).show();
            }
        }
        // Catch exception for URL
        catch (MalformedURLException exception){
            // if the url is bad
            Log.e(TAG, "URL Exception: ", exception);
        }
        // Catch exception for HttpConnection
        catch (IOException exception) {
            // Unable to open connection
            Log.e(TAG, "Connection Exception: ", exception);
        }

        // Convert the data to JSON Object
        try {
            jsonObject = new JSONObject(listData);
            Log.d(TAG, jsonObject.toString());
        }
        catch (JSONException e) {
            // There was an error, empty out the object
            jsonObject = null;
            Log.e(TAG, "Unable to convert to JSON");
        }
    return jsonObject;

    }

}
