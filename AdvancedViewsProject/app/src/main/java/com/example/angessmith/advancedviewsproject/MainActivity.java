package com.example.angessmith.advancedviewsproject;

// Created by Angela Smith September 13, 2014
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity {

    // Set the simple name as the tag for debugging
    final String TAG = "CreatureApp";
    private ArrayList<Creature> mCreatures;
    Spinner mCreatureSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Create the creatures with their commom and scientific names, and image ids
        mCreatures = new ArrayList<Creature>();
        mCreatures.add(Creature.newInstance("Sympetrum Vicinum", "Autumn Meadowhawk", R.drawable.dragonfly)); // Insecta
        mCreatures.add(Creature.newInstance("Hyles Euphorbiaw", "Spurge Hawk-moth", R.drawable.hawkmoth));
        mCreatures.add(Creature.newInstance("Agapostemon", "Metallic Green Bee", R.drawable.greenbee));
        mCreatures.add(Creature.newInstance("Graphocephala Coccinea", "Candy Striped Leafhopper", R.drawable.leafhopper));
        mCreatures.add(Creature.newInstance("Conocephalus Dorsalis", "Katydid", R.drawable.katydid));
        mCreatures.add(Creature.newInstance("Lithobates Pipiens", "Northern Tree Frog", R.drawable.frog)); // Amphibia


        SimpleAdapter adapter = creatureAdapter();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            // Then add the stuff for the spinner
            //Toast.makeText(this, "The view is in portrait mode", Toast.LENGTH_LONG).show();
            // Get the spinner for portrait mode
            mCreatureSpinner = (Spinner)findViewById(R.id.listspinner);
            // set the adapter to it
            mCreatureSpinner.setAdapter(adapter);

            // ADD SPINNER EVENT LISTENER
            mCreatureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.i(TAG, "Creature Selected at position " + position);
                    // Get the whole item displayed in view
                    setItemDataInView(parent, position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.i(TAG, "Nothing Selected");
                }
            });

        } else {
            // Set up for list view
            //Toast.makeText(this, "The view is in Landscape mode", Toast.LENGTH_LONG).show();
            // get the listview in landscape mode
            ListView creatureList = (ListView)findViewById(R.id.listView);
            // set the adapter to it
            creatureList.setAdapter(adapter);
            // LISTEN FOR USER TO CLICK ON LIST AND DISPLAY ITEM DATA
            creatureList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    setItemDataInView(parent, position);
                }
            });
        }

    }

    private void setItemDataInView(AdapterView<?> parent, int position) {
        Object creature = parent.getItemAtPosition(position);
        Log.i(TAG, "Creature = " + creature);
        TextView commonTextView = (TextView)findViewById(R.id.common_name_textView);
        commonTextView.setText(mCreatures.get(position).getCommonName());
        TextView scientificTextView = (TextView)findViewById(R.id.scientific_name_textView);
        scientificTextView.setText(mCreatures.get(position).getScientificName());
        ImageView creatureImageView = (ImageView) findViewById(R.id.creature_image_view);
        creatureImageView.setImageResource(mCreatures.get(position).getImageId());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.creature_field, menu);
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

    // Create a simple adapter to set the creture's common name in both the spinner and listview
    private SimpleAdapter creatureAdapter() {

        SimpleAdapter adapter;
        // set the text for the map
        final String creatureName = "creaturename";
        final String scientificName = "scientificname";
        final String image = "image";

        // Create the arraylist to hold the hashmap
        ArrayList<HashMap<String, Object>> creatures = new ArrayList<HashMap<String, Object>>();
        // Loop through all the creatures
        for (Creature creature : mCreatures) {
            // Create a bew map and add the common name to it
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put(creatureName, creature.getCommonName());
            hashMap.put(scientificName, creature.getScientificName());
            hashMap.put(image, creature.getImageId());
            creatures.add(hashMap);
        }

        // Create an array of the names
        String[] keys = {
            image, creatureName
        };

        // Create an array views to place
        int[] viewValues = new int[] {
                R.id.creature_image, R.id.creature_name
        };

        // Set this as the adapter
        adapter = new SimpleAdapter(this, creatures, R.layout.list_name, keys, viewValues);
        // Return the adapter
       return adapter;
    }
}
