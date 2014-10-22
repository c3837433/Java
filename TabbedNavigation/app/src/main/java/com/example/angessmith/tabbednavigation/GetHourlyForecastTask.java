package com.example.angessmith.tabbednavigation;

import android.os.AsyncTask;
import android.util.Log;

import com.example.angessmith.tabbednavigation.Fragment.HourlyForecastFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

// Created by AngeSSmith on 10/22/14.

// Get Hourly forecast task
public class GetHourlyForecastTask extends AsyncTask<String, Integer, ArrayList<HourlyForecast>> {
    final String TAG = "HOURLY_FORECAST_TASK";
    ArrayList<HourlyForecast> mHourlyForecastList;
    private ArrayList<HashMap<String, Object>> mHourlyMapList;

    @Override
    protected void onPreExecute() {
    }
    @Override
    protected ArrayList<HourlyForecast> doInBackground(String... params) {
        String urlString = params[0];
        Log.d(TAG, urlString);
        String returnedData = HTTPHelper.pullWeatherData(null, params[0]);
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
                String iconUrl = object.getString("icon_url");
                JSONObject tempObject = object.getJSONObject("temp");
                String tempEnglish = tempObject.getString("english");
                String condition = object.getString("condition");
                JSONObject feelsObject = object.getJSONObject("feelslike");
                String feelsLike = feelsObject.getString("english");
                // Create the Hourly Objects as they are added to the list
                mHourlyForecastList.add(HourlyForecast.newInstance(tempEnglish, condition, timeString, feelsLike, iconUrl));
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
         final String HOURLY_TEMP = "temperature";
         final String HOURLY_TIME = "timeHour";
         final String HOURLY_CONDITION = "condition";
         final String HOURLY_FEELS_LIKE = "feelsLike";
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
