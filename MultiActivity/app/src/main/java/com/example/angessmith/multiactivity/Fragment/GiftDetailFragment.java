package com.example.angessmith.multiactivity.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.angessmith.multiactivity.R;

 // Created by AngeSSmith on 10/13/14 for Java 2 Term 1410.

public class GiftDetailFragment extends Fragment {
    // Create the tag for this fragment
    public static final String TAG = "GiftDetailFragment.TAG";
    // And the bundle argument keys
    public static final String ARG_NAME = "GiftDetailFragment.ARG_NAME";
    public static final String ARG_LOC = "GiftDetailFragment.ARG_LOC ";
    public static final String ARG_PRICE = "GiftDetailFragment.ARG_PRICE";
    public static final String ARG_URL = "GiftDetailFragment.ARG_URL";



    // Create the instance of the fragment
    public static GiftDetailFragment newInstance(String name, String loc, String price, String URL) {

        GiftDetailFragment fragment = new GiftDetailFragment();
        // Create the bundle
        Bundle bundleArguments = new Bundle();
        bundleArguments.putString(ARG_NAME, name);
        bundleArguments.putString(ARG_LOC, loc);
        bundleArguments.putString(ARG_PRICE, price);
        bundleArguments.putString(ARG_URL, URL);
        // save the bundle arguments in the fragment
        fragment.setArguments(bundleArguments);
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
        // get and check the bundle for the passed data
        Bundle bundle = getArguments();
        if (bundle != null) {
            // send the data to place on view
            setGiftDetails(bundle.getString(ARG_NAME), bundle.getString(ARG_LOC), bundle.getString(ARG_PRICE), bundle.getString(ARG_URL));
        }

    }


    public void setGiftDetails(String name, String loc, String price, String url) {
        // update the gift details for orientation change
        getArguments().putString(ARG_NAME, name);
        getArguments().putString(ARG_LOC, loc);
        getArguments().putString(ARG_PRICE, price);
        getArguments().putString(ARG_URL, url);

        // Get the views
        TextView itemNameView = (TextView) getView().findViewById(R.id.name_text);
        TextView itemLocationView = (TextView) getView().findViewById(R.id.store_text);
        TextView itemPriceView = (TextView) getView().findViewById(R.id.price_text);
        TextView itemUrlView = (TextView) getView().findViewById(R.id.url_text);

        // set the details
        itemNameView.setText(name);
        itemLocationView.setText(loc);
        itemPriceView.setText(price);
        itemUrlView.setText(url);
    }

}
