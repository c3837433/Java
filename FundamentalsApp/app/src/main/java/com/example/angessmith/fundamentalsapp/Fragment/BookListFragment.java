package com.example.angessmith.fundamentalsapp.Fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.angessmith.fundamentalsapp.BestSellerList;
import com.example.angessmith.fundamentalsapp.Book;
import com.example.angessmith.fundamentalsapp.ConnectionChecker;
import com.example.angessmith.fundamentalsapp.HTTPHelper;
import com.example.angessmith.fundamentalsapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


//Created by AngeSSmith on 9/30/14 for Java 2 Term 1410.
// Book (Animal) best seller api http://api.nytimes.com/svc/books/v2/lists.json?list-name=animals&api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529


public class BookListFragment extends Fragment {

    // Create the TAG
    public static final String TAG = "BookListFragment.TAG";
    Spinner mSpinner;
    private ArrayList<Book> mBook;

    // Create a factory instance of the fragment
    public static BookListFragment newInstance() {
        // Create a new instance of the fragment
        BookListFragment fragment = new BookListFragment();
        // and return it
        return fragment;
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
        // get the spinner
        assert view != null;
        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        // Access the button
        Button getBestSellerButton = (Button) view.findViewById(R.id.get_list_button);
        getBestSellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "User clicked the button to get data");
                // Create an instance of the connection checker. After the fragment is commited, getActivity gets the context
                ConnectionChecker connectionChecker = new ConnectionChecker(getActivity());
                // check if connected
                boolean isConnected = connectionChecker.canConnectInternet();
                if (isConnected) {
                    Log.d(TAG, "User is connected, getting data");
                    // run the async task to get the list of array objects
                    String bookListString = "http://api.nytimes.com/svc/books/v2/lists/names.json?api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529";
                    // Get an instance of the ASYNC TASK to get the data
                    GetBookListsTask task = new GetBookListsTask();
                    // execute the task
                    task.execute(bookListString);
                }
                else {
                    // TODO: READ DATA FROM CACHE FILE
                    Log.d(TAG, "User is NOT connected, need to pull data from file");
                }
            }
        });

    }
    private void writeDataToCacheFile(String data, String filename) {
        //File externalFilesDir = getActivity().getExternalFilesDir(null);
        //Log.d(TAG, "File directory = " + externalFilesDir);
        //File file = new File(externalFilesDir, filename);
        File file;
        try {
            file = File.createTempFile(filename, null, getActivity().getCacheDir());
            Log.d(TAG, "File directory = " + file);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            // Write the data to the file
            outputStream.writeObject(data);
            // close the stream
            outputStream.close();
        }
        catch(Exception exception) {
            // There was an error writing the file
            Log.e(TAG, "Output Stream Exception: ", exception);
        }
    }

    private void getDataFromCacheFile() {
        // TODO: GET DATA FROM CACHE WHEN CONNECTION IS NOT AVAILABLE

        // TODO: HANDLE ERRORS WHEN CACHE IS EMPTY

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
            writeDataToCacheFile(dataString, "bookList.txt");
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
            ArrayAdapter<BestSellerList> arrayAdapter = new ArrayAdapter<BestSellerList>(getActivity(), android.R.layout.simple_spinner_dropdown_item, (arrayList));
            // Set the adapter to the spinner
            mSpinner.setAdapter(arrayAdapter);

        }
    }

}
