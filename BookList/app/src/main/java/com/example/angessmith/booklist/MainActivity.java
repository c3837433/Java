package com.example.angessmith.booklist;
// Created by: Angela Smith 9/19/2014 for Java 1 term 1409
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
/*

    Minimum Requirements:
        1. Your project must compile with a minimum SDK of API 14 and a target SDK of API 19 and run on an Android 4.0+ emulator or device.
        2. You must turn in a Fundamentals Walkthrough video.
        3. You must have your code pushed to a private Git repo and your instructor must have access.
        4. Application's API must be approved by the instructor via the API approval discussion board.
        5. Comments must be used to place the student name at the top of all .java files.

    Basic Requirements:
        1. Pull data from an approved API endpoint using several predefined URL strings.
        2. Utilize an AsyncTask to pull down network data in a background thread and update the UI with the retrieved data.
        3. Check for a network connection before each API call and notify the user if no connection exists.
        4. Parse JSON data and display two or more data points from multiple JSON objects or array elements.

    Mastery Requirements:
        1. Pull data from an approved API endpoint using a dynamic URL string that changes based on user text input.
        2. Utilize AsyncTask callbacks to show a progress indicator before task execution and hide the indicator when the task completes.
        3. Create a custom helper class that can be used to check network connectivity.
        4. Utilize advanced views to display parsed JSON data or use libraries to enhance the application UI.

    Rubric Points
        Structure/Efficiency: The application is structured efficiently and with proper methods and constructs.
        URL Connection: Application can connect to an API endpoint using a dynamic URL string that changes
             based on user text input that is properly encoded.
        AsyncTask: AsyncTask performs all network operations in a background thread.  Resulting data is passed
            to the appropriate callback method in order to update the UI after the task finishes executing.
            Appropriate callbacks are used to show an indeterminate progress indicator before execution starts and
            hide the progress indicator when the task finishes executing.
 Created    Connection Check: Application checks for a network connection before each network request executes
            and properly informs the user of a lack of connection.  Code utilizes a static helper method in a
            custom network connectivity class to check for connection.
        JSON & UI: Application parses and displays two or more data points from multiple JSON objects or
            JSON array entries. Either advanced views are used to display parsed data or libraries are used to
            enhance the application UI.

    API's:

        1. Lists API: http://api.nytimes.com/svc/books/v2/lists/names.json?api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529
        2. Books API with list string: http://api.nytimes.com/svc/books/v2/lists.json?list-name=animals&api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529

 */

public class MainActivity extends Activity {

    // Create the tag for debugging
    public static final String TAG = MainActivity.class.getSimpleName();
    // Create a connectivity variable
    static private Connectivity mConnection;
    // Get the progress bar
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get the reference to the progress bar
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        // Make it invisible at the start
        progressBar.setVisibility(View.INVISIBLE);
        // Get the button
        final Button getListsButton = (Button) findViewById(R.id.list_search_button);
        // Listen for the click
        getListsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send the context to the connectivity class
                mConnection = new Connectivity(MainActivity.this) {};
                // Check if device has internet connection
                boolean connected = mConnection.isInternetAvailable();
                if (connected) {
                    Toast.makeText(MainActivity.this,"We have internet connection", Toast.LENGTH_LONG).show();
                    // Run the Async Task to get the list
                    // Set the book list api
                    String apiString = "http://api.nytimes.com/svc/books/v2/lists/names.json?api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529";
                    GetBookListsTask task = new GetBookListsTask();
                    // Execute the task sending in the url string
                    task.execute(apiString);

                } else {
                    Toast.makeText(MainActivity.this,"Searching requires an internet connection.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    // <Params: What is passed in, Progress: progress report (int, long, void), Result: what is returned>
    private class GetBookListsTask extends AsyncTask<String, Integer, JSONObject> {


        // onPreExecute has access to the class
        @Override
        protected void onPreExecute() {
            // turn on the activity indicator
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            // set the responseCode to empty
            int responseCode = -1;
            JSONObject jsonObject = null;
            String listData = "";
            // Prepare for an exception in case the url is invalid
            try {
                // Set in the url to the passed in string
                URL bookListsUrl = new URL(params[0]);
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
                responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK)
                {
                    // We received data
                    Log.d(TAG, "We have received Data");
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
                    Toast.makeText(MainActivity.this, "We are unable to get the information right now.", Toast.LENGTH_LONG).show();
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
            return null;
        }


        // When we are done
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            // turn off the activity indicator
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
