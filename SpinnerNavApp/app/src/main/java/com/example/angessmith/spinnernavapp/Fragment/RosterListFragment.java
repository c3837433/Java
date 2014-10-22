package com.example.angessmith.spinnernavapp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.angessmith.spinnernavapp.R;
import com.example.angessmith.spinnernavapp.RosterClass;

import java.util.ArrayList;


// Created by AngeSSmith on 10/21/14.

public class RosterListFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_ARRAY_LIST = "rosterLists";
    private static final String TAG = "RosterListFragment";
    private static ListView mRosterList;

    public static RosterListFragment newInstance(int sectionNumber, ArrayList<RosterClass> rosterList) {
        RosterListFragment fragment = new RosterListFragment();
        // Log out the returned data
        Log.i(TAG, "SELECTED SECTION: " + sectionNumber + " AND LIST INFO: " + rosterList);
        Bundle args = new Bundle();
        args.putSerializable(ARG_ARRAY_LIST, rosterList);
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public RosterListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment
        return inflater.inflate(R.layout.roster_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);
        // Check the bundle
        Bundle arguments = getArguments();
        if (arguments != null)
        {
            ArrayList<RosterClass> rosterList = (ArrayList<RosterClass>) arguments.getSerializable(ARG_ARRAY_LIST);
            int rosterPostion = arguments.getInt(ARG_SECTION_NUMBER);
            // Get the listview
            mRosterList = (ListView) getView().findViewById(R.id.roster_listView);
            String[] teamMates = rosterList.get(rosterPostion -1).getTeamPlayers();
            // Create the array adapter with this team
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,teamMates);
            // and set the adapter the the list
            mRosterList.setAdapter(arrayAdapter);
        }
    }
}
