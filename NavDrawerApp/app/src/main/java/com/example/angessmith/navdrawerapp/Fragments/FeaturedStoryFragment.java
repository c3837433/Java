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

    private static final String ARG_POSITION = "FeaturedStoryFragment.POSITION";

    // Create the instance of the fragment
    public static FeaturedStoryFragment newInstance(int section) {
        FeaturedStoryFragment fragment = new FeaturedStoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, section);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.featured_fragment, container, false);
        return rootView;
    }

    // Set the title from the passed in section
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_POSITION));
    }

}
