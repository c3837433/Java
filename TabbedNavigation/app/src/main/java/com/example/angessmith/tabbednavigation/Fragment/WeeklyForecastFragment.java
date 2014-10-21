package com.example.angessmith.tabbednavigation.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.angessmith.tabbednavigation.R;

 // Created by AngeSSmith on 10/20/14.
// http://api.wunderground.com/api/3d402f1818f340e0/forecast10day/q/CA/San_Francisco.json

public class WeeklyForecastFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static WeeklyForecastFragment newInstance(int sectionNumber) {
        WeeklyForecastFragment fragment = new WeeklyForecastFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public WeeklyForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the fragment in the view
        return inflater.inflate(R.layout.weekly_fragment, container, false);
    }
}
