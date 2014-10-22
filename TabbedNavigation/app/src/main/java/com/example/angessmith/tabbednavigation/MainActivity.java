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
import android.support.v13.app.FragmentStatePagerAdapter;
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

    // Define the strings for the hourly map
    static final String HOURLY_TEMP = "temperature";
    static final String HOURLY_TIME = "timeHour";
    static final String HOURLY_CONDITION = "condition";
    static final String HOURLY_FEELS_LIKE = "feelsLike";
    // And the weekly map
    static final String WEEKLY_DAY = "dayWeek";
    static final String WEEKLY_DESCRIPTION = "description";
    static final String WEEKLY_IMAGE_ICON = "hourlyIcon";

    static final String ARG_ACTIVITY_CURRENT_CONDITION = "MainActivity.ARG_ACTIVITY_CURRENT_CONDITION";
    static final String ARG_ACTIVITY_HOURLY_LIST = "MainActivity.ACTIVITY_HOURLY_LIST";
    static final String ARG_ACTIVITY_HOURLY_MAP = "MainActivity.ACTIVITY_HOURLY_MAP";
    static final String ARG_ACTIVITY_WEEKLY_LIST = "MainActivity.ACTIVITY_WEEKLY_LIST";
    static final String ARG_ACTIVITY_WEEKLY_MAP = "MainActivity.ACTIVITY_WEEKLY_MAP";

    final String TAG = "MainActivity";
    CurrentCondition mCondition;
                /**
             * The {@link android.support.v4.view.PagerAdapter} that will provide
             * fragments for each of the sections. We use a
             * {@link FragmentPagerAdapter} derivative, which will keep every
             * loaded fragment in memory. If this becomes too memory intensive, it
             * may be best to switch to a
             * {@link android.support.v13.app.FragmentStatePagerAdapter}.
             */
    //SectionsPagerAdapter mSectionsPagerAdapter;
    FragmentStateAdapter mFragmentPagerAdapter;
    ViewPager mViewPager;
    // Define the shared preferences for the location to search
    private SharedPreferences mSettingPreference;
    private ArrayList<HourlyForecast> mHourlyForecastList;
    private ArrayList<WeeklyForecast> mWeeklyForecastList;
    private ArrayList<HashMap<String, Object>> mHourlyMapList;
    private ArrayList<HashMap<String, Object>> mWeeklyMapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "ON CREATE: Called");
        // Set up the preferences
        setUpPreferences();

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        // set to use tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mFragmentPagerAdapter = new FragmentStateAdapter(getFragmentManager());

        // Set up the ViewPager  (from the activity xml)with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        // set the created adapter to it
        //mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setAdapter(mFragmentPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        /*
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        */
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mFragmentPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mFragmentPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        Log.i(TAG, "ON SAVED INSTANCE STATE: Called");
        savedInstanceState.putSerializable(ARG_ACTIVITY_CURRENT_CONDITION, mCondition);
        savedInstanceState.putSerializable(ARG_ACTIVITY_HOURLY_LIST, mHourlyForecastList);
        savedInstanceState.putSerializable(ARG_ACTIVITY_HOURLY_MAP, mHourlyMapList);
        savedInstanceState.putSerializable(ARG_ACTIVITY_WEEKLY_LIST, mWeeklyForecastList);
        savedInstanceState.putSerializable(ARG_ACTIVITY_WEEKLY_MAP, mWeeklyMapList);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "ON RESTORE INSTANCE STATE: Called");
        // Restore state members from saved instance
        mCondition = (CurrentCondition) savedInstanceState.getSerializable(ARG_ACTIVITY_CURRENT_CONDITION);
        mHourlyForecastList = (ArrayList<HourlyForecast>) savedInstanceState.getSerializable(ARG_ACTIVITY_HOURLY_LIST);
        mHourlyMapList = (ArrayList<HashMap<String, Object>>) savedInstanceState.getSerializable(ARG_ACTIVITY_HOURLY_MAP);
        mWeeklyForecastList = (ArrayList<WeeklyForecast>) savedInstanceState.getSerializable(ARG_ACTIVITY_WEEKLY_LIST);
        mWeeklyMapList = (ArrayList<HashMap<String, Object>>) savedInstanceState.getSerializable(ARG_ACTIVITY_WEEKLY_MAP);
        Log.i(TAG, "ON RESTORE INSTANCE STATE: mCondition: " + mCondition);
        Log.i(TAG, "ON RESTORE INSTANCE STATE: mHourlyForecastList: " + mHourlyForecastList);
        Log.i(TAG, "ON RESTORE INSTANCE STATE: mHourlyMapList: " + mHourlyMapList);
        Log.i(TAG, "ON RESTORE INSTANCE STATE: mWeeklyForecastList: " + mWeeklyForecastList);
        Log.i(TAG, "ON RESTORE INSTANCE STATE: mWeeklyMapList: " + mWeeklyMapList);
    }

    private void setUpPreferences() {
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
            editor.putString("EDIT_CITY_PREFERENCE", "Chaska");
            editor.putString("EDIT_STATE_PREFERENCE", "MN");
            editor.commit();
            // and set the title with the default
            this.getActionBar().setTitle("Chaska, MN");
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
        // Load the preference fragment
        switch (item.getItemId())
        {
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

    // Method that gets the correct api url based on user location and api preference
    public String getLocation(int tab) {
        // Make sure the strings have no spaces
        String city = mSettingPreference.getString("EDIT_CITY_PREFERENCE","Chaska").replaceAll(" ", "_");
        String state = mSettingPreference.getString("EDIT_STATE_PREFERENCE","MN").replaceAll(" ", "_");
        String apiType = "";
        switch (tab) {
            case 1:
                apiType = "conditions";
                break;
            case 2:
                apiType = "hourly10day";
                break;
            case 3:
                apiType = "forecast10day";
                break;
        }
        return  "http://api.wunderground.com/api/3d402f1818f340e0/" + apiType +"/q/" + state +"/" + city + ".json";
    }

    public void getCurrentConditions () {
        // check for a connection
        ConnectionHelper connectionHelper = new ConnectionHelper(this);
        boolean isReady = connectionHelper.canConnectInternet();
        if (isReady) {
            // Get the location
            //String locationApi = getLocation(1);
            GetCurrentCondition conditionsTask = new GetCurrentCondition();
            conditionsTask.execute(getLocation(1));
        }
    }

    public void getHourlyForecast() {
        // check connection again
        ConnectionHelper connectionHelper = new ConnectionHelper(this);
        boolean isReady = connectionHelper.canConnectInternet();
        if (isReady) {
            // set string to the hourly
            GetHourlyForecast hourlyTask = new GetHourlyForecast();
            // get the location api string and execute it
            hourlyTask.execute(getLocation(2));
        }

    }

    public void getWeeklyForecast() {
        // verify connection
        ConnectionHelper connectionHelper = new ConnectionHelper(this);
        boolean isReady = connectionHelper.canConnectInternet();
        if (isReady) {
            // set string to the 10 day forecast
            //String locationApi = getLocation(3);
            //String urlString = "http://api.wunderground.com/api/3d402f1818f340e0/forecast10day/q/" + location + ".json";
            // Execute the task
            GetWeeklyForecast weeklyTask = new GetWeeklyForecast();
            weeklyTask.execute(getLocation(3));
        }
    }

    /*
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // Get the right fragment based on tab position
            Fragment fragment = null;
            // See which position was selected
            switch (position) {
                // Create a new instance of each fragment
                case 0:
                    getCurrentConditions();
                    fragment = CurrentForecastFragment.newInstance(position + 1, MainActivity.this);

                    break;
                case 1:
                    getHourlyForecast();
                    fragment = HourlyForecastFragment.newInstance(position + 1);
                    break;
                case 2:
                    // Make sure we have a valid list
                    getWeeklyForecast();
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
    */

    // CHANGED PAGER ADAPTER TO FragmentStatePagerAdapter because of memory issues
    public class FragmentStateAdapter extends FragmentStatePagerAdapter {
        public FragmentStateAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                // Create a new instance of each fragment
                case 0:
                    getCurrentConditions();
                    Log.i(TAG, "GET ITEM: Loading Conditions Fragment");
                    fragment = CurrentForecastFragment.newInstance(position + 1, MainActivity.this);

                    break;
                case 1:
                    getHourlyForecast();
                    Log.i(TAG, "GET ITEM: Loading Hourly Fragment");
                    fragment = HourlyForecastFragment.newInstance(position + 1);
                    break;
                case 2:
                    getWeeklyForecast();
                    Log.i(TAG, "GET ITEM: Loading Weekly Fragment");
                    fragment = WeeklyForecastFragment.newInstance(position + 1);

                    break;
                default:
                    break;
            }
            return fragment;
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
    public class GetHourlyForecast extends AsyncTask<String, Integer, ArrayList<HourlyForecast>> {
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
            mHourlyMapList = new ArrayList<HashMap<String, Object>>();
            // loop through the hours
            for (HourlyForecast hour : forecast) {   // Create a new map
                HashMap<String, Object> map = new HashMap<String, Object>();
                // the keys/value pairs
                map.put(HOURLY_CONDITION, hour.getCondition());
                map.put(HOURLY_TEMP, hour.getTemperatureString());
                map.put(HOURLY_TIME, hour.getTimeHour());
                map.put(HOURLY_FEELS_LIKE, hour.getFeelsLike());
                // Add to the list
                mHourlyMapList.add(map);
                //Log.i(TAG, "Map info: " + map);
            }
            // and call to set the items in the custom adapter
            HourlyForecastFragment.SetCustomListInAdapter(mHourlyMapList);
        }
    }

    // Get Weekly forecast task
    private class GetWeeklyForecast extends AsyncTask<String, Integer, ArrayList<WeeklyForecast>> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected ArrayList<WeeklyForecast> doInBackground(String... params) {
            String urlString = params[0];
            Log.d(TAG, urlString);
            String returnedData = HTTPHelper.pullWeatherData(MainActivity.this, params[0]);
            try {
                // Create an object
                JSONObject jsonObject = new JSONObject(returnedData);
                Log.d(TAG, jsonObject.toString());
                // Get the forecast object
                JSONObject forecastObject = jsonObject.getJSONObject("forecast");
                JSONObject txtForecastObject = forecastObject.getJSONObject("txt_forecast");
                // And the array of days in it
                JSONArray dayArray = txtForecastObject.getJSONArray("forecastday");
                // Loop through the days and get the objects
                mWeeklyForecastList = new ArrayList<WeeklyForecast>();
                for (int i = 0; i < dayArray.length(); i++) {
                    JSONObject dayObject = dayArray.getJSONObject(i);
                    // Get the day, description, and urlString
                    String dayString = dayObject.getString("title");
                    String descString = dayObject.getString("fcttext");
                    String iconUrlString = dayObject.getString("icon_url");
                    // create a new object
                    mWeeklyForecastList.add(WeeklyForecast.newInstance(dayString, descString, iconUrlString));
                }
                return mWeeklyForecastList;
            }
            catch (JSONException e) {
                Log.e(TAG, "Unable to convert to JSON");
            }
            return mWeeklyForecastList;
        }

        @Override
        protected void onPostExecute(ArrayList<WeeklyForecast> forecast) {
            // Create the hashmap of the objects
            mWeeklyMapList = new ArrayList<HashMap<String, Object>>();
            // loop through the hours
            for (WeeklyForecast day : forecast) {   // Create a new map
                HashMap<String, Object> map = new HashMap<String, Object>();
                // set the keys and values
                map.put(WEEKLY_DAY, day.getDay());
                map.put(WEEKLY_DESCRIPTION, day.getDescription());
                map.put(WEEKLY_IMAGE_ICON, day.getIconString());
                // Add to the fragment list adapter
                mWeeklyMapList.add(map);
            }
            WeeklyForecastFragment.setDaysInWeekView(mWeeklyMapList);
        }
    }
}
