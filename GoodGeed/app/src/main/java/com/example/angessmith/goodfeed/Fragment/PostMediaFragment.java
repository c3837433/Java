package com.example.angessmith.goodfeed.Fragment;


// Created by AngeSSmith on 12/3/14.
// Fragment that displays the buttons to display the camera or gallery within the share post activity
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.angessmith.goodfeed.R;

public class PostMediaFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "PostMediaFragment";

    // set up the interface
    public static OnMediaButtonClickListener mOnMediaButtonClickListener;

    // Set up the fragment
    public static PostMediaFragment newInstance() {
        return new PostMediaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the container
        View view = inflater.inflate(R.layout.fragment_post_media, container, false);
        // get the buttons and set the listeners
        ImageButton cameraButton = (ImageButton) view.findViewById(R.id.open_camera_button);
        cameraButton.setOnClickListener(this);
        ImageButton galleryButton = (ImageButton) view.findViewById(R.id.open_gallery_button);
        galleryButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        // see what was clicked
        switch (v.getId()) {
            case R.id.open_camera_button:
                mOnMediaButtonClickListener.OpenCamera();
                break;
            case R.id.open_gallery_button:
                mOnMediaButtonClickListener.OpenGallery();
                break;
            default:
                break;
        }
    }

    public interface OnMediaButtonClickListener {
        // give the user access to the camera or gallery
        public void OpenCamera();
        public void OpenGallery();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //check the activity
        if (activity instanceof OnMediaButtonClickListener) {
            // set the listener
            mOnMediaButtonClickListener = (OnMediaButtonClickListener) activity;
        }
    }
}
