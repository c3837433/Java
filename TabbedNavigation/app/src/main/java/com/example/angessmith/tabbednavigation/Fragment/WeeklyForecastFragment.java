package com.example.angessmith.tabbednavigation.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.angessmith.tabbednavigation.R;
import com.example.angessmith.tabbednavigation.WeeklyListviewAdapter;

import java.util.ArrayList;
import java.util.HashMap;

// Created by AngeSSmith on 10/20/14.
// http://api.wunderground.com/api/3d402f1818f340e0/forecast10day/q/CA/San_Francisco.json

public class WeeklyForecastFragment extends Fragment {
    // Define the properties
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static ListView mListView;
    private static Context mContext;

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

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);
        // set the context and listview properties so the area accessible
        mContext = getActivity();
        mListView = (ListView) getView().findViewById(R.id.weekly_listView);
    }

    public static void setDaysInWeekView(ArrayList<HashMap<String, Object>> list) {
        //Log.i("Weekly Fragment", "Passed in data = " + list);
        // set the data with the weekly adapter
        mListView.setAdapter(new WeeklyListviewAdapter(mContext, list));

    }
}
