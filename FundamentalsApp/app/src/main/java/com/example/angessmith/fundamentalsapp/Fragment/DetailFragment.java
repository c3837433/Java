package com.example.angessmith.fundamentalsapp.Fragment;

//Created by AngeSSmith on 9/30/14 for Java 2 Term 1410.

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.angessmith.fundamentalsapp.R;

public class DetailFragment extends Fragment{

    // Create the tag for this fragment
    public static final String TAG = "DetailFragment.TAG";

    // Create the instance of the fragment
    public static DetailFragment newInstance() {

        DetailFragment fragment = new DetailFragment();
        return fragment;
    }

    // Get access to the main activity
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle _savedInstanceState) {
        // Inflate the second container with this view
        View view = inflater.inflate(R.layout.book_details_fragment, container, false);
        return view;
    }
}
