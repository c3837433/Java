package com.example.angessmith.fundamentalsapp;

//Created by AngeSSmith on 9/30/14 for Java 2 Term 1410.

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.angessmith.fundamentalsapp.Fragment.BookListFragment;
import com.example.angessmith.fundamentalsapp.Fragment.DetailFragment;
import com.example.angessmith.fundamentalsapp.Fragment.SettingsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;


public class Main extends Activity implements BookListFragment.OnListItemClickListener, BookListFragment.OnSpinnerListener  {
    final String TAG = "FundamentalsApp";

    ArrayList<Book> mBooks;
    View bookListFragmentView;
    public static ArrayList<BestSellerList> bestSellerList;
    private SharedPreferences settingsPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingsPreferences = getPreferences(MODE_PRIVATE);
        if (settingsPreferences.contains("PREF_NETWORK_LIST")) {
            //Log.d(TAG, "Preference Default has been set");
            // Make sure we have a book list saved in case the user deleted it and set to cache only
            String networkPreference = settingsPreferences.getString("PREF_NETWORK_LIST","network");
            //Log.i(TAG, "This user selected:"+ networkPreference);

            if (networkPreference.equals("cache_only")) {
               // Log.d(TAG, "Need to get bestSellers list");
                File external = this.getExternalFilesDir(null);
                // Make sure this file exists or not
                boolean fileExists = new File(external, "bestSellersList.txt").exists();
                if (!fileExists) {
                    Log.d(TAG, "NO BOOK LIST IN FILE");
                }
            }
        } else {
            Log.d(TAG, "Preference Default has NOT been set");
            // set the default
            SharedPreferences.Editor editor = settingsPreferences.edit();
            editor.putString("PREF_NETWORK_LIST", "network");
            editor.commit();
        }
        // Get the book lists in for the spinner

        if (savedInstanceState == null) {
            //Toast.makeText(this, "saved instance state = null", Toast.LENGTH_SHORT).show();
            if (bestSellerList == null)
            {
                //Toast.makeText(this, "Bestsellers = null", Toast.LENGTH_SHORT).show();
                GetBookLists();
            }
            //Log.d(TAG, "Getting Fragment");
            // Create the fragment
            BookListFragment bookListFragment = BookListFragment.newInstance();
            // get the fragment and commit it so we can access it with the spinner
            getFragmentManager().beginTransaction().replace(R.id.book_list_container, bookListFragment, BookListFragment.TAG).commit();
        } else {
            // Get the list out of the saved instance state
            //Toast.makeText(this, "Checking saved instance state for list", Toast.LENGTH_SHORT).show();
            bestSellerList = (ArrayList<BestSellerList>) savedInstanceState.getSerializable("ARG_BOOKLIST");
            Toast.makeText(this, "Updating List from Saved State", Toast.LENGTH_SHORT).show();
            if (bestSellerList == null) {
                Toast.makeText(this, "Bestsellers = null, getting list", Toast.LENGTH_SHORT).show();
                GetBookLists();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // set the current values to the bundle
        Toast.makeText(Main.this, "saving current list", Toast.LENGTH_SHORT).show();
        savedInstanceState.putSerializable("ARG_BOOKLIST", bestSellerList);
        super.onSaveInstanceState(savedInstanceState);
    }

    // Implementation for button listener
    //@Override
    public void GetBookLists() {
        //Log.d(TAG, "User clicked the button to get data");

        // CHECK THE USERS NETWORK PREFERENCE
        String networkPreference = settingsPreferences.getString("PREF_NETWORK_LIST","network");
        //Log.i(TAG, "This user selected:"+ networkPreference);
        // CHECK THE CONENCTION
        ConnectionChecker connectionChecker = new ConnectionChecker(Main.this);
        boolean isConnected = connectionChecker.canConnectInternet();


        // FOR "NETWORK" PREFERENCE SETTING
        if (networkPreference.equals("network") && (isConnected)) {
            //Log.d(TAG, "Pulling Best Sellers Lists for user's NETWORK preference");
            pullBookListsFromWeb();
        }
        else {
            //Log.d(TAG, "Checking Cache for Best Sellers Lists");
            ArrayList<BestSellerList> cachedList = (ArrayList<BestSellerList>) pullCachedBestSellerListsForOffline(Main.this, "bestSellersList.txt");
            if (cachedList == null)
            {
                //Log.d(TAG, "No Best Sellers List Available in Cache, pulling from web");
                if (networkPreference.equals("check_offline") && (isConnected)) {
                    pullBookListsFromWeb();
                }
                else {
                    //Log.d(TAG, "No Best Sellers List Available in Cache and no internet available");
                    Toast.makeText(bookListFragmentView.getContext(), "No cache available, change your preference in settings or connect to the internet.", Toast.LENGTH_LONG).show();
                }
            }
            else {
                //Log.d(TAG, "Loading Best Sellers List from Cache");
                //Log.d(TAG, "List data:" + cachedList);
                // send the arraylist to the spinner
                setListInSpinner(cachedList, 0);
            }
        }

    }

    private void pullBookListsFromWeb() {
        Toast.makeText(Main.this, "Getting Lists from Web", Toast.LENGTH_SHORT).show();
        // run the async task to get the list of array objects
        String bookListString = "http://api.nytimes.com/svc/books/v2/lists/names.json?api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529";
        // Get an instance of the ASYNC TASK to get the data
        GetBookListsTask task = new GetBookListsTask();
        // execute the task
        task.execute(bookListString);
    }

    public void setListInSpinner(ArrayList<BestSellerList> bookList, int position) {
        //Log.d(TAG, "Spinner data:" + bookList);
        //Log.d(TAG, "Spinner position:" + position);
        ArrayAdapter<BestSellerList> arrayAdapter = new ArrayAdapter<BestSellerList>(this, android.R.layout.simple_spinner_dropdown_item, (bookList));
        // Set the adapter to the spinner
        //Log.d(TAG, "The Spinner should be at position " + position);
        //Log.i(TAG, "The saved list = "+  bookList);
        BookListFragment.mSpinner.setSelection(position);
        BookListFragment.mSpinner.setAdapter(arrayAdapter);
    }

    @Override
    public void GetBooksOnList(AdapterView<?> parent, View view, int position, long id) {
        // Get the bookl on the list at the selected index
        getBookListBooks(parent, position, bestSellerList);

    }


    // FIRST TASK TO GET BEST SELLER LISTS FOR SPINNER
    private class GetBookListsTask extends AsyncTask<String, Integer, ArrayList<BestSellerList>> {
        // onPreExecute has access to the class
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected ArrayList<BestSellerList> doInBackground(String... params) {
            // Use the HTTP Manager class to get the api data
            String urlstring = params[0];
            Log.d(TAG, urlstring);
            String dataString = HTTPHelper.getData(Main.this, params[0]);
            bestSellerList = new ArrayList<BestSellerList>();
            // Convert the returned string to JSON Objects
            try {
                // Create json objects from the data string
                JSONObject jsonObject = new JSONObject(dataString);
                Log.d(TAG, jsonObject.toString());
                // Get the array of results
                JSONArray listArray = jsonObject.getJSONArray("results");
                // Loop through the best sellers lists
                for (int i = 0; i < listArray.length(); i++) {
                    // Get the array object
                    JSONObject list = listArray.getJSONObject(i);
                    // get the display and encoded
                    String displayName = list.getString("display_name");
                    String encodedName = list.getString("list_name_encoded");
                    // Add the items to the list
                    bestSellerList.add(BestSellerList.newInstance(displayName, encodedName));
                }
                //  write the object to storage
                cacheDataForOfflineUse("bestSellersList.txt", bestSellerList);
                // return the list
                return bestSellerList;
            }
            catch (JSONException e) {
                Log.e(TAG, "Unable to convert to JSON");
            }
            // Return the list to the array
            return bestSellerList;
        }

        @Override
        protected void onPostExecute(ArrayList<BestSellerList> arrayList) {
            // Get the spinner and create an array adapter for it
            setListInSpinner(arrayList, 0);
            //setSellerListsInSpinner(arrayList);
        }
    }

    // CACHE BEST SELLER LISTS OR LIST BOOKS IN EXTERNAL STORAGE FOR OFFLINE USE
    private void cacheDataForOfflineUse(String filename, java.util.RandomAccess object) {
        File externalFilesDir = this.getExternalFilesDir(null);
        //Log.d(TAG, "File directory = " + externalFilesDir);
        File file = new File(externalFilesDir, filename);
        //File file;
        try {
            // file = File.createTempFile(filename, null, getActivity().getCacheDir());
            //Log.d(TAG, "File directory = " + file);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            // Write the data to the file
            outputStream.writeObject(object);
            // close the stream
            outputStream.close();
        }
        catch(Exception exception) {
            // There was an error writing the file
            Log.e(TAG, "Output Stream Exception: ", exception);
        }
    }

    // RETRIEVE SPINNER BEST SELLER LISTS FROM CACHE FOR OFFLINE USE
    private Object pullCachedBestSellerListsForOffline(Context context, String filename) {
        List<BestSellerList> storedDataList = null;
        // Reaccessed the external file directory
        File external = context.getExternalFilesDir(null);
        // And find the file with the passed in name
        File file = new File(external, filename);
        // Make sure this file exists or not
        boolean fileExists = new File(external, filename).exists();
        if (fileExists)
        {
            try {
                // Create a new input stream with this file
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                // Get the list of objects from the file
                storedDataList = (List<BestSellerList>)objectInputStream.readObject();

                // Close the object input stream
                objectInputStream.close();
                // return the list
                return storedDataList;
            }
            catch (FileNotFoundException exception) {
                Log.e(TAG, "Input Stream Exception: ", exception);
            }
            catch (StreamCorruptedException exception) {
                Log.e(TAG, "Stream Exception: ", exception);
            }
            catch (IOException exception) {
                Log.e(TAG, "IO Exception: ", exception);
            }
            catch (ClassNotFoundException exception) {
                Log.e(TAG, "Class Exception: ", exception);
            }
        }
        else {
            // Return nothing
            storedDataList = null;
        }
        return storedDataList;
    }



    private void getBookListBooks(AdapterView<?> parent, int position, ArrayList<BestSellerList> arrayList) {
        // Make sure we are still connected to the internet

        // CHECK THE USERS NETWORK PREFERENCE
        String networkPreference = settingsPreferences.getString("PREF_NETWORK_LIST","network");
        //String preference = String.valueOf(SettingsFragment.listPreference.getValue());

        Log.i(TAG, "This user selected: "+ networkPreference);
        //Log.i(TAG, "Settings Fragment Preference: "+ preference);
        // CHECK THE CONNECTION
        ConnectionChecker connectionChecker = new ConnectionChecker(Main.this);
        boolean isConnected = connectionChecker.canConnectInternet();


        // FOR "NETWORK" PREFERENCE SETTING
        if (networkPreference.equals("network") && (isConnected)) {
            //Log.d(TAG, "Pulling Books from Lists per user's NETWORK preference");
            Toast.makeText(this, "Pulling list from web", Toast.LENGTH_SHORT).show();
            pullBooklistBooksFromWeb(parent, position, arrayList);
        }
        else {
            //Log.d(TAG, "Checking Cache for selected Book List");
            //Get this list's encoded name
            String selectedListName = arrayList.get(position).getEncodedName();
            mBooks = (ArrayList<Book>) pullCachedBooksForList(this, selectedListName + ".txt");
            if (mBooks == null)
            {
                //Log.d(TAG, "No Book List Available in Cache, pulling from web");
                if (networkPreference.equals("check_offline") && (isConnected)) {
                    //Toast.makeText(this, "New List, pulling from web", Toast.LENGTH_SHORT).show();
                    pullBooklistBooksFromWeb(parent, position, arrayList);
                }
                else {
                    //Log.d(TAG, "No Book List Available in Cache and no internet available");
                    Toast.makeText(this, "No cache available, change your preference in settings or connect to the internet.", Toast.LENGTH_LONG).show();
                }
            }
            else {
                //Log.d(TAG, "Loading Best Sellers List from Cache");
                //Toast.makeText(this, "Loading from Cache", Toast.LENGTH_SHORT).show();
                // send the arraylist to the spinner
                // Get the adapter
                ArrayAdapter<Book> arrayAdapter = new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, (mBooks));
                // set the books in the adapter
                BookListFragment.mListview.setAdapter(arrayAdapter);
                BookListFragment.mBooks = mBooks;
            }
        }

    }


    private void pullBooklistBooksFromWeb(AdapterView<?> parent, int position, ArrayList<BestSellerList> arrayList) {
        // bet the list object at the selected position
        Object bookList = parent.getItemAtPosition(position);
        Log.i(TAG, "List = " + bookList);
        // Get the search name for this list
        String encodedName = (arrayList.get(position).getEncodedName());
        // Create the dynamic url
        String bookUrlString = "http://api.nytimes.com/svc/books/v2/lists.json?list-name=" + encodedName + "&api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529";
        // Call the task to get the book details
        GetBooksForSelectedList task = new GetBooksForSelectedList();
        task.execute(bookUrlString, encodedName);
    }

    // SECOND TASK TO GET LIST BOOKS FROM WEB AND STORE IN CACHE
    private class GetBooksForSelectedList extends AsyncTask <String, Integer, ArrayList<Book>> {
        // onPreExecute has access to the class
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected ArrayList<Book> doInBackground(String... params) {
            //Log.d(TAG, "Params 1 = " + params[0] + " and param 2 = " + params [1]);
            // Use the helper to get the data from the passed in url string
            String dataString = HTTPHelper.getData(Main.this, params[0]);
            // get the encoded name passed in
            String encodedListName = params[1];
            // create an arraylist for the mBooks
            mBooks = new ArrayList<Book>();
            try {
                // Create json objects from the returned data
                JSONObject jsonObject = new JSONObject(dataString);
                // Get the array of results
                JSONArray resultsArray = jsonObject.getJSONArray("results");
                // Loop through the best sellers lists
                for (int i = 0; i < resultsArray.length(); i++) {
                    // Get the array object
                    JSONObject list = resultsArray.getJSONObject(i);
                    // get each mBooks properties
                    int rank  = list.getInt("rank");
                    String listType = list.getString("list_name");
                    JSONArray bookDetailsArray = list.getJSONArray("book_details");
                    JSONObject book = bookDetailsArray.getJSONObject(0);
                    String title = book.getString("title");
                    String author = book.getString("author");
                    String description = book.getString("description");
                    // Add the items to the list (rank, title, author, description
                    mBooks.add(Book.newInstance(rank, title, author, description, listType));
                }
                //  write the object to storage using the encoded name
                cacheDataForOfflineUse(encodedListName + ".txt", mBooks);
                // return the list
                return mBooks;
            }
            catch (JSONException exception) {
                Log.e(TAG, "JSON exception" + exception);
            }

            // Return the list to the array
            return mBooks;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> arrayList) {
            // Create a simple array adapter for the listview items
            ArrayAdapter<Book> arrayAdapter = new ArrayAdapter<Book>(Main.this, android.R.layout.simple_list_item_1, (arrayList));
            // set the adapter to the listview
            BookListFragment.mListview.setAdapter(arrayAdapter);
            BookListFragment.mBooks = mBooks;
        }
    }

    // RETRIEVE LISTVIEW BOOK ITEMS FROM CACHE FOR OFFLINE USE
    private Object pullCachedBooksForList (Context context, String filename) {
        List<Book> storedListBooks = null;
        File external = context.getExternalFilesDir(null);
        // Find the correct file for this list
        File file = new File(external, filename);
        // see if this file exists or not
        boolean fileExists = new File(external, filename).exists();
        //Log.d(TAG, "This file exists: " + fileExists);
        if (fileExists) {
            try {
                // Create a new input stream with this file
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                // Get the list of objects from the file
                storedListBooks = (List<Book>)objectInputStream.readObject();
                // Close and return the list
                objectInputStream.close();
                return storedListBooks;
            }
            catch (FileNotFoundException exception) {
                Log.e(TAG, "Input Stream Exception: ", exception);
                storedListBooks = null;
            }
            catch (StreamCorruptedException exception) {
                Log.e(TAG, "Stream Exception: ", exception);
            }
            catch (IOException exception) {
                Log.e(TAG, "IO Exception: ", exception);
            }
            catch (ClassNotFoundException exception) {
                Log.e(TAG, "Class Exception: ", exception);
            }
        }
        else {
            storedListBooks = null;
        }
        return storedListBooks;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // check what was selected
        switch (item.getItemId())
        {
            // (only "Preferences" right now...
            case R.id.action_settings:
                // Load the preference fragment
                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, new SettingsFragment())
                        // make sure we can get back to the main view
                        .addToBackStack("settings")
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void setBookDetails(String title, String author, String description, int rank, String listType) {
        // Get the passed over data
        //Log.i(TAG, "Retrieved title: " + title + " author: " + author + " rank: " + rank + " description: " + description);
        // Get the detail fragment
        DetailFragment detailFragment = (DetailFragment) getFragmentManager().findFragmentByTag(DetailFragment.TAG);
        // check if it was created
        if (detailFragment == null) {
            // Then create it
            detailFragment = DetailFragment.newInstance(title, author, description, rank, listType);
            // Set the fragment within the container
            getFragmentManager().beginTransaction().replace(R.id.book_detail_container, detailFragment, DetailFragment.TAG).commit();
        } else {
            // pass the data to the current fragment
            detailFragment.setBookData(title, author, description, rank, listType);
        }

    }

}
