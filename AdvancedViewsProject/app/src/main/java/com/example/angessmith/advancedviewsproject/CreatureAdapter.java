package com.example.angessmith.advancedviewsproject;

// Created by AngeSSmith on 9/14/14 for Java 1 Week 3 Term 1409.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;


public class CreatureAdapter extends BaseAdapter {

    // Create the context
    private Context mContext;
    // And an array to hold the names
    //private ArrayList<Creature> mCreatures;
    // Create a new Arraylist to hold the hashmap objects
    private ArrayList<HashMap<String, Object>> mCreatureList = new ArrayList<HashMap<String, Object>>();
    // Create a new hashmap object
    public static HashMap<String, Object> mCreature;

    // Define the adapter to display the data
    /*
    public CreatureAdapter(Context _context, int _resource, ArrayList<Creature> _creatures) {
        mContext = _context;
        mCreatures = _creatures;
    }
    */
    // Create the
    public CreatureAdapter(Context _context,  ArrayList<HashMap<String, Object>> _creatureListData) {
        mContext = _context;
        mCreatureList  = _creatureListData;
    }
    //Get the number of objects in the data collection
    public int getCount() {
        return mCreatureList.size();
    }
    // Get the item at the passed in position
    public Object getItem(int _position) {
        return mCreatureList.get(_position);
    }
    // Get the id of the item in the passed in position
    public long getItemId(int _position) {
        return _position;
    }
    // Create the custom view for the Spinner or ListView
    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {

        // If there is no view available to recycle
        if(_convertView == null) {
            // create one in the list view
            _convertView = LayoutInflater.from(mContext).inflate(R.layout.list_name, _parent, false);
        }

        // Create the textfield
        TextView textView = (TextView)_convertView.findViewById(R.id.creature_name);
        // And an imageview for the thumbnail preview of the image
        ImageView imageView = (ImageView)_convertView.findViewById(R.id.creature_image);

        // Set this creature to a hew hashmap object
        mCreature = new HashMap<String, Object>();
        // At the position of the list of objects
        mCreature = mCreatureList.get(_position);

        // Use the activity keys to set the values in the fields
        textView.setText((String)mCreature.get(MainActivity.NAME));
        imageView.setImageResource((Integer) mCreature.get(MainActivity.IMAGE));
        // return the view
        return _convertView;
    }
    /*
    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {

        // If there is no view available
        if(_convertView == null) {
            // create one in the list view
            _convertView = LayoutInflater.from(mContext).inflate(mResource, _parent, false);
        }
        // Find which object we are using
        Creature creature = getItem(_position);
        Log.d(TAG, "This Creature = " + creature);

        HashMap <String,Object> hashmap = mCreaturelist.get(_position);
        // position gives you the index
        String commonName = (String) hashmap.get("name");
        Object imageId =  hashmap.get("image");

        // Set the common name in the Textview
        TextView textView = (TextView)_convertView.findViewById(R.id.creature_name);
        //textView.setText(creature.getCommonName());
        textView.setText(commonName);
        // And a thumbnail preview of the image
        //ImageView imageView = (ImageView)_convertView.findViewById(R.id.creature_image);
        //imageView.setImageResource(creature.getImageId());

        // Return the created views
        return _convertView;
    }
    */

}
