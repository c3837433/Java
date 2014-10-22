package com.example.angessmith.tabbednavigation;


 // Created by AngeSSmith on 10/20/14.

import java.io.Serializable;

public class HourlyForecast implements Serializable {

    private static final long serialVersionUID = 346813153740L;

    private String mTemperatureString;
    private String mCondition;
    private String mTimeHour;
    private String mFeelsLike;


    public static HourlyForecast newInstance (String temp, String condition, String time, String feelsLike) {
        // Create the object
        HourlyForecast forecast = new HourlyForecast();
        // and set the variables
        forecast.mTemperatureString = temp;
        forecast.mCondition = condition;
        forecast.mTimeHour = time;
        forecast.mFeelsLike = feelsLike;
        return forecast;
    }

    public HourlyForecast() {
        mCondition = mFeelsLike = mTemperatureString = mFeelsLike = "";
    }

    // Create the getters and setters
    public String getTemperatureString() {
        return mTemperatureString;
    }

    public String getCondition() {
        return mCondition;
    }


    public String getTimeHour() {
        return mTimeHour;
    }


    public String getFeelsLike() {
        return mFeelsLike;
    }
}
