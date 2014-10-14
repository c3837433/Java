package com.example.angessmith.multiactivity.Fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.angessmith.multiactivity.R;

import java.util.ArrayList;


// Created by AngeSSmith on 10/12/14 for Java 2 Week 3 Term 1410.

public class GiftListFragment extends Fragment implements AdapterView.OnItemClickListener {
    // create fragment tag
    public static final String TAG = "GiftListFragment.TAG";
    public static final String ARG_GIFTLIST = "GiftListFragment.ARG_GIFTLIST";

    // Define properties for the listview
    public static ArrayList<GiftObject> mGifts;
    public static ListView mListview;
    public static ArrayAdapter<GiftObject> mArrayAdapter;

    // Set the interface variable
    private OnGiftItemClickListener mOnGiftItemClickListener;

    // Create the factory
    public static GiftListFragment newInstance() {
        GiftListFragment fragment = new GiftListFragment();
        return fragment;
    }

    // SET UP THE FRAGMENT LAYOUT
    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        // Return the GiftListFragment
        return _inflater.inflate(R.layout.gift_list_fragment, _container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if the state was saved
        if (savedInstanceState != null) {
            // get the values back
            mGifts = (ArrayList<GiftObject>) savedInstanceState.getSerializable(ARG_GIFTLIST);
            //Log.i(TAG, "Gift Data from onCreate: " + mGifts);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Update the listview with the list items
            SetGiftsInList();
        } else {
            mGifts = new ArrayList<GiftObject>();
            //Log.i(TAG, "Creating new arraylist for gift data in onActivityCreated ");
            // Get the listview, and create and set the adapter to it
            SetGiftsInList();
        }
        //mListview = (ListView) getView().findViewById(R.id.giftlist);
        //mListview.setOnItemClickListener(this);
    }


    private void SetGiftsInList() {
        mListview =  (ListView) getView().findViewById(R.id.giftlist);
        mListview.setOnItemClickListener(this);
        mArrayAdapter = new ArrayAdapter<GiftObject>(getActivity(), android.R.layout.simple_list_item_1, (mGifts));
        mListview.setAdapter(mArrayAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Attach the activity to the listeners
        mOnGiftItemClickListener = (OnGiftItemClickListener) activity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "User Clicked List");
        // Get the object that was selected
        GiftObject giftObject = mGifts.get(position);
        Log.i(TAG, "User Clicked gift: " + giftObject);
        // Attach it to the listener to open in the view
        mOnGiftItemClickListener.openGiftInDetailView(giftObject, position);
    }

    // Define the list item interface
    public interface OnGiftItemClickListener {
        public void openGiftInDetailView(GiftObject object, int position);
    }

    // SAVE THE ARRAY LIST WHEN NECESSARY
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // set the current values to the bundle
        savedInstanceState.putSerializable(ARG_GIFTLIST, mGifts);
        Log.i(TAG, "Saving gift data: " + mGifts);
        // save the state
        super.onSaveInstanceState(savedInstanceState);
    }


}
