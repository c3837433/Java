package com.example.angessmith.quotes.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.angessmith.quotes.QuoteObject;
import com.example.angessmith.quotes.R;

// Created by AngeSSmith on 11/6/14 for MDF3 1412.

public class AddItemFragment extends Fragment {
    public static final String TAG = "AddItemFragment.TAG";

    // Define the views
    EditText mAddNameTextView;
    EditText mAddSourceTextView;
    EditText mAddQuoteTextView;

    // Define the instance
    public static AddItemFragment newInstance() {
        return new AddItemFragment();
    }

    // Set up the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the container
        View view = inflater.inflate(R.layout.fragment_quote_add, container, false);
        // set up the text views
        mAddNameTextView = (EditText) view.findViewById(R.id.add_name_text);
        mAddSourceTextView = (EditText) view.findViewById(R.id.add_source_text);
        mAddQuoteTextView = (EditText) view.findViewById(R.id.add_quote_text);
        return view;
    }

    public Object checkAndSaveQuote() {
        // Make sure the user entered a quote to save
        String quoteString = mAddQuoteTextView.getText().toString();
        if (!quoteString.equals("")) {

            // get the other values
            String name = mAddNameTextView.getText().toString();
            String source = mAddSourceTextView.getText().toString();
            // Empty fields
            mAddQuoteTextView.setText("");
            mAddNameTextView.setText("");
            mAddSourceTextView.setText("");
            // then we can create and return the new object
            return QuoteObject.newInstance(name, source, quoteString);
        } else {
            // Alert the user we need a quote to save to the list
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("No Quote Entered!");
            builder.setMessage("Please add a quote before saving.");
            builder.show();
            // return nothing
            return null;
        }
    }
}
