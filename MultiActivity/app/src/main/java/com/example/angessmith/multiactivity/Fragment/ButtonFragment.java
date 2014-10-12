package com.example.angessmith.multiactivity.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.angessmith.multiactivity.R;

// Created by AngeSSmith on 10/12/14 for Java 2 Term 1410.

public class ButtonFragment  extends Fragment {

    // Create tag
    public static final String TAG = "ButtonFragment.TAG";

    // Create factory
    public static ButtonFragment newInstance() {
        ButtonFragment fragment = new ButtonFragment();
        return fragment;
    }


    // Add layout
    @Override
    public View onCreateView (LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        // return the button layout
        return _inflater.inflate(R.layout.button_fragment, _container, false);
    }
}
