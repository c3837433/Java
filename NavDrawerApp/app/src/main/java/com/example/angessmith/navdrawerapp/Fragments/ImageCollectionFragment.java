package com.example.angessmith.navdrawerapp.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.angessmith.navdrawerapp.Main;
import com.example.angessmith.navdrawerapp.R;

import java.util.ArrayList;

/**
 * Created by AngeSSmith on 10/19/14.
 */
public class ImageCollectionFragment  extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ImageCollectionFragment newInstance() {
        return new ImageCollectionFragment();
    }

    // create an array of the images
    final int[] mImages = new int[] {
            R.drawable.begging, R.drawable.clown, R.drawable.mexdog,
            R.drawable.rescue, R.drawable.survelence, R.drawable.undercover
    };

    public ImageCollectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_collection_fragment, container, false);
        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if the state was saved
        if (savedInstanceState != null) {
            //get the gridview in the fragment
            GridView gridview = (GridView) getView().findViewById(R.id.image_grid);
            // Set the image adapter to the view
            gridview.setAdapter(new ImageAdapter(mImages));
        }
    }

    private class ImageAdapter extends BaseAdapter {

        // The collection of image IDs that we'll be using in this adapter.
        private int[] mImageIds;

        public ImageAdapter(int[] _imageIds) {
            mImageIds = _imageIds;
        }

        // Find out how many items are in the array
        @Override
        public int getCount() {
            return mImageIds.length;
        }

        // get a photo
        @Override
        public Integer getItem(int _position) {
            return mImageIds[_position];
        }

        // get the id of an item
        @Override
        public long getItemId(int _position) {
            return _position;
        }

        // get the view
        @Override
        public View getView(int _position, View _convertView, ViewGroup _parent) {

            // If the passed in view is null, create a new one.
            // This view is typically null to start but will contain
            // a recycled view later on.
            if(_convertView == null) {
                // create a new one
                _convertView = getActivity().getLayoutInflater().inflate(R.layout.grid_image, null);
            }

            // get the image view in the grid
            ImageView iv = (ImageView) _convertView;
            // set the item in it
            iv.setImageResource(getItem(_position));
            // return the view
            return _convertView;
        }

    }
}
