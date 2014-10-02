package com.example.angessmith.fundamentalsapp.Fragment;

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
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;


//Created by AngeSSmith on 9/30/14 for Java 2 Term 1410.
// Book (Animal) best seller api http://api.nytimes.com/svc/mBooks/v2/lists.json?list-name=animals&api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529


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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // get the passed in object
        String title = mBooks.get(position).getTitle();
        String author = mBooks.get(position).getAuthor();
        String description = mBooks.get(position).getDescription();
        int rank = mBooks.get(position).getRank();
        // and send it to the main view
        mListener.setBookDetails(title, author, description, rank);
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
    // Define the on item listener interface
    public interface OnListItemClickListener {

        public void setBookDetails(String title, String author, String description, int rank);
    }

    // Get access to the activity layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle _savedInstanceState) {
        View view = inflater.inflate(R.layout.book_list_fragment, container, false);
        return view;
    }

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
                    Toast.makeText(getActivity(),"User is connected, getting data",Toast.LENGTH_SHORT).show();
                    // run the async task to get the list of array objects
                    String bookListString = "http://api.nytimes.com/svc/books/v2/lists/names.json?api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529";
                    // Get an instance of the ASYNC TASK to get the data
                    GetBookListsTask task = new GetBookListsTask();
                    // execute the task
                    task.execute(bookListString);
                }
                else {
                    // User is not connected, pull data from cache
                    Toast.makeText(getActivity(),"User is NOT connected, trying to pull data from file",Toast.LENGTH_SHORT).show();
                    // Get the data from the cache
                    ArrayList<BestSellerList> cachedList = (ArrayList<BestSellerList>) pullCachedDataForOfflineUse(getActivity(), "bookList.txt");
                    // send the arraylist to the spinner
                    setArrayInList(cachedList);
                }
            }
        });

    }
    private void cacheDataForOfflineUse(String data, String filename, Object object) {
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

    private Object pullCachedDataForOfflineUse(Context context, String filename) {
        List<BestSellerList> storedDataList = null;
        // Reaccess the external file directory
        File external = context.getExternalFilesDir(null);
        // And find the file with the passed in name
        File file = new File(external, filename);
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
        if (storedDataList == null)
        {
            // Inform the user we have no data, we need internet
            Toast.makeText(getActivity(),"Internet is required, no cached data to view.",Toast.LENGTH_SHORT).show();
        }
        return storedDataList;

    }


    private class GetBookListsTask extends AsyncTask<String, Integer, ArrayList<BestSellerList>> {
        // onPreExecute has access to the class
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected ArrayList<BestSellerList> doInBackground(String... params) {
            // Use the HTTP Manager class to get the api data
            String dataString = HTTPHelper.getData(getActivity(), params[0]);
            ArrayList<BestSellerList> bestSellerList = new ArrayList<BestSellerList>();
            //cacheDataForOfflineUse(dataString, "bookList.txt");
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
                cacheDataForOfflineUse(dataString, "bookList.txt", bestSellerList);
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
            setArrayInList(arrayList);

        }
    }

    private void setArrayInList(final ArrayList<BestSellerList> arrayList) {
        ArrayAdapter<BestSellerList> arrayAdapter = new ArrayAdapter<BestSellerList>(getActivity(), android.R.layout.simple_spinner_dropdown_item, (arrayList));
        // Set the adapter to the spinner
        mSpinner.setAdapter(arrayAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Make sure we are still connected to the internet
                ConnectionChecker connectionChecker = new ConnectionChecker(getActivity());
                boolean isConnected = connectionChecker.canConnectInternet();
                if (isConnected) {
                    Toast.makeText(getActivity(), "User still connected, getting book info", Toast.LENGTH_SHORT).show();
                    // bet the list object at the selected position
                    Object bookList = parent.getItemAtPosition(position);
                    Log.i(TAG, "List = " + bookList);
                    // Get the search name for this list
                    String encodedName = (arrayList.get(position).getEncodedName());
                    // Create the dynamic url
                    String bookUrlString = "http://api.nytimes.com/svc/books/v2/lists.json?list-name=" + encodedName + "&api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529";
                    // Call the task to get the book details
                    GetBookDetails task = new GetBookDetails();
                    task.execute(bookUrlString);
                    Log.d(TAG, "Encoded name = " + encodedName);
                } else {
                    // User is not connected, pull data from cache
                    Toast.makeText(getActivity(), "User is NOT connected, trying to pull data from file", Toast.LENGTH_SHORT).show();
                    // TODO: Get the data from the cache

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }

    private class GetBookDetails extends AsyncTask <String, Integer, ArrayList<Book>> {
        // onPreExecute has access to the class
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected ArrayList<Book> doInBackground(String... params) {
            // Use the helper to get the data from the passed in url string
            String dataString = HTTPHelper.getData(getActivity(), params[0]);
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
                    JSONArray bookDetailsArray = list.getJSONArray("book_details");
                    JSONObject book = bookDetailsArray.getJSONObject(0);
                    String title = book.getString("title");
                    String author = book.getString("author");
                    String description = book.getString("description");
                    // Add the items to the list (rank, title, author, description
                    mBooks.add(Book.newInstance(rank, title, author, description));
                }
                //  write the object to storage
                cacheDataForOfflineUse(dataString, "mBooks.txt", mBooks);
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

}
