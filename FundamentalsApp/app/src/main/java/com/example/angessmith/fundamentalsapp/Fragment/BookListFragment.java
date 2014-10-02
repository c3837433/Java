package com.example.angessmith.fundamentalsapp.Fragment;

//Created by AngeSSmith on 9/30/14 for Java 2 Term 1410.
// Book (Animal) best seller api http://api.nytimes.com/svc/mBooks/v2/lists.json?list-name=animals&api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.angessmith.fundamentalsapp.BestSellerList;
import com.example.angessmith.fundamentalsapp.Book;
import com.example.angessmith.fundamentalsapp.ConnectionChecker;
import com.example.angessmith.fundamentalsapp.HTTPHelper;
import com.example.angessmith.fundamentalsapp.R;

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



public class BookListFragment extends Fragment implements AdapterView.OnItemClickListener {

    // Create the TAG
    public static final String TAG = "BookListFragment.TAG";
    private OnListItemClickListener mListener;
    Spinner mSpinner;
    ListView mListview;
    ArrayList<Book> mBooks;

    // Create a factory instance of the fragment
    public static BookListFragment newInstance() {
        // Create a new instance of the fragment
        BookListFragment fragment = new BookListFragment();
        // and return it
        return fragment;
    }

    // SET UP ON LIST ITEM CLICK TO GET DETAILS FOR THAT BOOK
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // get the passed in object
        String title = mBooks.get(position).getTitle();
        String author = mBooks.get(position).getAuthor();
        String description = mBooks.get(position).getDescription();
        int rank = mBooks.get(position).getRank();
        String listType = mBooks.get(position).getList();
        // and send it to the main view
        mListener.setBookDetails(title, author, description, rank, listType);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Make sure the activity attaching this has the OnListItemClickListener
        if (activity instanceof OnListItemClickListener) {
            // set the listener
            mListener = (OnListItemClickListener) activity;
        } else {
            // if it does not implement this, throw an exception
            throw new IllegalArgumentException("You must implement the OnListItemClickListener");
        }

    }

    // DEFINE THE INTERFACE
    public interface OnListItemClickListener {

        public void setBookDetails(String title, String author, String description, int rank, String list);
    }

    // SET THE FRAGMENT IN THE LAYOUT
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle _savedInstanceState) {
        View view = inflater.inflate(R.layout.book_list_fragment, container, false);
        return view;
    }

    // SET UP BUTTON TO CHECK FOR INTERNET AND PULL BEST SELLER LISTS FROM WEB OR CACHE FOR SPINNER
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Access the fragment view
        View view = getView();
        // get the spinner and listview for when we have data
        assert view != null;
        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        mListview = (ListView) view.findViewById(R.id.bookslist);
        // set the on click listener to the listview
        mListview.setOnItemClickListener(this);
        // Access the button
        final Button getBestSellerButton = (Button) view.findViewById(R.id.get_list_button);
        getBestSellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "User clicked the button to get data");
                // Create an instance of the connection checker. After the fragment is commited, getActivity gets the context
                ConnectionChecker connectionChecker = new ConnectionChecker(getActivity());
                // check if connected
                boolean isConnected = connectionChecker.canConnectInternet();
                if (isConnected) {
                    Toast.makeText(getActivity(),"Getting Lists from Web",Toast.LENGTH_SHORT).show();
                    // run the async task to get the list of array objects
                    String bookListString = "http://api.nytimes.com/svc/books/v2/lists/names.json?api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529";
                    // Get an instance of the ASYNC TASK to get the data
                    GetBookListsTask task = new GetBookListsTask();
                    // execute the task
                    task.execute(bookListString);
                }
                else {
                    // User is not connected, pull data from cache
                    Toast.makeText(getActivity(),"No internet connection, checking cache.",Toast.LENGTH_SHORT).show();
                    // Get the data from the cache
                    ArrayList<BestSellerList> cachedList = (ArrayList<BestSellerList>) pullCachedBestSellerListsForOffline(getActivity(), "bestSellersList.txt");
                    if (cachedList == null)
                    {
                        Toast.makeText(getActivity(), "No cache available, connect to the internet and try again.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        // send the arraylist to the spinner
                        setSellerListsInSpinner(cachedList);
                    }
                }
            }
        });

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
            String dataString = HTTPHelper.getData(getActivity(), params[0]);
            ArrayList<BestSellerList> bestSellerList = new ArrayList<BestSellerList>();
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
            setSellerListsInSpinner(arrayList);

        }
    }

    // SET BEST SELLER LISTS IN SPINNER, ADD ON CLICK LISTENER TO GET THAT LIST BOOKS FROM WEB OR OFFLINE CACHE
    private void setSellerListsInSpinner(final ArrayList<BestSellerList> arrayList) {
        ArrayAdapter<BestSellerList> arrayAdapter = new ArrayAdapter<BestSellerList>(getActivity(), android.R.layout.simple_spinner_dropdown_item, (arrayList));
        // Set the adapter to the spinner
        mSpinner.setAdapter(arrayAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // get the books for the list view
                getBookListBooks(parent, position, arrayList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    // CACHE BEST SELLER LISTS OR LIST BOOKS IN EXTERNAL STORAGE FOR OFFLINE USE
    private void cacheDataForOfflineUse(String filename, Object object) {
        File externalFilesDir = getActivity().getExternalFilesDir(null);
        Log.d(TAG, "File directory = " + externalFilesDir);
        File file = new File(externalFilesDir, filename);
        //File file;
        try {
            // file = File.createTempFile(filename, null, getActivity().getCacheDir());
            Log.d(TAG, "File directory = " + file);
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
        // Reaccess the external file directory
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
        ConnectionChecker connectionChecker = new ConnectionChecker(getActivity());
        boolean isConnected = connectionChecker.canConnectInternet();
        if (isConnected) {
            Toast.makeText(getActivity(), "Getting List from Web", Toast.LENGTH_SHORT).show();
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
            Log.d(TAG, "Encoded name = " + encodedName);
        } else {
            // User is not connected, pull data from cache
            Toast.makeText(getActivity(), "No internet connection, checking cache.", Toast.LENGTH_SHORT).show();
            //Get this list's encoded name
            String selectedListName = arrayList.get(position).getEncodedName();
            ArrayList<Book> cachedBooks = (ArrayList<Book>) pullCachedBooksForList(getActivity(), selectedListName + ".txt");
            if (cachedBooks == null)
            {
                // Than this information has not been cached yet, alert the user
                Toast.makeText(getActivity(), "This list has not been retrieved yet. Connect to the internet and try again.", Toast.LENGTH_SHORT).show();
            }
            else {
                // Get the adapter
                ArrayAdapter<Book> arrayAdapter = new ArrayAdapter<Book>(getActivity(), android.R.layout.simple_list_item_1, (cachedBooks));
                // set the books in the adapter
                mListview.setAdapter(arrayAdapter);
            }
        }
    }

    // SECOND TASK TO GET LIST BOOKS FROM WEB AND STORE IN CACHE
    private class GetBooksForSelectedList extends AsyncTask <String, Integer, ArrayList<Book>> {
        // onPreExecute has access to the class
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected ArrayList<Book> doInBackground(String... params) {
            Log.d(TAG, "Params 1 = " + params[0] + " and param 2 = " + params [1]);
            // Use the helper to get the data from the passed in url string
            String dataString = HTTPHelper.getData(getActivity(), params[0]);
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
            ArrayAdapter<Book> arrayAdapter = new ArrayAdapter<Book>(getActivity(), android.R.layout.simple_list_item_1, (arrayList));
            // set the adapter to the listview
            mListview.setAdapter(arrayAdapter);
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
        Log.d(TAG, "This file exists: " + fileExists);
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

}
