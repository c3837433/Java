package com.example.angessmith.navdrawerapp.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.example.angessmith.navdrawerapp.Main;
import com.example.angessmith.navdrawerapp.R;
import com.example.angessmith.navdrawerapp.StoryListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

// Created by AngeSSmith on 10/19/14.


public class StoriesFragment  extends Fragment {

    private static final String ARG_POSITION = "StoriesFragment.POSITION";
    private static ArrayList<HashMap<String, Object>> mStoryList;

    // Create the instance of the fragment
    public static StoriesFragment newInstance(int section, ArrayList<HashMap<String, Object>> storyList) {
        StoriesFragment fragment = new StoriesFragment();
        mStoryList = storyList;
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, section);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stories_fragment, container, false);
        return rootView;
    }


    // Set the title from the passed in section
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_POSITION));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get the listview in the fragment
        ListView listView = (ListView) getActivity().findViewById(R.id.story_listView);
        // create a new custom adapter and set it with the passed arraylist
        listView.setAdapter(new StoryListAdapter(getActivity(), mStoryList));
    }

}