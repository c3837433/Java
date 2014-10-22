package com.example.angessmith.spinnernavapp;

import android.app.Activity;
import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.angessmith.spinnernavapp.Fragment.RosterListFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements ActionBar.OnNavigationListener {

    /*
    Case Study 1
"Your client has requested you build an application that serves as a roster for a
little league baseball league with 6 teams of 9 players. They would like the application
to allow the user to select a team and display the names for each member of the selected team."


     */
    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private static final String TAG = "MAIN ACTIVITY";
    private ArrayList<RosterClass> mRosterList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CreateRosters();
        // Get the team name from the class
        List<String> teamLists = new ArrayList<String>();
        for (RosterClass team : mRosterList) {
            // get the name
            String teamName = team.getTeamName();
            teamLists.add(teamName);
        }
        // change the list to an string array
        String [] teamNames = teamLists.toArray(new String[teamLists.size()]);

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        // Set the navigation mode to a list navigation
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(

                // Pass the team names into the spinner adapter
                new ArrayAdapter<String>(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1, teamNames),
                this);
    }

    private ArrayList<RosterClass> CreateRosters() {
        // Build the objects
        Log.i(TAG, "Roster info = " + mRosterList);
        if (mRosterList != null) {
            Log.i(TAG, "ROSTER != NULL " + mRosterList);
            return mRosterList;
        }
        mRosterList = new ArrayList<RosterClass>();
        String[] teamOne =  new String[]{"John Arnold", "Reed Helton", "George Asyres", "Bobby Salvato", "Juan Schroyer", "Albert Murphy", "Joey Kanode", "Charlie Reed", "Ed Cole"};
        String[] teamTwo = new String[] {"Adam Olson", "Don Peebles", "Steve Fuller", "Brady Vandyne", "Jay Carlson", "Greg Morris", "Jeff Chao", "Johnny Jowett", "Rob Fauver"};
        String[] teamThree = new String[] {"Dennis Galligan", "Todd Sisco", "Julius Peterson", "Daniel Jewell", "John Chapelle", "Luis Friedman", "Andrew Nelson", "Jake Taylor", ""};
        String[] teamFour = new String[] {"Steve Workman", "Brian Winkler", "Donald Dunham", "Tom Coleman", "Joe Johnston", "Charley Griffith", "James Prine", "Raymond Almaraz", "Will Gomez"};
        String[] teamFive = new String[] {"Dewayne K. Mueller", "Steve Holmes", "John McCardle", "Don Young", "David Lomax", "Dale Richardson", "Terrence Ryles", "Jesus Looz", "Francisco Laduke"};
        String[] teamSix = new String[] {"William Key", "Jack Benson", "Raymond Powell", "Dan Hendrix", "Adam Clack", "Rusty Anrade", "Olen Martin", "Antoine Adkins", "Juan Wells"};
        mRosterList.add(RosterClass.newInstance("Gotham Rogues", teamOne));
        mRosterList.add(RosterClass.newInstance("Mountain Knights", teamTwo));
        mRosterList.add(RosterClass.newInstance("Average Joes", teamThree));
        mRosterList.add(RosterClass.newInstance("Twin Hawks", teamFour));
        mRosterList.add(RosterClass.newInstance("Sandlot Heros", teamFive));
        mRosterList.add(RosterClass.newInstance("Field Boys", teamSix));
        Log.i(TAG, "NEW Roster info = " + mRosterList);
        return mRosterList;
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.
        getFragmentManager().beginTransaction()
                .replace(R.id.container, RosterListFragment.newInstance(position + 1, mRosterList))
                .commit();
        return true;
    }

}
