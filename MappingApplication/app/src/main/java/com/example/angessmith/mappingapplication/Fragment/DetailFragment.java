package com.example.angessmith.mappingapplication.Fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angessmith.mappingapplication.ImageObject;
import com.example.angessmith.mappingapplication.R;

import java.io.IOException;

// Created by AngeSSmith on 11/15/14.

public class DetailFragment extends Fragment {

    public static ImageObject mImageObject;
    private static TextView mImageTitleView;
    private static TextView mImageLocationView;
    private static ImageView mImageView;
    public static final String TAG = DetailFragment.TAG;


    // Define the instance
    public static DetailFragment newInstance(Object object) {
        DetailFragment fragment = new DetailFragment();
        mImageObject = (ImageObject) object;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // set up the text views and imageview
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mImageTitleView = (TextView) view.findViewById(R.id.detail_image_title);
        mImageLocationView = (TextView) view.findViewById(R.id.detail_image_location);
        mImageView = (ImageView) view.findViewById(R.id.detail_image_view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bitmap bitmap = null;
        // Get the image object data
        if (mImageObject != null) {
            mImageTitleView.setText(mImageObject.getImageName());
            mImageLocationView.setText(mImageObject.getImageLocation());
            // get the uri for the image
            Uri imageUri = Uri.parse(mImageObject.getUriString());
            try {
                // See if we can access this from the gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // if we have a valid bitmap, add it to the view
            if (bitmap != null) {
                mImageView.setImageBitmap(bitmap);
            }
        }
    }
}
