package com.example.angessmith.quotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


// Created by AngeSSmith on 11/6/14.

public class QuoteListAdapter  extends BaseAdapter {

    // Define the context so we can inflate the list
    private Context mContext;

    public void setQuoteArrayList(ArrayList<HashMap<String, Object>> mQuoteArrayList) {
        this.mQuoteArrayList = mQuoteArrayList;
    }

    // Define up the arraylist and hashmap
    private ArrayList<HashMap<String, Object>> mQuoteArrayList = new ArrayList<HashMap<String, Object>>();
    public static HashMap<String, Object> mQuoteObject;

    // Use the factory to set the properties
    public QuoteListAdapter(Context context, ArrayList<HashMap<String, Object>> arrayList) {
        mContext = context;
        mQuoteArrayList = arrayList;
    }

    @Override
    public int getCount() {
        // get the number of items in the array list
        return mQuoteArrayList.size();
    }

    @Override
    public Object getItem(int position)  {
        // get the item at the selected position
        return mQuoteArrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // see if we can recycle a view
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_quote, parent, false);
        }
        // Get the text views
        TextView quoteTextView = (TextView)convertView.findViewById(R.id.quote_item_quote);
        TextView authorTextView = (TextView)convertView.findViewById(R.id.quote_item_author);

        // set the hashmap
        mQuoteObject = new HashMap<String, Object>();
        // add the quote
        mQuoteObject = mQuoteArrayList.get(position);

        // set the values in the fields
        quoteTextView.setText("\"" + mQuoteObject.get(QuoteListActivity.ADAPTER_QUOTE) + "\"");
        authorTextView.setText(" - " + mQuoteObject.get(QuoteListActivity.ADAPTER_AUTHOR));
        // return the view
        return convertView;
    }
}
