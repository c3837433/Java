package com.example.angessmith.fundamentalsapp.Fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.angessmith.fundamentalsapp.BestSellerList;
import com.example.angessmith.fundamentalsapp.Book;
import com.example.angessmith.fundamentalsapp.ConnectionChecker;
import com.example.angessmith.fundamentalsapp.HTTPHelper;
import com.example.angessmith.fundamentalsapp.Main;
import com.example.angessmith.fundamentalsapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.BindException;
import java.util.ArrayList;


//Created by AngeSSmith on 9/30/14 for Java 2 Term 1410.
// Book (Animal) best seller api http://api.nytimes.com/svc/books/v2/lists.json?list-name=animals&api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529


public class BookListFragment extends Fragment {

    // Create the TAG
    public static final String TAG = "BookListFragment.TAG";
    Spinner mSpinner;
    private ArrayList<Book> mBook;
    private ArrayList<BestSellerList> mBestSellers;
    String displayName;

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

        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        // TODO: GET THE LIST OF BOOK OBJECTS, CREATE ARRAY OF NAMES
        // Create an instance of the connection checker. After the fragment is commited, getActivity gets the context
        ConnectionChecker connectionChecker = new ConnectionChecker(getActivity());
        // check if connected
        boolean isConnected = connectionChecker.canConnectInternet();
        if (isConnected) {
            // run the async task to get the list of array objects
            String bookListString = "http://api.nytimes.com/svc/books/v2/lists/names.json?api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529";
        }
        // TODO: PUT ITEMS IN ARRAY ADAPTER
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

            // TODO: WRITE STRING TO FILE

            // TODO: CREATE AND SAVE RETURNED STRING TO JSON OBJECT
            /* Convert the data to JSON Object
            try {
                jsonObject = new JSONObject(dataString);
                Log.d(TAG, jsonObject.toString());
            }
            catch (JSONException e) {
                // There was an error, empty out the object
                jsonObject = null;
                Log.e(TAG, "Unable to convert to JSON");
            }
            */
            // Return the JSON object

            /*
            JSONObject jsonObject = HTTPHelper.getApiData(mContext, params[0]);
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
            */
        }
        // When we are done getting the data
        @Override
        protected void onPostExecute(ArrayList<BestSellerList> bookList) {

        }
    }

}
