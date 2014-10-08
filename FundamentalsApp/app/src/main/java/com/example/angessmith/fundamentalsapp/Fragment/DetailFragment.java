package com.example.angessmith.fundamentalsapp.Fragment;

//Created by AngeSSmith on 9/30/14 for Java 2 Term 1410.

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.angessmith.fundamentalsapp.R;

public class DetailFragment extends Fragment {

    // Create the tag for this fragment
    public static final String TAG = "DetailFragment.TAG";
    // And the bundle argument keys
    public static final String ARG_TITLE = "DetailFragment.ARG_TITLE";
    public static final String ARG_AUTHOR = "DetailFragment.ARG_AUTHOR ";
    public static final String ARG_DESC = "DetailFragment.ARG_DESC";
    public static final String ARG_RANK = "DetailFragment.ARG_RANK";
    public static final String ARG_LIST = "DetailFragment.ARG_LIST";

    // Create the instance of the fragment
    public static DetailFragment newInstance(String title, String author, String description, int rank, String listType) {

        DetailFragment fragment = new DetailFragment();

        // Create the bundle
        Bundle bundleArguments = new Bundle();
        bundleArguments.putString(ARG_TITLE, title);
        bundleArguments.putString(ARG_AUTHOR, author);
        bundleArguments.putString(ARG_DESC, description);
        bundleArguments.putInt(ARG_RANK, rank);
        bundleArguments.putString(ARG_LIST, listType);
        // save the bundle arguments in the fragment
        fragment.setArguments(bundleArguments);
        return fragment;
    }

    // Get access to the main activity and set up the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle _savedInstanceState) {
        // Inflate the second container with this view
        return inflater.inflate(R.layout.book_details_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Load the book data from the bundle
        Bundle bA = getArguments();
        // IF they are there
        if (bA != null) {
            // Get the values and set it to the view
            setBookData(bA.getString(ARG_TITLE), bA.getString(ARG_AUTHOR), bA.getString(ARG_DESC), bA.getInt(ARG_RANK), bA.getString(ARG_LIST));
        }

    }

    public void setBookData(String title, String author, String description, int rank, String listType) {
        // Update the arguments each time the device is rotated
        getArguments().putString(ARG_TITLE, title);
        getArguments().putString(ARG_AUTHOR, author);
        getArguments().putString(ARG_DESC, description);
        getArguments().putInt(ARG_RANK, rank);
        getArguments().putString(ARG_LIST, listType);

        // Get the textviews
        TextView titleView = (TextView) getView().findViewById(R.id.book_title);
        TextView authorView = (TextView) getView().findViewById(R.id.book_author);
        TextView descView = (TextView) getView().findViewById(R.id.book_description);
        TextView rankView = (TextView) getView().findViewById(R.id.book_rank);

        // set the values to the views
        titleView.setText(title);
        authorView.setText("By: " + author);
        descView.setText(description);
        rankView.setText("Ranked # " + String.valueOf(rank) + " on the New York Times " + listType + " List");

    }
}
