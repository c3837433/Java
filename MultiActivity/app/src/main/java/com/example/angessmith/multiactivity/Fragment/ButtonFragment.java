package com.example.angessmith.multiactivity.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.angessmith.multiactivity.R;

import java.util.concurrent.ExecutionException;

// Created by AngeSSmith on 10/12/14 for Java 2 Term 1410.

public class ButtonFragment  extends Fragment implements View.OnClickListener {

    // Create tag
    public static final String TAG = "ButtonFragment.TAG";

    // Create the listener for the button
    private OnButtonClickListener mAddItemButtonListener;
    // Create factory
    public static ButtonFragment newInstance() {
        ButtonFragment fragment = new ButtonFragment();
        return fragment;
    }


    // Add layout
    @Override
    public View onCreateView (LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        // return the button layout
        return _inflater.inflate(R.layout.button_fragment, _container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // set the listener
        mAddItemButtonListener = (OnButtonClickListener) activity;
    }
    @Override
    public void onClick(View v) {
        // Set the listener to run the open add story view when clicked
        mAddItemButtonListener.openAddStoryView();
    }

    // Define the interface for the button
    public interface OnButtonClickListener {
        public void openAddStoryView();
    }

    // Set up the interfaces when created
    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        // Get the button and set the listener to it
        Button button = (Button) getView().findViewById(R.id.add_gift_button);
        button.setOnClickListener((View.OnClickListener) this);
    }
}
