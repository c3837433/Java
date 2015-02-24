package com.example.angessmith.mediaquotes;

// Created by Angela Smith on 11/20/2014
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class QuoteDaydreamSettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new DreamPreferenceFragment()).commit();
    }

    public static class DreamPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // set up the list preference
            addPreferencesFromResource(R.xml.quote_daydream_prefs);
        }

        @Override
        public void onActivityCreated(Bundle _savedInstanceState) {
            super.onActivityCreated(_savedInstanceState);
            // Get the list preference
            final ListPreference mPreference = (ListPreference) findPreference("QUOTE_TIME_FREQUENCY");
            mPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    // When the user changes the option
                    mPreference.setValue((String) newValue);
                    // and update the preference
                    SharedPreferences settingsPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settingsPreferences.edit();
                    editor.putString("QUOTE_TIME_FREQUENCY",(String)newValue);
                    // Save the changes
                    editor.commit();
                    return false;
                }
            });
        }
    }
}
