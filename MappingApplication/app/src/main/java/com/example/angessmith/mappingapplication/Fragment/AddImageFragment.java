package com.example.angessmith.mappingapplication.Fragment;

// Created by AngeSSmith on 11/15/14.

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.angessmith.mappingapplication.R;


public class AddImageFragment extends Fragment implements View.OnClickListener{

    // Camera and view properties
    public static  EditText mImageTitleView;
    public static  EditText mImageLocationView;
    public static ImageView mNewImageView;

    // Define the interface for the button listener
    private OnAddImageListener mAddImageListener;

    // Set up the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the container
        View view = inflater.inflate(R.layout.fragment_add_image, container, false);
        // set up the views
        mImageTitleView = (EditText) view.findViewById(R.id.image_location_title_text);
        mImageLocationView = (EditText) view.findViewById(R.id.image_location_name_text);
        mNewImageView = (ImageView) view.findViewById(R.id.image_view);
        // Add the listener to the button
        Button button = (Button) view.findViewById(R.id.camera_button);
        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        // open the camera
        mAddImageListener.OpenCamera();
    }


    public interface OnAddImageListener {
        // open the camera
        public void OpenCamera();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Check the listener
        if (activity instanceof OnAddImageListener) {
            // set the listener
            mAddImageListener = (OnAddImageListener) activity;
        }
    }
}
