package com.example.angessmith.foundationsapp;

// Created By: Angela Smith
// September 3, 2014

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        // Declare the array
        final ArrayList<String> stateList = new ArrayList<String>();
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
                }
                // empty out the text field
                enteredStateText.setText("");

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
