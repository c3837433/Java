package com.example.angessmith.tabbednavigation.Fragment;


import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import android.preference.PreferenceManager;
import android.util.Log;

import com.example.angessmith.tabbednavigation.R;
// Created by AngeSSmith on 10/20/14.

public class SettingsFragment extends PreferenceFragment {

    private String mCity;
    private String mState;

    // Create the settings fragment
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load in the preference fragment
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);
        // get the current preferences
        SharedPreferences storedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Get the edit text views and add the listeners to them so we can get the new values from the user
        final EditTextPreference cityPreference =  (EditTextPreference) findPreference("EDIT_CITY_PREFERENCE");
        final EditTextPreference statePreference =  (EditTextPreference) findPreference("EDIT_STATE_PREFERENCE");
        // Get the current stored values
        mCity = storedPreferences.getString("EDIT_CITY_PREFERENCE", "Chaska");
        mState = storedPreferences.getString("EDIT_STATE_PREFERENCE", "Chaska");
        // set the summary on the settings view to the shared preference
        cityPreference.setSummary(mCity);
        statePreference.setSummary(mState);

        // Set the listeners
        cityPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mCity = newValue.toString();
                // Set the default
                preference.setDefaultValue(newValue);
                // Get the preference
                SharedPreferences settingsPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settingsPreferences.edit();
                // save the city to the preferences
                editor.putString("LOCATION_CITY_PREFERENCE", mCity);
                // and update the summary
                cityPreference.setSummary(mCity);
                // and  reset the action bar with the new search location
                ActionBar actionBar = getActivity().getActionBar();
                actionBar.setTitle(mCity + ", " + mState);
                editor.commit();
                return true;
            }
        });

        statePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setDefaultValue(newValue);
                // see what the value =
                Log.i("Settings Fragment", "The new preference value = " + newValue);
                // Get the preference
                SharedPreferences settingsPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settingsPreferences.edit();
                // save the state to the preferences
                editor.putString("LOCATION_STATE_PREFERENCE", mState);
                // update the state too
                statePreference.setSummary(mState);
                ActionBar actionBar = getActivity().getActionBar();
                actionBar.setTitle(mCity + ", " + mState);
                editor.commit();
                return true;
            }
        });
    }

    // Change the background so it is not transparent
    public void onResume() {
        super.onResume();
        getView().setBackgroundColor(Color.WHITE);
    }

}
