package com.example.angessmith.multiactivity.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.angessmith.multiactivity.R;


// Created by AngeSSmith on 10/12/14 for Java 2 Week 3 Term 1410.

public class GiftListFragment extends Fragment implements View.OnClickListener{

    // create fragment tag
    public static final String TAG = "GiftListFragment.TAG";
    // And the bundle argument keys
    public static final String ARG_GIFT_TITLE = "GiftListFragment.ARG_GIFT_TITLE";
    public static final String ARG_LOCATION = "GiftListFragment.ARG_LOCATION";
    public static final String ARG_PRICE = "GiftListFragment.ARG_PRICE";
    public static final String ARG_URL = "GiftListFragment.ARG_URL";

    // CREATE THE LISTENER
    private OnSaveItemListener mOnSaveItemListener;
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
    public void onClick(View v) {
        mOnSaveItemListener.SaveItemsToList();
    }

    // Create the interface
    public interface OnSaveItemListener {
        public void SaveItemsToList();
    }
    // Add the listener to the view
    public void  onAttach(Activity activity) {
        super.onAttach(activity);
        mOnSaveItemListener = (OnSaveItemListener) this;
    }
}
