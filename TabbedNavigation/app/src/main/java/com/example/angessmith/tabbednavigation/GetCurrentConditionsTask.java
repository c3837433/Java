package com.example.angessmith.tabbednavigation;

import android.os.AsyncTask;
import android.util.Log;

import com.example.angessmith.tabbednavigation.Fragment.CurrentForecastFragment;

import org.json.JSONException;
import org.json.JSONObject;

 // Created by AngeSSmith on 10/22/14.

public class GetCurrentConditionsTask extends AsyncTask<String, Integer, CurrentCondition> {
    final String TAG = "CONDITIONS_TASK";
    CurrentCondition condition;
    @Override
    protected void onPreExecute() {
    }
    @Override
    protected CurrentCondition doInBackground(String... params) {
        // Use the HTTP Manager class to get the api data
        String urlstring = params[0];
        Log.d(TAG, urlstring);
        String returnedData = HTTPHelper.pullWeatherData(null, params[0]);
        try {
            // Create an object
            JSONObject jsonObject = new JSONObject(returnedData);
            Log.d(TAG, jsonObject.toString());
            JSONObject currentObject = jsonObject.getJSONObject("current_observation");
            Log.d(TAG, currentObject.toString());
            // order: (weather, temp, humidity, wind, pressure, precip, dewpoint, feelsLike,  iconUrl)
            // Create the new object
            condition = CurrentCondition.newInstance(currentObject.getString("weather"),currentObject.getString("temperature_string"), currentObject.getString("relative_humidity"), currentObject.getString("wind_string"), currentObject.getString("pressure_in"),currentObject.getString("precip_1hr_string"),currentObject.getString("dewpoint_string"),currentObject.getString("feelslike_string"),currentObject.getString("icon_url") );
            return condition;
        }
        catch (JSONException e) {
            Log.e(TAG, "Unable to convert to JSON");
        }
        //
        return condition;
    }
    @Override
    protected void onPostExecute(CurrentCondition condition) {
        // set the value over
        CurrentForecastFragment.mCondition = condition;
        CurrentForecastFragment.SetConditionsToView();
    }
}