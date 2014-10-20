package com.example.angessmith.navdrawerapp.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


import com.example.angessmith.navdrawerapp.Main;
import com.example.angessmith.navdrawerapp.R;

// Created by AngeSSmith on 10/19/14.

public class ImageCollectionFragment  extends Fragment {
    private static final String ARG_POSITION = "ImageCollectionFragment.POSITION";
    View mRootView;

    public static ImageCollectionFragment newInstance(int section) {
        ImageCollectionFragment fragment = new ImageCollectionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, section);
        fragment.setArguments(args);
        return fragment;
        //return new ImageCollectionFragment();
    }


    public ImageCollectionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the image collection fragment
        mRootView = inflater.inflate(R.layout.image_collection_fragment, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get the gridview in the fragment
        GridView gridview =  (GridView) mRootView.findViewById(R.id.image_grid);
        // Set the image adapter to the view (pass the context of the activity)
          gridview.setAdapter(new GridViewAdapter(getActivity()));

    }

    // Set the title from the passed in section
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_POSITION));
    }

    // Create an image adapter
    private class GridViewAdapter extends BaseAdapter {

        private  Context mContext;
        // Create an array of static images
        final int[] mImages = new int[] {
                R.drawable.begging, R.drawable.clown, R.drawable.mexdog,
                R.drawable.rescue, R.drawable.survelence, R.drawable.undercover
        };

        public GridViewAdapter(Context _context) {
           // mImageIds = _imageIds;
            mContext = _context;
        }
        // Find out how many items are in the array
        @Override
        public int getCount() {
            return mImages.length;
        }
        // get a photo
        @Override
        public Integer getItem(int _position) {
            return mImages[_position];
        }
        // get the id of an item
        @Override
        public long getItemId(int _position) {
            return _position;
        }
        // get the view that will display each image
        @Override
        public View getView(int _position, View _view, ViewGroup _parent) {
            // If the passed in view is null, create a new one.
            if(_view == null) {
                Log.d("Array Adapter", "the position: " + _position);
                // Get the layout inflator from the fragment
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                //LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                _view =  layoutInflater.inflate(R.layout.grid_image, null);
            }
            // Set the view to an image view
            ImageView iv = (ImageView) _view;
            // set the item in it
            iv.setImageResource(getItem(_position));
            // return the view
            return _view;
        }
    }
}
