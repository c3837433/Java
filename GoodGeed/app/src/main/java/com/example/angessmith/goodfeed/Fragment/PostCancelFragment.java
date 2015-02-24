package com.example.angessmith.goodfeed.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.angessmith.goodfeed.R;

// Created by AngeSSmith on 12/3/14.
// Fragment that displays the cancel button and imageview within the share post activity

public class PostCancelFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "PostCancelFragment";
    ImageView mImageView;
    private static Bitmap mImageBitmap;

    // Set up the fragment
    public static PostCancelFragment newInstance(Bitmap imageBitmap) {
        PostCancelFragment fragment = new PostCancelFragment();
        // get the bitmap that was currently retrieved from either the camera or gallery in the activity
        mImageBitmap = imageBitmap;
        return fragment;
    }


    // Define the interface listener when the user wants to cancel the current image
    public static OnCancelButtonClickListener mOnCancelButtonClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_cancel, container, false);
        // set cancel button and the listener to it
        ImageButton cancelButton = (ImageButton) view.findViewById(R.id.cancel_media_button);
        cancelButton.setOnClickListener(this);
        // get the imageview and set the created image in it
        mImageView = (ImageView) view.findViewById(R.id.post_media_imageview);
        mImageView.setImageBitmap(mImageBitmap);
        return view;
    }

    // Set up the on click listener
    @Override
    public void onClick(View v) {
        // Make sure it was the cancel button
        switch (v.getId()) {
            case R.id.cancel_media_button:
                mOnCancelButtonClickListener.ClearImage(mImageView);
                break;
            default:
                break;
        }
    }

    public interface OnCancelButtonClickListener {
        // Allow the activity access to the image view so it can clear it
        public void ClearImage(ImageView imageView);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // set the listener to the calling activity
        if (activity instanceof OnCancelButtonClickListener) {
            mOnCancelButtonClickListener = (OnCancelButtonClickListener) activity;
        }
    }
}
