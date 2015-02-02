package com.example.angessmith.navdrawerapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;

import com.example.angessmith.navdrawerapp.Main;
import com.example.angessmith.navdrawerapp.R;

// Created by AngeSSmith on 10/20/14.

public class SettingsFragment extends PreferenceFragment {
    // Create the value for the title
    private static final String ARG_POSITION = "SettingsFragment.POSITION";

    // Create the settings fragment
    public static SettingsFragment newInstance(int section) {
        SettingsFragment fragment = new SettingsFragment();
        // Save the title
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, section);
        fragment.setArguments(args);
        return fragment;
    }

    // Set the title from the passed in section
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Set the page title
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_POSITION));
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

        // Get the switch preference
        SwitchPreference preference = (SwitchPreference) findPreference("MOBILE_DATA_PREFERENCE");
        preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            // Listen for the switch to change
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // Update the preference with the new value
                preference.setDefaultValue(newValue);
                // see what the value =
                Log.i("Settings Fragment","The new preference value = " + newValue);
                // Get the preference
                SharedPreferences settingsPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settingsPreferences.edit();
                // Change the boolean to a string and save it to the preferences
                editor.putString("MOBILE_DATA_PREFERENCE", newValue.toString());
                editor.commit();
                // Allow it to make additional changes
                return true;
            }
        });
    }

}
