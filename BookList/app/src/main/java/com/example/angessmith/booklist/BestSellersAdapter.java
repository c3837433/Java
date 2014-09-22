package com.example.angessmith.booklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

// Created by: Angela Smith 9/22/2014 for Java 1 term 1409


public class BestSellersAdapter extends BaseAdapter {

    // Create a context
    private Context mContext;
    // The hashmap array list
    private ArrayList<HashMap<String, Object>> mBestSellerList = new ArrayList<HashMap<String, Object>>();
    // And a hashmap
    public static HashMap<String, Object> mBestSeller;

    // Set the variables
    public BestSellersAdapter(Context _context, ArrayList<HashMap<String, Object>> _bestSellerListData) {
        mContext = _context;
        mBestSellerList = _bestSellerListData;
    }
    @Override
    public int getCount() {
        // Get the number of items in the list
        return mBestSellerList.size();
    }

    @Override
    public Object getItem(int position) {
        // Get the position of the item
        return mBestSellerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // IF there is no view available to recycle, create one
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.book_row, parent, false);
        }

        TextView titleField = (TextView)convertView.findViewById(R.id.book_title);
        TextView authorField = (TextView)convertView.findViewById(R.id.book_author);
        TextView descriptionField = (TextView)convertView.findViewById(R.id.book_description);
        TextView rankField = (TextView)convertView.findViewById(R.id.book_rank);

        // Create a new hashmap object and set it to the book listed at this position
        mBestSeller = new HashMap<String, Object>();
        mBestSeller = mBestSellerList.get(position);

        // Set the fields with the data from this object
        titleField.setText((String)mBestSeller.get(MainActivity.TITLE));
        authorField.setText("By: " + mBestSeller.get(MainActivity.AUTHOR));
        descriptionField.setText((String)mBestSeller.get(MainActivity.DESCRIPTION));
        // Get the Rank
        Integer rankValue = (Integer) mBestSeller.get(MainActivity.RANK);
        rankField.setText(String.valueOf(rankValue));

        // Return the view
        return convertView;
    }
}
