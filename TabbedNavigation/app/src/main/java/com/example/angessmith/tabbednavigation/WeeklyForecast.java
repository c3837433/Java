package com.example.angessmith.tabbednavigation;

 // Created by AngeSSmith on 10/21/14.

import java.io.Serializable;

public class WeeklyForecast implements Serializable {

    private static final long serialVersionUID = 264352789266376484L;

    // Define the properties
    private String mDay;
    private String mDescription;
    private String mIconString;

    // Build the factory
    public static WeeklyForecast newInstance (String day, String description, String imageIcon) {
        // Create the object
        WeeklyForecast forecast = new WeeklyForecast();
        // and set the variables
        forecast.mDay = day;
        forecast.mDescription = description;
        forecast.mIconString = imageIcon;
        return forecast;
    }

    public WeeklyForecast () {
        mDay = mDescription = mIconString = "";
    }

    // Create the getters
    public String getDay() {
        return mDay;
    }

    public String getDescription() {
        return mDescription;
    }


    public String getIconString() {
        return mIconString;
    }

}