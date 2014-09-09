package com.example.angessmith.foundationsapp;

// Created By: Angela Smith
// September 3, 2014
 /*
 Functional Changes
Show a Toast whenever a new entry is added (or removed) from the data collection that contains the contents of the entry.
Add a button that, when clicked, shows every element in the data collection in one AlertDialog.

Cosmetic Changes
Add the app icon to the title of the AlertDialog(s) being used in the app. Make sure the icon is the appropriate size as to match the size of the title text.
Change your app icon to be a white box with a blue checkmark. Link here.
  */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;



public class StateListActivity extends Activity {

    // Set the tag for debugging
    final String TAG = "Foundation State App";
    // Create the array adapter
    ArrayAdapter<String> stateArrayAdapter;
    String[] mStateHashArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // GET VIEW ITEMS
        // Get the add State and view all buttons
        final Button stateButton = (Button) findViewById(R.id.addStateButton);
        final Button viewAllButton = (Button) findViewById(R.id.viewAllButton);
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


        // ON VIEW ALL CLICK
        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Find out how many items are currently in the array
                int numberOfStates = mStateHashSet.size();
                        //getListCount(mStateHashSet, numberOfStatesVisited);
                Log.d(TAG, "There are " +numberOfStates + " states.");

                String alertMessage;
                if (numberOfStates == 0)
                {
                    alertMessage =  getString(R.string.view_all_empty_alert_message);
                }
                else {
                    // Create the message of all entered states
                    StringBuilder stringBuilder = getMessageString(mStateHashArray, numberOfStates);
                    alertMessage = stringBuilder.toString();
                }
                // Display the alert
                displayDialogAlert(alertMessage, getString(R.string.view_all_alert_title));
            }
        });

        // ON ADD STATE CLICK
        stateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Get the string in the text field
                String mStateText = String.valueOf(enteredStateText.getText());
                // Check the state entered
                //Log.d(TAG, "The entered state is: " + mStateText);
                // Define the message to be displayed to the user based on text entered
                String toastMessage;
                // Check if this is new or duplicate
                if (mStateHashSet.contains(mStateText)) {
                    toastMessage = "You have already added " + mStateText + ".";
                }
                else if ((mStateText.length() == 0)) {
                    toastMessage = "State was blank.";
                }
                else {
                    // Add the item to the hashset
                    mStateHashSet.add(mStateText);
                    // Create an array to hold the set and convert the hashset to the array
                    mStateHashArray = mStateHashSet.toArray(new String[mStateHashSet.size()]);
                    // Create the array adapter to view them in the list
                    stateArrayAdapter = new ArrayAdapter<String>(StateListActivity.this, android.R.layout.simple_list_item_1, mStateHashArray);
                    // set the array to the list
                    statesList.setAdapter(stateArrayAdapter);

                    // Set the average text length line
                    getAverageLength(mStateHashArray, mStateHashSet, averageCharactersText);
                    // Set the number of states line
                    getListCount(mStateHashSet, numberOfStatesVisited);
                    // Set the Toast message string
                    toastMessage = "You added " + mStateText + ".";
                }
                // Display the appropriate toast to the user
                Toast.makeText(StateListActivity.this, toastMessage, Toast.LENGTH_LONG).show();
                // Finally, empty out the text field
                enteredStateText.setText("");
            }
        });


        // LISTEN FOR USER TO CLICK ON LIST AND CREATE DIALOG STRINGS
        statesList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // Get the selected view
                TextView chosenStateView = (TextView) view;
                // Get the text from that view
                String chosenStateString = (String)chosenStateView.getText();
                String dialogMessage = getString(R.string.click_state_alert_message) + " "+ chosenStateString + ".";
                // Create the alert
               displayDialogAlert(dialogMessage, getString(R.string.click_state_alert_title));
            }
        });

    }

    private void displayDialogAlert(String message, String title) {
        // Build the alert dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the passed in message, title, and the created icon
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setIcon(R.drawable.ic_launcher);

        // Add a confirmation button
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // The user confirmed the entry
            }
        });
        // Create the alert dialog
        AlertDialog messageDialog = builder.create();
        // Show the message to the user
        messageDialog.show();
    }

    // CREATE THE STRING TO HOLD THE DIALOG MESSAGE STRING
    private StringBuilder getMessageString(String[] stateHashArray, int numberOfStates) {
        // Create the string builder and add the basic string
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.visited_string)).append(" ");
        // Loop through each entered state, and get the state at the index
        for (int i = 0; i < stateHashArray.length; i++){
            String state = stateHashArray[i];
            // If there is only one item, display that string
            if (stateHashArray.length == 1) {
                // Then we have one item
                stringBuilder.append(" ").append(state).append(".");
            }
            // Continue looping through and add a space, the state, and a comma and another space
            else {
                // Then we have to account for multiple items
                if (i < numberOfStates - 1) {
                    // Add a space, the state, and a comma for the next one
                    stringBuilder.append(" ").append(state).append(", ");
                }
                // If it is the final state, prepend with "and" and close with a period.
                else if (i == numberOfStates - 1) {
                    stringBuilder.append(" and ").append(state).append(".");
                }
            }
        }
        // Return the string builder for the dialog
        return stringBuilder;
    }

    // GET NUMBER OF ITEMS IN HASHSET AND CREATE TEXT STRING
    private int getListCount(HashSet<String> mStateHashSet, TextView numberOfStatesVisited) {
        int numberOfStates = mStateHashSet.size();
        // set that number to the number field
        if (numberOfStates == 1) {
            numberOfStatesVisited.setText(getString(R.string.visited_string) + " " + numberOfStates + " state!");
        } else {
            numberOfStatesVisited.setText(getString(R.string.visited_string) + " " +  numberOfStates + " states!");
        }
        return numberOfStates;
    }

    // GET THE AVERAGE LENGTH OF EACH OF THE STATES IN THE SET AND SET THE TEXT
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
        }
        // Take the new total and divide it by the number of items in the list
        textLength = textLength/mStateHashSet.size();
        intVal = String.valueOf(textLength);
        //Log.i(TAG, "The average number of characters equals = "+ intVal);
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
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
