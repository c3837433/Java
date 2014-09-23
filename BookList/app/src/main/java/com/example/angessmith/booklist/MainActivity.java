package com.example.angessmith.booklist;

// Created by: Angela Smith 9/19/2014 for Java 1 term 1409

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
    // And the string keys for the listview
    static final String RANK = "rank";
    static final String TITLE = "title";
    static final String AUTHOR = "author";
    static final String DESCRIPTION = "description";
    static final String ISBN = "isbn";
    // Create a connectivity variable
    static private Connectivity mConnection;
    // Get the progress bar
    ProgressBar progressBar;
    Spinner mSpinner;
    Button getListsButton;
    private ArrayList<BookList> mBooklist;
    private ArrayList<BestSellersList> mBestSellersList;
    String displayName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get the reference to the progress bar and spinner
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mSpinner = (Spinner) findViewById(R.id.list_spinner);
        // Make them invisible at the start
        progressBar.setVisibility(View.INVISIBLE);
        mSpinner.setVisibility(View.INVISIBLE);
        // Get the button
        getListsButton = (Button) findViewById(R.id.list_search_button);

        // Listen for the click
        getListsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the best seller's lists for the spinner
                getBestSellerLists();
            }
        });



    }

    private void getBestSellerLists() {
        // Send the context to the connectivity class
        mConnection = new Connectivity(MainActivity.this) {
        };
        // Check if device has internet connection
        boolean connected = mConnection.isInternetAvailable();
        if (connected) {
            // Run the Async Task to get the list with the book list api
            String apiString = "http://api.nytimes.com/svc/books/v2/lists/names.json?api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529";
            GetBookListsTask task = new GetBookListsTask();
            // Execute the task sending in the url string
            task.execute(apiString);
        } else {
            // Inform the user we need internet to make the request
            Toast.makeText(MainActivity.this, getString(R.string.missing_internet), Toast.LENGTH_LONG).show();
        }
    }


    // FIRST ASYNC TASK THAT GETS THE LIST OF BOOK LISTS THAT CAN BE SEARCHED
    // <Params: What is passed in, Progress: progress report (int, long, void), Result: what is returned>
    private class GetBookListsTask extends AsyncTask<String, Integer, ArrayList<BookList>> {
        // onPreExecute has access to the class
        @Override
        protected void onPreExecute() {
            // turn on the activity indicator
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<BookList> doInBackground(String... params) {
            // Use the HTTP Manager class to get the api data
            JSONObject jsonObject = HTTPManager.getApiData(params[0]);
            if (jsonObject != null) {
                try {
                    JSONArray listArray = jsonObject.getJSONArray("results");
                    // See which url was passed over
                    mBooklist = new ArrayList<BookList>();
                    // Loop through the array and create new posts
                    for (int i = 0; i < listArray.length(); i++) {
                        // Get the array object
                        JSONObject list = listArray.getJSONObject(i);
                        // get the names
                        displayName = list.getString("display_name");
                        String encodedName = list.getString("list_name_encoded");
                        mBooklist.add(BookList.newInstance(displayName, encodedName));
                    }
                    return mBooklist;
                } catch (Exception e) {
                    Log.e(TAG, "Unable to get array from object");
                }
            } else {
                //otherwise return nothing
                mBooklist = null;
            }
            return mBooklist;
        }
        // When we are done getting the data
        @Override
        protected void onPostExecute(ArrayList<BookList> bookList) {
            super.onPostExecute(bookList);
            // turn off the activity indicator
            progressBar.setVisibility(View.INVISIBLE);
            // Return to the main activity and set the booklist in the spinner
            setSpinnerAdapter(bookList);
        }
    }


    // SECOND ASYNC TASK TO GET BOOK LIST
    private class GetBestSellersList extends AsyncTask<String, Integer, ArrayList<BestSellersList>> {

        @Override
        protected void onPreExecute() {
            // turn on the activity indicator again
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<BestSellersList> doInBackground(String... params) {
            // Use the HTTP Manager class to get the api data
            JSONObject jsonObject = HTTPManager.getApiData(params[0]);
            // If the returned data is not empty
            if (jsonObject != null) {
                try {
                    // Get the array of results
                    JSONArray listArray = jsonObject.getJSONArray("results");
                    // Create a new array list to hold the items
                    mBestSellersList = new ArrayList<BestSellersList>();
                    for (int i = 0; i < listArray.length(); i++) {
                        // Get the array object (only one)
                        JSONObject list = listArray.getJSONObject(i);
                        // get the properties
                        Number rank  = list.getInt("rank");
                        JSONArray detailsArray = list.getJSONArray("book_details");
                        JSONObject book = detailsArray.getJSONObject(0);
                        String title = book.getString("title");
                        String author = book.getString("author");
                        String description = book.getString("description");
                        String isbn = book.getString("primary_isbn13");
                        // Get the current list from the spinner
                        String listName = mSpinner.getSelectedItem().toString();
                        // and create the list item
                        mBestSellersList.add(BestSellersList.newInstance(rank, author, title, description, isbn, listName));
                    }
                    return mBestSellersList;
                }
                catch (Exception e) {
                    Log.e(TAG, "Unable to get array from object");
                }

            } else {
                // If we did not get any object, return nothing
                mBestSellersList = null;
            }
            return mBestSellersList;
        }

        // When we are done getting the data
        @Override
        protected void onPostExecute(final ArrayList<BestSellersList> bookList) {
            super.onPostExecute(bookList);
            // turn off the activity indicator
            progressBar.setVisibility(View.INVISIBLE);
            // Set all the books within the listview
            ArrayList<HashMap<String, Object>> bestSellersMapList = putListInHashmap(bookList);
            // Display the listview with the map
            SetUpListView(bookList, bestSellersMapList);
        }
    }


    // Method that takes in the list and sets the items within a hashmap
    private ArrayList<HashMap<String, Object>> putListInHashmap(ArrayList<BestSellersList> bookList) {
        ArrayList<HashMap<String, Object>> bestSellersMapList = new ArrayList<HashMap<String, Object>>();
        // Loop through each of the objects
        for (BestSellersList bestSeller : bookList)
        {
            // Create a new hashmap object to be used in the list
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            // Add each of the keys and values
            hashMap.put(RANK, bestSeller.getRank());
            hashMap.put(TITLE, bestSeller.getTitle());
            hashMap.put(AUTHOR, bestSeller.getAuthorName());
            hashMap.put(DESCRIPTION, bestSeller.getDescription());
            // Add to the list
            bestSellersMapList.add(hashMap);
        }
        return bestSellersMapList;
    }

    // Takes the hashmap and sets the items in the listview
    private void SetUpListView(final ArrayList<BestSellersList> bookList, ArrayList<HashMap<String, Object>> bestSellersMapList) {
        ListView listView = (ListView) findViewById(R.id.best_sellers_listview);
        // Create an instance of the BestSellersList adapter and set it to the listview
        listView.setAdapter(new BestSellersAdapter(this, bestSellersMapList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // View this item in the detail view
                openBookDetailView(parent, position, bookList);

            }
        });
    }

    // When the user clicks on a list item, send the data to the detail view
    private void openBookDetailView(AdapterView<?> parent, int position, ArrayList<BestSellersList> bookList) {
        // Get the current item
        Object book = parent.getItemAtPosition(position);
        // Create an intent to open up the detail view
        Intent intent = new Intent(getApplicationContext(), BookDetails.class);
        // Set this item's properties in the detail view
        intent.putExtra(TITLE,  (bookList.get(position).getTitle()));
        intent.putExtra(AUTHOR,  (bookList.get(position).getAuthorName()));
        intent.putExtra(DESCRIPTION,  (bookList.get(position).getDescription()));
        intent.putExtra(RANK,  (bookList.get(position).getRank()));
        intent.putExtra(ISBN, (bookList.get(position).getISBN()));
        intent.putExtra("List", bookList.get(position).getListName());
        // Switch views
        startActivity(intent);
    }


    private void setSpinnerAdapter(ArrayList<BookList> bookList) {
        // Show the spinner
        mSpinner.setVisibility(View.VISIBLE);
        // And Remove the Button
        getListsButton.setVisibility(View.GONE);
        // Create an array adapter to set the items in the spinner
        ArrayAdapter<BookList> myAdapter = new ArrayAdapter<BookList>(this, android.R.layout.simple_spinner_item, (bookList));
        // Set the adapter to the spinner
        mSpinner.setAdapter(myAdapter);
        // Set on click listener for spinner
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Verify we still are connected to the internet
                // Send the context to the connectivity class
                mConnection = new Connectivity(MainActivity.this) {
                };
                // Check if device has internet connection
                boolean connected = mConnection.isInternetAvailable();
                if (connected) {
                    Object bookList = parent.getItemAtPosition(position);
                    Log.i(TAG, "List = " + bookList);
                    // Get the search name for this list
                    String searchId = (mBooklist.get(position).getEncodedName());
                    // Create a url string
                    String urlString = "http://api.nytimes.com/svc/books/v2/lists.json?list-name=" + searchId + "&api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529";
                    // Get an instance of the second async task
                    GetBestSellersList task = new GetBestSellersList();
                    // Execute and send in the new string
                    task.execute(urlString);
                } else {
                    // Inform the user we need internet access to search
                    Toast.makeText(MainActivity.this, getString(R.string.missing_internet), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
