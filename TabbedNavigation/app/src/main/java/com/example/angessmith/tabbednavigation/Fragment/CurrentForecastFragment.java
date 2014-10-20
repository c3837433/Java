package com.example.angessmith.tabbednavigation.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.angessmith.tabbednavigation.R;


// Created by AngeSSmith on 10/20/14.

public class CurrentForecastFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static CurrentForecastFragment newInstance(int sectionNumber) {
        CurrentForecastFragment fragment = new CurrentForecastFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public CurrentForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // get the current view
        return inflater.inflate(R.layout.current_fragment, container, false);
    }

}
