package com.example.angessmith.tabbednavigation;

import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


// Created by AngeSSmith on 10/20/14.

public class HTTPHelper  {

    // Define the tag for any exceptions
    public static final String TAG = "HTTPHelper";

    public static String pullWeatherData (MainActivity context, String param) {
        // Create an empty string to hold the data
        String weatherData = "";
        try {
            // Create a url from the string
            URL url = new URL(param);
            // Set up the connection
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            // connect
            connection.connect();
            // make sure response was valid
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                // If good, use the library to get the data from the stream
                InputStream inputStream = connection.getInputStream();
                weatherData = IOUtils.toString(inputStream);
                // Close and disconnect from the connection
                inputStream.close();
                connection.disconnect();
            }
            else {
                // If the response was not valid, inform the user
                Toast.makeText(context, "Unable to access server at this time, try again later.", Toast.LENGTH_LONG).show();
            }
        }
        // Prepare for possible exceptions
        catch (MalformedURLException exception){
            // URL Exception
            Log.e(TAG, "URL Exception: ", exception);
        }
        catch (IOException exception) {
            // or a connection exception
            Log.e(TAG, "Connection Exception: ", exception);
        }
        // Return the weather data
        return weatherData;
    }
}
