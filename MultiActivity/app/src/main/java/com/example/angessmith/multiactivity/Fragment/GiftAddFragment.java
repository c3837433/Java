package com.example.angessmith.multiactivity.Fragment;
// Created by AngeSSmith on 10/12/14 for Java 2 Term 1410.

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;

import com.example.angessmith.multiactivity.R;

public class GiftAddFragment extends Fragment  {

    // CREATE TAG
    public static final String TAG = "GiftAddFragment.TAG";

    EditText mTitleTextView;
    EditText mLocTextView;
    EditText mPriceTextView;
    EditText mUrlTextView;

    // CREATE FACTORY
    public static GiftAddFragment newInstance() {
        return new GiftAddFragment();
    }

    // SET UP THE FRAGMENT LAYOUT
    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        // Return the fragment
        return _inflater.inflate(R.layout.gift_add_fragment, _container, false);
    }

    public Object onSaveItemClick () {
        // Get the values from the textfields for the object
        String itemName = mTitleTextView.getText().toString();
        String itemLocation = mLocTextView.getText().toString();
        String itemPrice = (mPriceTextView.getText().toString());
        String itemUrl = mUrlTextView.getText().toString();

        // Make sure we at least have a title
        if (!itemName.equals(""))
        {
            // If we do, return the object
            return GiftObject.newInstance(itemName, itemLocation, itemPrice, itemUrl);
        }
        // Otherwise, alert the user we need a name
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Missing Name");
            builder.setMessage("This item needs a name to be saved.");
            builder.show();
            return null;
        }
    }

    // Set up the textfield properties
    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);
        //Set the EditText views
        mTitleTextView   = (EditText) getView().findViewById(R.id.item_title_textView);
        mLocTextView   = (EditText) getView().findViewById(R.id.item_location_textView);
        mPriceTextView   = (EditText) getView().findViewById(R.id.item_price_textView);
        mUrlTextView   = (EditText) getView().findViewById(R.id.item_url_textView);
    }

    // From activity, clear the textviews whe the user cancels
    public void clearTextViews () {
        mTitleTextView.setText("");
        mLocTextView.setText("");
        mPriceTextView.setText("");
        mUrlTextView.setText("");
    }

}
