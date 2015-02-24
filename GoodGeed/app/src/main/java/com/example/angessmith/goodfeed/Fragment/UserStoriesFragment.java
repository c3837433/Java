package com.example.angessmith.goodfeed.Fragment;


// Created by AngeSSmith on 12/11/14.
// Fragment that displays the users stories within a list view for the user detail activity

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.angessmith.goodfeed.CustomUserStoriesAdapter;
import com.example.angessmith.goodfeed.R;
import com.example.angessmith.goodfeed.StoryPost;

import java.util.List;

public class UserStoriesFragment extends ListFragment {

    public static final String TAG = "UserStoriesFragment";
    public List<StoryPost> mStoryList;
    CustomUserStoriesAdapter mAdapter;



    // Set up the fragment
    public static UserStoriesFragment newInstance(List<StoryPost> list) {
        // Get and set the author
        UserStoriesFragment fragment = new UserStoriesFragment();
        fragment.mStoryList = list;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the container
        return inflater.inflate(R.layout.fragment_user_stories, null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new CustomUserStoriesAdapter(getActivity(),mStoryList);
        setListAdapter(mAdapter);
    }
}
