package com.example.angessmith.advancedviewsproject;

// Created by Angela Smith September 13, 2014
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity {

    // Set the simple name as the tag for debugging
    final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Creature> mCreatures;

    // Get the list view that will hold all the creatures in landscape
    final ListView statesList = (ListView) findViewById(android.R.id.list);

    // Create the spinner and adapter
    Spinner mItemSpinner;
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Create the creatures
        mCreatures = new ArrayList<Creature>();
        mCreatures.add(Creature.newInstance("Sympetrum Vicinum", "Autumn Meadowhawk", R.drawable.hawkmoth));
        mCreatures.add(Creature.newInstance("Hyles Euphorbiaw", "Spurge Hawk-moth", R.drawable.dragonfly));
        mCreatures.add(Creature.newInstance("Agapostemon", "Metallic Green Bee", R.drawable.greenbee));
        mCreatures.add(Creature.newInstance("Graphocephala Coccinea", "Candy Striped Leafhopper", R.drawable.leafhopper));
        mCreatures.add(Creature.newInstance("Conocephalus Dorsalis", "Katydid", R.drawable.katydid));
        mCreatures.add(Creature.newInstance("Lithobates Pipiens", "Northern Tree Frog", R.drawable.frog));


        SimpleAdapter adapter = creatureAdapter();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            // Then add the stuff for the spinner
            Toast.makeText(this, "The view is in portrait mode", Toast.LENGTH_LONG).show();
            //mItemSpinner = (Spinner) findViewById(R.id.listspinner);
            statesList.setAdapter(adapter);

        } else {
            // Set up for list view
            Toast.makeText(this, "The view is in Landscape mode", Toast.LENGTH_LONG).show();
            //SimpleAdapter adapter = creatureAdapter();
            statesList.setAdapter(adapter);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.state_field, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    private SimpleAdapter creatureAdapter() {

        SimpleAdapter adapter;
        // set the text for the map
        final String creatureName = "creaturename";
        // Create the arraylist to hold the hashmap
        ArrayList<HashMap<String, String>> creatures = new ArrayList<HashMap<String, String>>();
        // Loop through all the creatures
        for (Creature creature : mCreatures) {
            // Create a bew map and add the common name to it
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put(creatureName, creature.getCommonName());
            creatures.add(hashMap);
        }

        // Create an array of the names
        String[] keys = {
            creatureName
        };

        // Check for orientation
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            // Create the array of the values
            int[] viewValues = new int[] {
                    R.id.listspinner
            };
            // Set this as the adapter
            adapter = new SimpleAdapter(this, creatures, android.R.layout.simple_spinner_dropdown_item, keys, viewValues);
        } else {
            int[] viewValues = new int[] {
                    android.R.id.list
            };
            // Set this as the adapter
            adapter = new SimpleAdapter(this, creatures, R.layout.activity_list, keys, viewValues);
        }

       return adapter;
    }
}
