package com.example.angessmith.foundationsapp;

// Created By: Angela Smith
// September 3, 2014

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
im//port android.widget.AdapterView;
import android.widget.ArrayAdapter;
im//port android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
im//port android.widget.Toast;

import java.util.ArrayList;


public class StateListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        // Get the add State button
        final Button stateButton = (Button) findViewById(R.id.addStateButton);
        // Get the text field
        final TextView enteredStateText = (TextView) findViewById(R.id.enteredStateText);

        // Get the view to hold the number of states visited
        final TextView numberOfStatesVisited = (TextView) findViewById(R.id.number_of_states_view);
        // Get the listview that will hold all the states the user has visited
        //final ListView statesList = (ListView) findViewById(R.id.statesVisitedList);
        final ListView statesList = (ListView) findViewById(android.R.id.list);



        // Practice playing with autocomplete
        // Get the autocomplete textview
        //AutoCompleteTextView autoEnteredText = (AutoCompleteTextView) findViewById(R.id.autocomplete_state);
        // Get the list of states in the string array
        //String[] statesAvailable = getResources().getStringArray(R.array.states_array);
        // Tell the textview to use the strings in the array to autocomplete when the user begins to enter text
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, statesAvailable);
        // Add the array adapter to the textview
        //autoEnteredText.setAdapter(arrayAdapter);

        // Declare the array
        final ArrayList<String> stateList = new ArrayList<String>();

        // Create the Array Adapter to hold the states
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stateList);
        // Set the array to the arapter for the listview
        statesList.setAdapter(arrayAdapter);

        // Create the on click listener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // Get the string in the text field
                String stateText = String.valueOf(enteredStateText.getText());
                // Check the state entered
                Log.d("The entered state is: ", stateText);
                if (stateList.contains(stateText))
                {
                    // Then toast user this is a duplicate
                    String duplicateString = "This state has already been entered.";
                    //Toast duplicateToast = Toast.makeText(this,duplicateString,Toast.LENGTH_LONG).show();
                    Log.d("Bummer", duplicateString);
                }
                else
                {
                    String newStateString = "\"The State is New";
                    // toast user this is a good state
                    Log.d("Yea", newStateString);
                    // Add the state to the array
                    stateList.add(stateText);
                    // Update the number of items in the array
                    setNumberOfStates();

                }
                // empty out the text field
                enteredStateText.setText("");

            }

            private void setNumberOfStates() {
                int numberOfStates = stateList.size();
                // set that number to the number field
                if (numberOfStates == 1)
                    numberOfStatesVisited.setText("You have visited " + numberOfStates + " state!");
                else
                    numberOfStatesVisited.setText("You have visited " + numberOfStates + " states!");
            }
        };

        // Set the on click listener to the add state button
        stateButton.setOnClickListener(listener);

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
