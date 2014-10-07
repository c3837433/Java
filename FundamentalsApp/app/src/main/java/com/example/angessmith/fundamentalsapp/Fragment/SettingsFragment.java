package com.example.angessmith.fundamentalsapp.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.example.angessmith.fundamentalsapp.R;

// Created by AngeSSmith on 10/7/14.

public class SettingsFragment extends PreferenceFragment {

    final String TAG = "SettingsFragment";
    // Override the oncreate to load in the preference fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load in the preference fragment
        addPreferencesFromResource(R.xml.settings);
    }

    // make the fragment do something
    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        // find the preference network list
        Preference prefNetworkList = findPreference("PREF_NETWORK_LIST");
        // listen for the click
        prefNetworkList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // Listen for which object was changed
                Log.i(TAG, "User changed selected item in preference list");
                Log.i(TAG, "New Value = " + newValue);
                return true;
            }
        });

        // find the click for deleting the cache
        Preference clearCacheButton = findPreference("PREF_CACHE");
        clearCacheButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // Empty the cache
                Log.i(TAG, "User selected clear the cache");
                return true;
            }
        });
    }

    public void onResume() {
        super.onResume();
        getView().setBackgroundColor(Color.WHITE);
    }
}
