package com.example.angessmith.navdrawerapp.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angessmith.navdrawerapp.Main;
import com.example.angessmith.navdrawerapp.NewsStory;
import com.example.angessmith.navdrawerapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


// Created by AngeSSmith on 10/18/14.

public class FeaturedStoryFragment  extends Fragment {

    private static final String ARG_POSITION = "FeaturedStoryFragment.POSITION";
    private static final String ARG_FEATURE = "FeaturedStoryFragment.FEATURE";

    // Define the views
    TextView mTitleView;
    TextView mTimeView;
    TextView mTextView;
    ImageView mImageView;


    // Create the instance of the fragment
    public static FeaturedStoryFragment newInstance(int section, NewsStory featuredStory) {
        // Create the story
        FeaturedStoryFragment fragment = new FeaturedStoryFragment();
        Log.i("Feature Fragment", "Story Title on Fragment = " + featuredStory.getTitle());
        // Save the items
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, section);
        args.putSerializable(ARG_FEATURE, featuredStory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.featured_fragment, container, false);
        return rootView;
    }

    // Set the title from the passed in section
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Set the page title
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_POSITION));
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);
        // Get the views
        mTitleView = (TextView) getView().findViewById(R.id.feature_title);
        mTimeView = (TextView) getView().findViewById(R.id.feature_time);
        mTextView = (TextView) getView().findViewById(R.id.feature_story);
        mImageView = (ImageView) getView().findViewById(R.id.feature_image);
        // Set the passed in story to the views
        setFeatureStory((NewsStory) getArguments().getSerializable(ARG_FEATURE));
    }

    private void setFeatureStory(NewsStory story) {
        // Make sure the story passed in correctly
        Log.i("Feature Fragment", "Story Title on Set Feature Story = " + story.getTitle());
        // set the values
        mTitleView.setText(story.getTitle());
        mTimeView.setText(story.getTimeStamp());
        mTextView.setText(story.getStory());
        mImageView.setImageResource(story.getImageId());
    }

}
