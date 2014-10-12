package com.example.angessmith.multiactivity.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.angessmith.multiactivity.R;

 // Created by AngeSSmith on 10/12/14 for Java 2 Term 1410.

public class GiftAddFragment extends Fragment implements ButtonFragment.OnButtonClickListener{

    // CREATE TAG
    public static final String TAG = "GiftAddFragment.TAG";

    // CREATE FACTORY
    public static GiftAddFragment newInstance() {
        GiftAddFragment fragment = new GiftAddFragment();
        return fragment;
    }

    // SET UP THE FRAGMENT LAYOUT
    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        // Return the fragment
        return _inflater.inflate(R.layout.gift_add_fragment, _container, false);
    }

    @Override
    public void openAddStoryView() {
        
    }
}
