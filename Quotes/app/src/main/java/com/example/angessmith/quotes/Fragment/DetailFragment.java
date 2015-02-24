package com.example.angessmith.quotes.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.angessmith.quotes.QuoteObject;
import com.example.angessmith.quotes.R;

// Created by AngeSSmith on 11/6/14 for MDF3 1412.

public class DetailFragment extends Fragment {

    public static final String TAG = "DetailFragment.TAG";
    public static QuoteObject mQuote;
    private static TextView mAuthorView;
    private static TextView mSourceView;
    private static TextView mQuoteView;

    // Define the instance
    public static DetailFragment newInstance(Object object) {
        //Log.d(TAG, "Fragment Created");
        DetailFragment fragment = new DetailFragment();
        mQuote = (QuoteObject) object;
        return fragment;
    }

    // Set up the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the container
        //Log.d(TAG, "View Created");
        View view = inflater.inflate(R.layout.fragment_quote_detail, container, false);
         mAuthorView = (TextView) view.findViewById(R.id.detail_quote_author);
         mSourceView = (TextView) view.findViewById(R.id.detail_quote_source);
         mQuoteView = (TextView) view.findViewById(R.id.detail_quote);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Log.d(TAG, "Activity Created");
        // Set the views
        if (mQuote != null) {
          //  Log.d(TAG, "Quote not empty");
            // set the text to the views
            mAuthorView.setText(" - " + mQuote.getAuthorName());
            mSourceView.setText(mQuote.getSource());
            // Set up the quote
            String formatedString = "\"" + mQuote.getQuote() + "\"";
            mQuoteView.setText(formatedString);
        }
    }
}
