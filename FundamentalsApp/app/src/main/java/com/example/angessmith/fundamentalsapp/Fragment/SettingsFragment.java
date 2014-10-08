package com.example.angessmith.fundamentalsapp.Fragment;

// Created by AngeSSmith on 10/7/14.

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.example.angessmith.fundamentalsapp.R;

import java.io.File;

public class SettingsFragment extends PreferenceFragment {

    final String TAG = "SettingsFragment";
    // Override the on create to load in the preference fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load in the preference fragment
        addPreferencesFromResource(R.xml.settings);
    }

    // make the fragment listen for the user to click on the options
    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        // Get the list preference
        final ListPreference listPreference = (ListPreference) findPreference("PREF_NETWORK_LIST");
        // set a listener so if the value changes the preference gets updated
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //Log.i(TAG, "User changed the preference");
                //Log.i(TAG, "new value = " + newValue);
                // Update the preference with the new value
                listPreference.setValue((String) newValue);
                return false;
            }
        });

        // find the click for deleting the cache
        Preference clearCacheButton = findPreference("PREF_CACHE");
        clearCacheButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // Empty the cache
                Log.i(TAG, "User selected clear the cache");
                File external = getActivity().getExternalFilesDir(null);
                if (external.listFiles() != null) {
                    for (File file : external.listFiles()) {
                        Log.i(TAG, "Deleting file:" + file);
                        file.delete();
                    }
                    // Alert the user that all files have been deleted
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setMessage("Cache Cleared");
                    dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        // Listen for the user to click the button
                        public void onClick(DialogInterface dialog, int integer) {
                            // nothing to do
                        }
                    });
                    // Show the user
                    dialog.show();
                }

                return true;
            }
        });
    }


    public void onResume() {
        super.onResume();
        // Set the settings background view to white
        getView().setBackgroundColor(Color.WHITE);
    }


}
