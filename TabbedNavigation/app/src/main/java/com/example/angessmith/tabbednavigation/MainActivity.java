package com.example.angessmith.tabbednavigation;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.angessmith.tabbednavigation.Fragment.CurrentForecastFragment;
import com.example.angessmith.tabbednavigation.Fragment.HourlyForecastFragment;
import com.example.angessmith.tabbednavigation.Fragment.SettingsFragment;
import com.example.angessmith.tabbednavigation.Fragment.WeeklyForecastFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

// http://api.wunderground.com/api/3d402f1818f340e0/forecast/q/CA/San_Francisco.json
    //http://api.wunderground.com/api/3d402f1818f340e0/forecast/q/55318.json
// http://api.wunderground.com/api/3d402f1818f340e0/hourly/q/CA/San_Francisco.json
/*
Case Study 2
"Your client has requested you build a weather application prototype using the weather underground API
 that provides the user with three features that will be frequently accessed:
    1. a detailed forecast for the current time,
    2. an hour by hour forecast,
    3. a weekly forecast."
 */
public class MainActivity extends Activity implements ActionBar.TabListener {

    // Define the shared preferences for the location to search
    private SharedPreferences mSettingPreference;
    final String TAG = "MainActivity";
    // Define the strings for the map
    static final String HOURLY_TEMP = "temperature";
    static final String HOURLY_TIME = "timeHour";
    static final String HOURLY_CONDITION = "condition";
    static final String HOURLY_FEELS_LIKE = "feelsLike";

    CurrentCondition mCondition;
    private ArrayList<HourlyForecast> mHourlyForecastList;
                /**
             * The {@link android.support.v4.view.PagerAdapter} that will provide
             * fragments for each of the sections. We use a
             * {@link FragmentPagerAdapter} derivative, which will keep every
             * loaded fragment in memory. If this becomes too memory intensive, it
             * may be best to switch to a
             * {@link android.support.v13.app.FragmentStatePagerAdapter}.
             */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    /*
    Back in the onCreate() method of the activity, a few things are happening for our setup.
        1. The first thing that happens is that the action bar is set to use tabs for navigation.
        2. Then we create a new adapter for the ViewPager and assign it to that pager.
        3. Then, a SimpleOnPageChangeListener is attached to the pager so that everytime the user swipes,
        the listener can catch that and set the appropriate tab to be selected.
        4. Lastly, at the bottom of onCreate(), a new tab is added for each of the views in the
        ViewPager to create consistent navigation.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the preferences
        mSettingPreference = getPreferences(MODE_PRIVATE);
        if (mSettingPreference.contains("EDIT_CITY_PREFERENCE")) {
            // get the current values for the city/state
            String cityPreference = mSettingPreference.getString("EDIT_CITY_PREFERENCE","Chaska");
            String statePreference = mSettingPreference.getString("EDIT_STATE_PREFERENCE","MN");
            Log.i(TAG, "User search = " + cityPreference + ", " + statePreference);
            // change the action bar title
            this.getActionBar().setTitle(cityPreference + ", " + statePreference);
        } else {
            // define default
            Log.i(TAG, "User search has not been set ");
            // set the default
            SharedPreferences settingsPreferences = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settingsPreferences.edit();
            // save the city to the preferences
            editor.putString("LOCATION_CITY_PREFERENCE", "Chaska");
            editor.putString("EDIT_STATE_PREFERENCE", "MN");
            editor.commit();
            // and set the title with the default
            this.getActionBar().setTitle("Chaska, MN");
        }
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        // set to use tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager  (from the activity xml)with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        // set the created adapter to it
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
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
        switch (item.getItemId())
        {
            // (only "Preferences" right now...
            case R.id.action_settings:
                // Load the preference fragment
                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, new SettingsFragment())
                                // make sure we can get back to the main view
                        .addToBackStack("settings")
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
        /*
        This FragmentPagerAdapter class is a type of specialized adapter that's used in conjunction
        with a ViewPager to show fragments for each view in the ViewPager. This adapter is used to
        create and return fragments (currently only a placeholder fragment), get the total number
        of pages to swipe between, and get the title of each page in the ViewPager.
         */

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            Fragment fragment = null;
            // See which position was selected
            switch (position) {
                // Create a new instance of each fragment
                case 0:
                    getCurrentConditions();
                    fragment = CurrentForecastFragment.newInstance(position + 1);

                    break;
                case 1:
                    getHourlyForecast();
                    fragment = HourlyForecastFragment.newInstance(position + 1);
                    break;
                case 2:
                    fragment = WeeklyForecastFragment.newInstance(position + 1);
                    break;
                default:
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            // We have three pages, current, hourly, and weekly
            return 3;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            // Get the string for the title of each tab
            switch (position) {
                case 0:
                    return getString(R.string.title_tab1);
                case 1:
                    return getString(R.string.title_tab2);
                case 2:
                    return getString(R.string.title_tab3);
            }
            return null;
        }
    }
    public void getCurrentConditions () {
        // check for a connection
        ConnectionHelper connectionHelper = new ConnectionHelper(this);
        boolean isReady = connectionHelper.canConnectInternet();
        if (isReady) {
            String urlString = "http://api.wunderground.com/api/3d402f1818f340e0/conditions/q/" + mSettingPreference.getString("EDIT_STATE_PREFERENCE","MN")+ "/" + mSettingPreference.getString("EDIT_CITY_PREFERENCE","Chaska")+ ".json";
            GetCurrentCondition conditionsTask = new GetCurrentCondition();
            conditionsTask.execute(urlString);
        }
    }

    public void getHourlyForecast() {
        // check connection again
        ConnectionHelper connectionHelper = new ConnectionHelper(this);
        boolean isReady = connectionHelper.canConnectInternet();
        if (isReady) {
            // set string to the hourly
            String urlString = "http://api.wunderground.com/api/3d402f1818f340e0/hourly10day/q/" + mSettingPreference.getString("EDIT_STATE_PREFERENCE","MN")+ "/" + mSettingPreference.getString("EDIT_CITY_PREFERENCE","Chaska")+ ".json";
            GetHourlyForecast hourlyTask = new GetHourlyForecast();
            hourlyTask.execute(urlString);
        }

    }
    //http://api.wunderground.com/api/3d402f1818f340e0/conditions/q/CA/San_Francisco.json
    // Task to get current conditions
    private class GetCurrentCondition extends AsyncTask<String, Integer, CurrentCondition> {
        // onPreExecute has access to the class
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected CurrentCondition doInBackground(String... params) {
            // Use the HTTP Manager class to get the api data
            String urlstring = params[0];
            Log.d(TAG, urlstring);
            String returnedData = HTTPHelper.pullWeatherData(MainActivity.this, params[0]);
            try {
                // Create an object
                JSONObject jsonObject = new JSONObject(returnedData);
                Log.d(TAG, jsonObject.toString());
                JSONObject currentObject = jsonObject.getJSONObject("current_observation");
                Log.d(TAG, currentObject.toString());
                // Get the current values
                // order: (weather, temp, humidity, wind, pressure, precip, dewpoint, feelsLike,  iconUrl)
                // Create the new object
                mCondition = CurrentCondition.newInstance(currentObject.getString("weather"),currentObject.getString("temperature_string"), currentObject.getString("relative_humidity"), currentObject.getString("wind_string"), currentObject.getString("pressure_in"),currentObject.getString("precip_1hr_string"),currentObject.getString("dewpoint_string"),currentObject.getString("feelslike_string"),currentObject.getString("icon_url") );
                return mCondition;
            }
            catch (JSONException e) {
                Log.e(TAG, "Unable to convert to JSON");
            }
            //
            return mCondition;
        }
        @Override
        protected void onPostExecute(CurrentCondition condition) {
            // set the value over
            CurrentForecastFragment.mCondition = condition;
            CurrentForecastFragment.SetConditionsToView();
        }
    }

    // Get Hourly forecast task
    private class GetHourlyForecast extends AsyncTask<String, Integer, ArrayList<HourlyForecast>> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected ArrayList<HourlyForecast> doInBackground(String... params) {
            String urlString = params[0];
            Log.d(TAG, urlString);
            String returnedData = HTTPHelper.pullWeatherData(MainActivity.this, params[0]);
            try {
                // Create an object
                JSONObject jsonObject = new JSONObject(returnedData);
                Log.d(TAG, jsonObject.toString());
                JSONArray hourlyArray = jsonObject.getJSONArray("hourly_forecast");
                mHourlyForecastList = new ArrayList<HourlyForecast>();
                for (int i = 0; i < hourlyArray.length(); i++) {
                    JSONObject object = hourlyArray.getJSONObject(i);
                    JSONObject timeObject = object.getJSONObject("FCTTIME");
                    String timeString = timeObject.getString("civil");

                    JSONObject tempObject = object.getJSONObject("temp");
                    String tempEnglish = tempObject.getString("english");
                    String condition = object.getString("condition");
                    JSONObject feelsObject = object.getJSONObject("feelslike");
                    String feelsLike = feelsObject.getString("english");
                    // Create the Hourly Objects as they are added to the list
                    mHourlyForecastList.add(HourlyForecast.newInstance(tempEnglish, condition, timeString, feelsLike));
                }
               return mHourlyForecastList;
            }
            catch (JSONException e) {
                Log.e(TAG, "Unable to convert to JSON");
            }
            //
            return mHourlyForecastList;
        }

        @Override
        protected void onPostExecute(ArrayList<HourlyForecast> forecast) {
            // Create the hashmap of the objects
            ArrayList<HashMap<String, Object>> hourlyForecastList = new ArrayList<HashMap<String, Object>>();
            // loop through the hours
            for (HourlyForecast hour : forecast) {   // Create a new map
                HashMap<String, Object> map = new HashMap<String, Object>();
                // the keys/value pairs
                map.put(HOURLY_CONDITION, hour.getCondition());
                map.put(HOURLY_TEMP, hour.getTemperatureString());
                map.put(HOURLY_TIME, hour.getTimeHour());
                map.put(HOURLY_FEELS_LIKE, hour.getFeelsLike());
                // Add to the list
                hourlyForecastList.add(map);
                Log.i(TAG, "Map info: " + map);
                // and call the set the items in the custom adapter
                HourlyForecastFragment.SetCustomListInAdapter(hourlyForecastList);
            }
        }
    }
}
