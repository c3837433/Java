package com.example.angessmith.booklist;

// Created by: Angela Smith 9/21/2014 for Java 1 term 1409

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.image.SmartImageView;

import org.json.JSONArray;
import org.json.JSONObject;

public class BookDetailsActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();
    // Create an instance of the progress bar
    ProgressBar progressBar;
    // And Context to pass into HTTPHelper
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Get the item that was sent over
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString(MainActivity.TITLE);
            String author = extras.getString(MainActivity.AUTHOR);
            String description = extras.getString(MainActivity.DESCRIPTION);
            Integer rank = extras.getInt(MainActivity.RANK);
            String isbn = extras.getString(MainActivity.ISBN);
            String list = extras.getString("List");
            // If we have an isbn, try to get the image for this book
            if ((isbn != "") || (isbn != null) ) {
                // Create a connectivity variable
                ConnectivityHelper mConnection = new ConnectivityHelper(BookDetailsActivity.this) {
                };
                // Check if device has internet connection
                boolean connected = mConnection.isInternetAvailable();
                if (connected) {
                    // search google api for an image of the book
                    String apiString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
                    Log.d(TAG, apiString);
                    // Create an instance the ASYNC Task
                    GetBookImage task = new GetBookImage();
                    // Start the task
                    task.execute(apiString);

                } else {
                    // Inform the user we need internet to make the request
                    Toast.makeText(BookDetailsActivity.this, getString(R.string.no_image_internet), Toast.LENGTH_LONG).show();
                    // Hide the progress bar
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            // Set the items on the view
            SetBookDetailsInView(title, author, description, rank, list);
        }

    }

    private void SetBookDetailsInView(String title, String author, String description, Integer rank, String list) {
        // Get the views
        // Declare our view variables and assign them the views from the layout file
        final TextView bookTitle = (TextView) findViewById(R.id.book_detail_title);
        final TextView bookAuthor = (TextView) findViewById(R.id.book_detail_author);
        final TextView bookDescription = (TextView) findViewById(R.id.book_detail_description);
        final TextView bookRank = (TextView) findViewById(R.id.book_detail_rank);

        // set the passed in texts
        bookTitle.setText(title);
        bookAuthor.setText("By: " + author);
        bookDescription.setText(description);
        bookRank.setText("Ranked #" + String.valueOf(rank) + " on the New York Times " + list + " List");
    }

    // Get the book image
    private class GetBookImage extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // turn on the activity indicator again
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            // Use the HTTP Manager class to get the api data
            JSONObject jsonObject = HTTPHelper.getApiData(mContext, params[0]);
            // Create a url string to pass back
            String urlString = "";
            try {
                // As long as the result is not empty, create an array of result items,
                if (jsonObject != null) {
                    JSONArray itemArray = jsonObject.getJSONArray("items");
                    // Get the first (only) object
                    JSONObject volumeObject = itemArray.getJSONObject(0);
                    // Get the thumbnail image
                    urlString = volumeObject.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail");
                }
                else {
                    //otherwise return nothing
                    urlString = "";
                }
            }
            catch (Exception e) {
                Log.e(TAG, "Unable to get array from object");
            }
            // Return the url string
            return urlString;
        }


        // When we are done getting the data
        @Override
        protected void onPostExecute(final String urlString ) {
            super.onPostExecute(urlString);
            // turn off the activity indicator
            progressBar.setVisibility(View.INVISIBLE);
            // get the smart image view
            SmartImageView bookImage = (SmartImageView) findViewById(R.id.book_image);
            // Load the image from the url
            bookImage.setImageUrl(urlString);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.book_details, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }


}
