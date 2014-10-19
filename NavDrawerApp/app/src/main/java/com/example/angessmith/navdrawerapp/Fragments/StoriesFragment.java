package com.example.angessmith.navdrawerapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.angessmith.navdrawerapp.R;

 // Created by AngeSSmith on 10/19/14.


public class StoriesFragment  extends Fragment {

    public static final String TAG = "StoriesFragment";

    // Create the instance of the fragment
    public static StoriesFragment newInstance() {
        return new StoriesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stories_fragment, container, false);
        return rootView;
    }

}