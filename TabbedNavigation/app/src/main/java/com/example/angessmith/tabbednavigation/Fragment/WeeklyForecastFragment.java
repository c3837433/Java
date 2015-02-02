package com.example.angessmith.tabbednavigation.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
    private static final String ARG_WEEKLY_LIST = "WeeklyForecastFragment.LIST";
    private static ArrayList<HashMap<String, Object>> mWeeklyList;
    private static ListView mListView;
    private static Context mContext;

    public static WeeklyForecastFragment newInstance(int sectionNumber) {
        WeeklyForecastFragment fragment = new WeeklyForecastFragment();
        Bundle args = new Bundle();
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
        // Check the bundle
        Bundle bundleArguments = getArguments();
        // Check the argument validity
        if ((bundleArguments != null)  && (bundleArguments.getSerializable(ARG_WEEKLY_LIST) != null)) {
            // update the list
            mWeeklyList = (ArrayList<HashMap<String, Object>>) getArguments().getSerializable(ARG_WEEKLY_LIST);
            Log.i("WEEKLY FRAGMENT", "Saved instance state found condition, reloading data");
            // reset it in the listview
            mListView.setAdapter(new WeeklyListviewAdapter(mContext, mWeeklyList));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the condition
        getArguments().putSerializable(ARG_WEEKLY_LIST,  mWeeklyList);
    }

    public static void setDaysInWeekView(ArrayList<HashMap<String, Object>> list) {
        mWeeklyList = list;
        //Log.i("Weekly Fragment", "Passed in data = " + list);
        // set the data with the weekly adapter
        mListView.setAdapter(new WeeklyListviewAdapter(mContext, list));

    }
}
