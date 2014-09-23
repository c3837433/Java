package com.example.angessmith.booklist;
// Created by: Angela Smith 9/21/2014 for Java 1 term 1409
// Google API to search a book by isbn: https://www.googleapis.com/books/v1/volumes?q=isbn:[isbn]
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import org.json.JSONArray;
import org.json.JSONObject;

public class BookDetails extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();
    ProgressBar progressBar;

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
                // search google api for an image of the book
                String apiString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
                Log.d(TAG, apiString);
                // Create an instance the ASYNC Task
                GetBookImage task = new GetBookImage();
                // Start the task
                task.execute(apiString);
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
            JSONObject jsonObject = HTTPManager.getApiData(params[0]);
            String bookData = "";
            String urlString = "";

            try {
                // As long as the result is not empty, create an array of result objects,
                if (jsonObject != null) {
                    JSONArray itemArray = jsonObject.getJSONArray("items");
                    //.getJSONObject("volumeInfo").getJSONObject("imageLinks").get("thumbnail");
                    JSONObject volumeObject = itemArray.getJSONObject(0);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
