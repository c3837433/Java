package com.example.angessmith.tabbednavigation;

import java.io.Serializable;

/**
 * Created by AngeSSmith on 10/20/14.
 */
   /*
    weather: "Overcast",
    temperature_string: "65.6 F (18.7 C)",
    relative_humidity: "78%",
    wind_string: "From the SSW at 2.0 MPH",
    pressure_in: "29.98",
    dewpoint_string: "58 F (15 C)",
    feelslike_string: "65.6 F (18.7 C)",
    precip_1hr_string: "0.00 in ( 0 mm)",
    icon_url: "http://icons.wxug.com/i/c/k/cloudy.gif",
     */
public class CurrentCondition implements Serializable {

    private static final long serialVersionUID =  484763662987253462L;

    // Create the variables
    private String mIconUrl;
    private String mTemperatureString;
    private String mRelativeHumidity;
    private String mWindString;
    private String mPressure;
    private String mWeather;
    private String mFeelsLike;
    private String mPrecipitation;
    private String mDewPoint;
    public static CurrentCondition newInstance(String weather, String temp, String humidity, String wind, String pressure, String precip, String dewpoint, String feelsLike, String iconUrl) {
        // Create the new object
        CurrentCondition condition = new CurrentCondition();
        // and set the variables
        condition.mIconUrl = iconUrl;
        condition.mTemperatureString = temp;
        condition.mRelativeHumidity = humidity;
        condition.mWindString = wind;
        condition.mPressure = pressure;
        condition.mWeather = weather;
        condition.mFeelsLike = feelsLike;
        condition.mPrecipitation = precip;
        condition.mDewPoint = dewpoint;
        return condition;
    }


    public CurrentCondition() {
        mIconUrl = mTemperatureString = mRelativeHumidity = mWindString = mPressure = mWeather =  mFeelsLike = mPrecipitation = mDewPoint = "";
    }

    // Define the getters and setters
    public String getIconUrl() {
        return mIconUrl;
    }

    public String getTemperatureString() {
        return mTemperatureString;
    }


    public String getRelativeHumidity() {
        return mRelativeHumidity;
    }

    public String getWindString() {
        return mWindString;
    }


    public String getPressure() {
        return mPressure;
    }

    public String getWeather() {
        return mWeather;
    }

    public String getFeelsLike() {
        return mFeelsLike;
    }

    public String getPrecipitation() {
        return mPrecipitation;
    }

    public String getDewPoint() {
        return mDewPoint;
    }

}
