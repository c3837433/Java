package com.example.angessmith.multiactivity.Fragment;
// Created by AngeSSmith on 10/13/14 for Java 2 Term 1410.

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.angessmith.multiactivity.R;

public class GiftDetailFragment extends Fragment {
    // Create the tag for this fragment
    public static final String TAG = "GiftDetailFragment.TAG";
    // And the bundle argument keys
    /*
    public static final String ARG_NAME = "GiftDetailFragment.ARG_NAME";
    public static final String ARG_LOC = "GiftDetailFragment.ARG_LOC ";
    public static final String ARG_PRICE = "GiftDetailFragment.ARG_PRICE";
    public static final String ARG_URL = "GiftDetailFragment.ARG_URL";
    int position;
    */

    // Define the interface listener
    private GiftDetailListener mDetailListener;

    // Create the instance of the fragment
    public static GiftDetailFragment newInstance() {
        GiftDetailFragment fragment = new GiftDetailFragment();
        //GiftDetailFragment fragment = new GiftDetailFragment(String name, String loc, String price, String URL);
        // Create the bundle
        /*
        Bundle bundleArguments = new Bundle();
        bundleArguments.putString(ARG_NAME, name);
        bundleArguments.putString(ARG_LOC, loc);
        bundleArguments.putString(ARG_PRICE, price);
        bundleArguments.putString(ARG_URL, URL);
        // save the bundle arguments in the fragment
        fragment.setArguments(bundleArguments);
        */
        return fragment;
    }

    // Layout the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle _savedInstanceState) {
        // inflate the container with the fragments
        return inflater.inflate(R.layout.details_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setGiftDetails();
        // get and check the bundle for the passed data
        Bundle bundle = getArguments();
        if (bundle != null) {
            // send the data to place on view
            //setGiftDetails(bundle.getString(ARG_NAME), bundle.getString(ARG_LOC), bundle.getString(ARG_PRICE), bundle.getString(ARG_URL));
            // set the values in the view
            setGiftDetails();
        }
        // Set the delete listener to the button
        Button deleteButton = (Button) getView().findViewById(R.id.delete_button);
        //deleteButton.setOnClickListener(this);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailListener.deleteGift();
            }
        });

        final Button shareButton = (Button) getView().findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create an intent to share this item
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                String shareString = "I found " + mDetailListener.getGiftObject().getName() + " at " + mDetailListener.getGiftObject().getLocation() + " for $" + mDetailListener.getGiftObject().getPrice() + ". Check it out! " + mDetailListener.getGiftObject().getUrl();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Share gift"));
            }
        });


    }

    // Create the listeners
    public interface GiftDetailListener {
        public GiftObject getGiftObject();
        public void deleteGift();
    }

    // Attach the listeners
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // check which instance the activity is and set them
        if (activity instanceof GiftDetailListener) {
            mDetailListener = (GiftDetailListener) activity;
        }
    }

    public void setGiftDetails() {
        //setGiftDetails(String name, String loc, String price, String url)
      /*  // update the gift details for orientation change
        getArguments().putString(ARG_NAME, name);
        getArguments().putString(ARG_LOC, loc);
        getArguments().putString(ARG_PRICE, price);
        getArguments().putString(ARG_URL, url);
    */
        // Get the views
        TextView itemNameView = (TextView) getView().findViewById(R.id.name_text);
        TextView itemLocationView = (TextView) getView().findViewById(R.id.store_text);
        TextView itemPriceView = (TextView) getView().findViewById(R.id.price_text);
        TextView itemUrlView = (TextView) getView().findViewById(R.id.url_text);

        // check the values
        Log.i(TAG, "The name = " + mDetailListener.getGiftObject().getName());
        Log.i(TAG, "The location = " + mDetailListener.getGiftObject().getLocation());
        Log.i(TAG, "The price = " + mDetailListener.getGiftObject().getPrice());
        Log.i(TAG, "The url = " + mDetailListener.getGiftObject().getUrl());
        // set the listeners
        itemNameView.setText(mDetailListener.getGiftObject().getName());
        itemLocationView.setText(mDetailListener.getGiftObject().getLocation());
        itemPriceView.setText(mDetailListener.getGiftObject().getPrice());
        itemUrlView.setText(mDetailListener.getGiftObject().getUrl());

        // set the details
        /*
        itemNameView.setText(name);
        itemLocationView.setText(loc);
        itemPriceView.setText(price);
        itemUrlView.setText(url);
        */
    }

}
