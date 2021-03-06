package com.example.angessmith.tabbednavigation.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angessmith.tabbednavigation.CurrentCondition;
import com.example.angessmith.tabbednavigation.R;
import com.loopj.android.image.SmartImageView;

// Created by AngeSSmith on 10/20/14.
// http://api.wunderground.com/api/3d402f1818f340e0/conditions/q/CA/San_Francisco.json
public class CurrentForecastFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CURRENT_CONDITION = "conditionalForecast";

    public static CurrentCondition mCondition;
    // Get the views ready
    private static TextView mTempView;
    private static TextView mPressureView;
    private static TextView mPrecipView;
    private static TextView mHumidityView;
    private static TextView mDewPointView;
    private static TextView mWindView;
    private static TextView mFeelsView;
    private static TextView mConditionView;
    private static SmartImageView mIconView;
    static  Context mContext;

    public static CurrentForecastFragment newInstance(int sectionNumber, Context context) {
        mContext = context;
        CurrentForecastFragment fragment = new CurrentForecastFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public CurrentForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // get the current view
        return inflater.inflate(R.layout.current_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);
        // Get the views
        mConditionView = (TextView) getView().findViewById(R.id.condition_weather);
        mTempView = (TextView) getView().findViewById(R.id.condition_temp);
        mPressureView = (TextView) getView().findViewById(R.id.condition_pressure);
        mPrecipView = (TextView) getView().findViewById(R.id.condition_precip);
        mHumidityView = (TextView) getView().findViewById(R.id.condition_humidity);
        mDewPointView = (TextView) getView().findViewById(R.id.condition_dew);
        mWindView = (TextView) getView().findViewById(R.id.condition_wind);
        mFeelsView = (TextView) getView().findViewById(R.id.condition_feels);
        mIconView = (SmartImageView) getView().findViewById(R.id.condition_image);
        // See if we have saved values to use
        Bundle bundleArguments = getArguments();
        // IF they are there
        if ((bundleArguments != null)  && (bundleArguments.getSerializable(ARG_CURRENT_CONDITION) != null)) {
            mCondition = (CurrentCondition) getArguments().getSerializable(ARG_CURRENT_CONDITION);
            Log.i("CURRENT FORECAST", "Saved intance state found condition, reloading data");
            SetConditionsToView();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the condition
        getArguments().putSerializable(ARG_CURRENT_CONDITION, (java.io.Serializable) mCondition);
    }


    public static void SetConditionsToView() {
        // make sure we have valid data
        if (mCondition != null) {
            // update the view with data
            //Log.i("MainActivity", "The object = " + mCondition);
            mConditionView.setText("Current Conditions: " + mCondition.getWeather());
            mTempView.setText(mCondition.getTemperatureString());
            mPrecipView.setText("Precipitation: " + mCondition.getPrecipitation());
            mPressureView.setText("Pressure: " + mCondition.getPressure());
            mHumidityView.setText("Humidity: " + mCondition.getRelativeHumidity());
            mDewPointView.setText("Dew Point: " + mCondition.getDewPoint());
            mWindView.setText("Wind: " + mCondition.getWindString());
            mFeelsView.setText("Feels Like: " + mCondition.getFeelsLike());
            mIconView.setImageUrl(mCondition.getIconUrl());

        }
        else {
            Toast.makeText(mContext,"Unable to retrieve data right now", Toast.LENGTH_LONG ).show();
        }
    }
}
