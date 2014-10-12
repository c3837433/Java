package com.example.angessmith.multiactivity.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.angessmith.multiactivity.R;


// Created by AngeSSmith on 10/12/14 for Java 2 Week 3 Term 1410.

public class GiftListFragment extends Fragment {

    // create fragment tag
    public static final String TAG = "GiftListFragment.TAG";

    // Create the factory
    public static GiftListFragment newInstance() {
        GiftListFragment fragment = new GiftListFragment();
        return fragment;
    }

    // SET UP THE FRAGMENT LAYOUT
    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        // Return the GiftListFragment
        return _inflater.inflate(R.layout.gift_list_fragment, _container, false);
    }
}
