package com.example.angessmith.navdrawerapp.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.angessmith.navdrawerapp.Main;
import com.example.angessmith.navdrawerapp.R;


// Created by AngeSSmith on 10/18/14.

public class FeaturedStoryFragment  extends Fragment {

    public static final String TAG = "FeaturedStoryFragment";

    // Create the instance of the fragment
    public static FeaturedStoryFragment newInstance() {
        return new FeaturedStoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.featured_fragment, container, false);
        return rootView;
    }

}
