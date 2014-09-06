package com.example.angessmith.foundationsapp;

// Created By: Angela Smith
// September 3, 2014

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.HashSet;



public class StateListActivity extends Activity {

    // Set the tag for debugging
    final String TAG = "Foundation State App";
    // Create the array adapter
    ArrayAdapter<String> stateArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // GET VIEW ITEMS
        // Get the add State button
        final Button stateButton = (Button) findViewById(R.id.addStateButton);
        // Get the text field
        final TextView enteredStateText = (TextView) findViewById(R.id.enteredStateText);
        // Get the view to hold the number of states visited
        final TextView numberOfStatesVisited = (TextView) findViewById(R.id.number_of_states_view);
        // Get the list view that will hold all the states the user has visited
        final ListView statesList = (ListView) findViewById(android.R.id.list);
        // Get the text view for the average number of characters in the states
        final TextView averageCharactersText = (TextView) findViewById(R.id.average_number_characters_in_states);

        // CREATE THE HASHSET
        final HashSet<String> mStateHashSet = new HashSet<String>();
        // BUILD THE ALERT DIALOG
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);


        stateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Get the string in the text field
                String mStateText = String.valueOf(enteredStateText.getText());
                // Check the state entered
                Log.d(TAG, "The entered state is: " + mStateText);

                // Add the item to the hashset
                mStateHashSet.add(mStateText);
                // Create an array to hold the set and convert the hashset to the array
                String[] stateHashArray = mStateHashSet.toArray(new String[mStateHashSet.size()]);
                // set the adapter
                // Create the array adapter
                //ArrayAdapter<String> stateArrayAdapter;
                stateArrayAdapter = new ArrayAdapter<String>(StateListActivity.this, android.R.layout.simple_list_item_1, stateHashArray);
                // set the array to the list
                statesList.setAdapter(stateArrayAdapter);

                // Set the average text length
                getAverageLength(stateHashArray, mStateHashSet, averageCharactersText);

                // Find out how many items are currently in the array
                getListCount(mStateHashSet, numberOfStatesVisited);

                // Finally, empty out the text field
                enteredStateText.setText("");
            }
        });


        // Set the on click listener for the list view
        statesList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // Get the selected view
                TextView chosenStateView = (TextView) view;
                // Get the text from that view
                String chosenStateString = (String) chosenStateView.getText();
                Log.i(TAG, "The state  of " + chosenStateString + " was selected");
                // Create the alert
                builder.setMessage("You visited "+ chosenStateString + ".");
                builder.setTitle("State was Selected");
                // Add a confirmation button
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // The user confirmed the entry
                }
            });
                // Create the alert dialog
                AlertDialog messageDialog = builder.create();
                // Show the message to the user
                messageDialog.show();
            }
        });

    }

    private void getListCount(HashSet<String> mStateHashSet, TextView numberOfStatesVisited) {
        int numberOfStates = mStateHashSet.size();
        // set that number to the number field
        if (numberOfStates == 1)
            numberOfStatesVisited.setText("You have visited " + numberOfStates + " state!");
        else
            numberOfStatesVisited.setText("You have visited " + numberOfStates + " states!");
    }

    private void getAverageLength(String[] stateHashArray, HashSet<String> mStateHashSet, TextView averageCharactersText) {
        // set the text length to 0
        int textLength = 0;
        // create a string to display the value
        String intVal;
        // Loop through each state in the list
        for (String state : stateHashArray)
        {
            // Add the number of characters for each word
            textLength += state.length();
            // Set the value equal to the total text length
            intVal = String.valueOf(textLength);
            Log.i(TAG, "The number of total characters equals = " + intVal);
        }
        // Take the new total and divide it by the number of items in the list
        textLength = textLength/mStateHashSet.size();
        intVal = String.valueOf(textLength);
        Log.i(TAG, "The average number of characters equals = "+ intVal);
        // Set the string to the text view based on plurality
        if (mStateHashSet.size() == 1) {
            averageCharactersText.setText("That state has " + intVal + " characters");
        }
        else if (mStateHashSet.size() > 1) {
            averageCharactersText.setText("Those states average " + intVal + " characters");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*
        if (id == R.id.action_settings) {
            return true;
        }*/
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
        //return super.onOptionsItemSelected(item);
    }
}
