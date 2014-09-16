package com.example.angessmith.advancedviewsproject;

// Created by AngeSSmith on 9/14/14 for Java 1 Week 3 Term 1409.

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


public class CreatureAdapter extends BaseAdapter {
    final String TAG = "CreatureApp";
    // Create the context
    private Context mContext;
    // And an array to hold the names
    private ArrayList<Creature> mCreatures;

    // Define the adapter to display the data
    public CreatureAdapter(Context _context, ArrayList<Creature> _creatures) {
        mContext = _context;
        mCreatures = _creatures;
    }

    //Get the number of objects in the data collection
    @Override
    public int getCount() {
        return mCreatures.size();
    }

    @Override
    public Creature getItem(int _position) {
        return mCreatures.get(_position);
    }

    @Override
    public long getItemId(int _position) {
        return _position;
    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {

        // If there is no view available
        if(_convertView == null) {
            // create one in the list view
            _convertView = LayoutInflater.from(mContext).inflate(R.layout.list_name, _parent, false);
        }
        // Find which object we are using
        Creature creature = getItem(_position);

        Log.d(TAG, "This Creature = " + creature);
        // Set the common name in the Textview
        TextView textView = (TextView)_convertView.findViewById(R.id.creature_name);
        textView.setText(creature.getCommonName());
        // And a thumbnail preview of the image
        ImageView imageView = (ImageView)_convertView.findViewById(R.id.creature_image);
        imageView.setImageResource(creature.getImageId());

        // Return the created views
        return _convertView;
    }

}
