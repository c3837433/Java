package com.example.angessmith.multiactivity.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.angessmith.multiactivity.R;

import java.util.ArrayList;


// Created by AngeSSmith on 10/12/14 for Java 2 Week 3 Term 1410.

public class GiftListFragment extends Fragment {
    // create fragment tag
    public static final String TAG = "GiftListFragment.TAG";

    // Define properties for the listview
    public static ArrayList<GiftObject> mGifts;
    public static ListView mListview;
    public static ArrayAdapter<GiftObject> mArrayAdapter;



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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGifts = new ArrayList<GiftObject>();
        // Get the listview, and create and set the adapter to it
        mListview =  (ListView) getView().findViewById(R.id.giftlist);
        mArrayAdapter = new ArrayAdapter<GiftObject>(getActivity(), android.R.layout.simple_list_item_1, (mGifts));
        mListview.setAdapter(mArrayAdapter);
    }


}
