package com.example.angessmith.tabbednavigation;

import android.os.AsyncTask;
import android.util.Log;

import com.example.angessmith.tabbednavigation.Fragment.WeeklyForecastFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AngeSSmith on 10/22/14.
 */
// Get Weekly forecast task
public class GetWeeklyForecastTask extends AsyncTask<String, Integer, ArrayList<WeeklyForecast>> {

    final String TAG = "WEEKLY_FORECAST_TASK";
    ArrayList<WeeklyForecast> mWeeklyForecastList;
    private ArrayList<HashMap<String, Object>> mWeeklyMapList;

    @Override
    protected void onPreExecute() {
    }
    @Override
    protected ArrayList<WeeklyForecast> doInBackground(String... params) {

        String urlString = params[0];
        Log.d(TAG, urlString);
        String returnedData = HTTPHelper.pullWeatherData(null, params[0]);
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
        final String WEEKLY_DAY = "dayWeek";
        final String WEEKLY_DESCRIPTION = "description";
        final String WEEKLY_IMAGE_ICON = "hourlyIcon";
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
