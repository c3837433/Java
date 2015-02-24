package com.example.angessmith.goodfeed.Fragment;

// Created by AngeSSmith on 12/3/14.
// Fragment that displays the user entered text views for the title and story for the share post activity

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.angessmith.goodfeed.Listeners.OnShareListener;
import com.example.angessmith.goodfeed.R;

public class PostTextFragment extends Fragment {

    public static final String TAG = "PostTextFragment";
    EditText mTitleView;
    EditText mStoryTextView;
    // Define the share listener to communicate with the activity
    OnShareListener mListener;

    // Set up the fragment
    public static PostTextFragment newInstance() {
        return new PostTextFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the container
        View view = inflater.inflate(R.layout.fragment_post_text, container, false);
        // Set up the text views
        mTitleView = (EditText) view.findViewById(R.id.post_story_title);
        mStoryTextView = (EditText) view.findViewById(R.id.post_story_text);
        return view;
    }


    // set the listener from the activity
    public void setListener(OnShareListener listener) {
        this.mListener = listener;
    }

    // When the user presses share, send the current values to the activity
    public void returnValuesToActivity() {
        // get the values
        mListener.prepareStoryForShare(mTitleView.getText().toString(), mStoryTextView.getText().toString());
    }
}
