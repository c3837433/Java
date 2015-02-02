package com.example.angessmith.tabbednavigation.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.angessmith.tabbednavigation.HourlyListviewAdapter;
import com.example.angessmith.tabbednavigation.R;

import java.util.ArrayList;
import java.util.HashMap;


//Created by AngeSSmith on 10/20/14.
// http://api.wunderground.com/api/3d402f1818f340e0/hourly10day/q/CA/San_Francisco.json

public class HourlyForecastFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_HOURLY_LIST = "HourlyForecastFragment.LIST";
    private static ArrayList<HashMap<String, Object>> mHourlyList;
    private static ListView mListView;
    private static Context mContext;
    public static HourlyForecastFragment newInstance(int sectionNumber) {
        HourlyForecastFragment fragment = new HourlyForecastFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HourlyForecastFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the fragment
        return inflater.inflate(R.layout.hourly_fragment, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        Log.i("HOURLY FORECAST", "Loading Activity in On Create");

    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);
        Log.i("HOURLY FORECAST", "Loading Activity in OnActivityCreated");
        // Get the list view ready
        mListView = (ListView) getView().findViewById(R.id.hourly_listView);
        mContext = getActivity();
        // See if we have saved values to use
        Bundle bundleArguments = getArguments();
        // Check the argument validity
        if ((bundleArguments != null)  && (bundleArguments.getSerializable(ARG_HOURLY_LIST) != null)) {
            // update the list
            mHourlyList = (ArrayList<HashMap<String, Object>>) getArguments().getSerializable(ARG_HOURLY_LIST);
            Log.i("HOURLY FORECAST", "Saved intance state found condition, reloading data");
            // reset it in the listview
            mListView.setAdapter(new HourlyListviewAdapter(mContext, mHourlyList));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the condition
        getArguments().putSerializable(ARG_HOURLY_LIST,  mHourlyList);
    }

    public static void SetCustomListInAdapter(ArrayList<HashMap<String, Object>> forecastList) {
        mHourlyList = forecastList;
        // set the data with the custom adapter
        mListView.setAdapter(new HourlyListviewAdapter(mContext, forecastList));

    }
}
