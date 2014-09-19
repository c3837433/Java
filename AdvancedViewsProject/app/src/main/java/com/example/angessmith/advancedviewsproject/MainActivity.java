package com.example.angessmith.advancedviewsproject;

// Created by AngeSSmith on 9/14/14 for Java 1 Week 3 Term 1409.
/*
    Assignment Requirements
    MINIMUM:
        1. Your project must compile with a minimum SDK of API 14 and a target SDK of API 19 and run on an Android 4.0+ emulator or device.
        2. You must turn in a Fundamentals Walkthrough video.
        3. You must have your code pushed to a private Git repo and your instructor must have access."
        4. Comments must be used to place the student name at the top of all .java files.
    STANDARD:
        1. Application must support both portrait and landscape orientations.
        2. Master view should be a Spinner in portrait and a ListView in landscape. Detail view can be any non-AdapterView UI control used to display information.
        3. AdapterViews are populated using a collection of HashMap objects that are used to store related data.
        4. Application uses built-in Android views (android.R.layout) for AdapterView item layouts.
        5. Detail view must be populated from the correct data as determined by the position in the corresponding AdapterView.
    MASTERY:
        1. AdapterViews are populated using a collection of custom objects containing multiple points of data.
        2. Application uses custom layouts for AdapterView item layouts.
        3. Detail view is populated from a shared UI population method that is shared across all AdapterViews.

    RUBRIC:
        Structure/Efficiency: The application is structured efficiently and with proper methods and constructs.
        Data Source: AdapterView data source is a collection of custom class objects. Custom class contains multiple (at least three) data points as class members.
        User Interface: Application displays the proper UI components in landscape and portrait orientations.  At least one AdapterView uses custom item layouts that show multiple points of data.
        Events: All AdapterView item clicks/selections properly load data into the detail view using a shared UI population method.
        Logic: Detail view is populated with data from a custom class object that contains multiple (at least three) points of data and corresponds to the selected position in the associated AdapterView.
 */
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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity {

    // Set the simple name as the tag for debugging
    final String TAG = "CreatureApp";
    // Create an arraylist to hold all the custom creature objects
    private ArrayList<Creature> mCreatures;
    // Create the static string keys that will be used in the hashmap and custom adapter
    static final String NAME = "creaturename";
    static final String SCIENTIFIC = "scientificname";
    static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Create the creatures with their commom and scientific names, and image ids
        mCreatures = new ArrayList<Creature>();
        mCreatures.add(Creature.newInstance("Sympetrum Vicinum", "Autumn Meadowhawk", R.drawable.dragonfly));
        mCreatures.add(Creature.newInstance("Hyles Euphorbiaw", "Spurge Hawk-moth", R.drawable.hawkmoth));
        mCreatures.add(Creature.newInstance("Agapostemon", "Metallic Green Bee", R.drawable.greenbee));
        mCreatures.add(Creature.newInstance("Graphocephala Coccinea", "Candy Striped Leafhopper", R.drawable.leafhopper));
        mCreatures.add(Creature.newInstance("Conocephalus Dorsalis", "Katydid", R.drawable.katydid));
        mCreatures.add(Creature.newInstance("Lithobates Pipiens", "Northern Tree Frog", R.drawable.frog));


        // Create the arraylist to hold the hashmap
        ArrayList<HashMap<String, Object>> creatureList = new ArrayList<HashMap<String, Object>>();
        // Loop through all the creatures
        for (Creature creature : mCreatures) {
            // Create a hash map and add the properties to it
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put(NAME, creature.getCommonName());
            hashMap.put(SCIENTIFIC, creature.getScientificName());
            hashMap.put(IMAGE, creature.getImageId());
            creatureList.add(hashMap);
        }

        //SimpleAdapter adapter = creatureAdapter();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            // Then add the stuff for the spinner
            //Toast.makeText(this, "The view is in portrait mode", Toast.LENGTH_LONG).show();
            // Create the spinner for portrait mode
            Spinner mCreatureSpinner = (Spinner)findViewById(R.id.listspinner);
            // set the adapter to it
            //mCreatureSpinner.setAdapter(adapter);
            mCreatureSpinner.setAdapter(new CreatureAdapter(this,creatureList));

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
                    // Do nothing
                    Log.i(TAG, "Nothing Selected");
                }
            });

        } else {
            // Set up for list view
            //Toast.makeText(this, "The view is in Landscape mode", Toast.LENGTH_LONG).show();
            // get the listview in landscape mode
            ListView listView = (ListView)findViewById(R.id.listView);
            // set the adapter to it
            //listView.setAdapter(adapter);
            listView.setAdapter(new CreatureAdapter(this,creatureList));

            // LISTEN FOR USER TO CLICK ON LIST AND DISPLAY ITEM DATA
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    setItemDataInView(parent, position);
                }
            });
        }

    }

    private void setItemDataInView(AdapterView<?> parent, int position) {
        // Get the current creature object at the position of the listview/spinner
        Object creature = parent.getItemAtPosition(position);
        Log.i(TAG, "Creature = " + creature);
        // Get the common name textview and pass in this objects value
        TextView commonTextView = (TextView)findViewById(R.id.common_name_textView);
        commonTextView.setText(mCreatures.get(position).getCommonName());
        // Get the scientific name textview and pass in that value
        TextView scientificTextView = (TextView)findViewById(R.id.scientific_name_textView);
        scientificTextView.setText(mCreatures.get(position).getScientificName());
        // Finally get and set the image into the imageview
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

    /*
    // Create a simple adapter to set the creature's common name in both the spinner and list view
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
    */
}
