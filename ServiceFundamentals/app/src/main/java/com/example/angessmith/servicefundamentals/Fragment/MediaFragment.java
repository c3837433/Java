package com.example.angessmith.servicefundamentals.Fragment;

 // Created by AngeSSmith on 10/27/14.
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.angessmith.servicefundamentals.MediaService;
import com.example.angessmith.servicefundamentals.R;

/*
Functional Changes:

    1.  Add a seekbar to your app that, while the activity is open, shows the current playback progress
        of the currently playing song.
    2.  Add the ability to loop a single song or continue through other songs for playback.
        If the user skips forward or back with single song looping selected, the current track should
        be restarted instead of changing.

Cosmetic Changes:

    1.  Change your layout to work in both landscape and portrait orientations with each orientation
        having a distinctly different appearance but the same functionality. Application UI must use fragments.
    2.  Change your notification to show a large image of album art in an expanded notification.
 */

public class MediaFragment extends Fragment implements View.OnClickListener, ServiceConnection {
    // Define the TAG
    public static final String TAG = "MediaFragment.TAG";
    public static final String ARG_TOGGLE_CHECK = "MediaFragment.ARG_TOGGLE_CHECK";
    public static final String ARG_ARTIST_NAME = "MediaFragment.ARG_ARTIST_NAME";
    public static final String ARG_SONG_TITLE = "MediaFragment.ARG_SONG_TITLE";
    boolean mBoundToService;
    // define the service
    MediaService mMediaService;
    Context mContext;
    ToggleButton mToggle;
    TextView mArtistNameView;
    TextView mSongTitleView;

    // Create the factory
    public static MediaFragment newInstance() {
        // return an instance of the fragment
        return new MediaFragment();
    }

    // Set up the fragment view
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        // Get the media view
        View view = _inflater.inflate(R.layout.media_fragment, _container, false);
        view.findViewById(R.id.play_button).setOnClickListener(this);
        view.findViewById(R.id.pause_button).setOnClickListener(this);
        view.findViewById(R.id.stop_button).setOnClickListener(this);
        view.findViewById(R.id.rewind_button).setOnClickListener(this);
        view.findViewById(R.id.forward_button).setOnClickListener(this);
        mToggle = (ToggleButton) view.findViewById(R.id.toggleButton);
        mArtistNameView = (TextView) view.findViewById(R.id.artist_name_view);
        mSongTitleView = (TextView) view.findViewById(R.id.title_view);
        // see if we need to change the toggle
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        mToggle.setChecked(sharedPref.getBoolean(ARG_TOGGLE_CHECK, false));
        mSongTitleView.setText(sharedPref.getString(ARG_SONG_TITLE, "The Four Seasons"));
        mArtistNameView.setText(sharedPref.getString(ARG_ARTIST_NAME, "Vivaldi"));
        Log.i(TAG, "View Created");
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, " OnCreated, binding service");
        // store the context to stop leaks
        mContext = getActivity().getApplicationContext();

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "On Activity Created");
    }

    // When the fragment loads initially and when returning
    @Override
    public void onStart() {
        super.onStart();
        // Create an intent to connect the service with the activity
        Log.d(TAG, "Starting");
        Intent intent = new Intent(getActivity(), MediaService.class);
        mContext.startService(intent);
        mContext.bindService(intent, this, Context.BIND_AUTO_CREATE);
    }


    @Override
    public void onClick(View v) {
        // check which button was clicked
        switch (v.getId()) {
            case R.id.play_button:
                Log.d(TAG, "User pressed play button");
                if(mMediaService != null) {
                    mMediaService.playSong();
                    mMediaService.updateSongInformation();
                    mMediaService.showNotification();
                }
                break;
            case R.id.stop_button:
                if (mMediaService != null) {
                    Log.d(TAG, "User pressed stop button");
                    mMediaService.stopSong();
                }

                break;
            case R.id.pause_button:
                Log.d(TAG, "User pressed pause button");
                if (mMediaService != null) {
                    Log.d(TAG, "Bound, pausing song");
                    mMediaService.pauseSong();
                }
                break;
            case R.id.rewind_button:
                Log.d(TAG, "Backing tracks");
                if(mMediaService != null) {
                    Log.d(TAG, "Media service not empty");
                    // Change the position index, and update the view and notification information
                    mMediaService.skipBackSong();
                    mMediaService.updateSongInformation();
                    mMediaService.showNotification();
                }
                break;
            case R.id.forward_button:
                if(mMediaService != null) {
                    Log.d(TAG, "Skipping forward a song");
                    // Change the position index, and update the view and notification information
                    mMediaService.skipForwardSong();
                    mMediaService.updateSongInformation();
                    mMediaService.showNotification();
                }
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "Device changed orientation from fragment");
        // Reset the toggle
        mMediaService.mRepeatSong = mToggle.isChecked();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        String toggle = (mToggle.isChecked()) ? "repeat, checked" : "no repeat";
        Log.i(TAG, "Saving state of repeat toggle as " + toggle);
        // Save the current values so when the activity is relaunched we have values
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ARG_ARTIST_NAME, mArtistNameView.getText().toString());
        editor.putString(ARG_SONG_TITLE, mSongTitleView.getText().toString());
        editor.commit();
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.i(TAG, "Service Connected, getting binder");
        // Get the service binder
        MediaService.MediaServiceBinder serviceBinder = (MediaService.MediaServiceBinder) service;
        mMediaService = serviceBinder.getService();
        // set service checker
        mBoundToService = true;
        // Get the remaining views
        ImageView songImageView = (ImageView) getView().findViewById(R.id.album_image_view);
        SeekBar songSeekBar = (SeekBar) getView().findViewById(R.id.seekBar);
        TextView portSeekTime = (TextView) getView().findViewById(R.id.portSeekTime);
        TextView landSeekCurrent = (TextView) getView().findViewById(R.id.landSeekCurrent);
        TextView landSeekTotal = (TextView) getView().findViewById(R.id.landSeekTotal);

        // Set the listener for the toggle switch
        mToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // set the repeat boolean to the new value
                mMediaService.mRepeatSong = isChecked;
                // update the preference for the toggle to the new value
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(ARG_TOGGLE_CHECK, isChecked);
                editor.commit();
            }
        });
        // set the views to the service
        mMediaService.setViews(mArtistNameView, mSongTitleView, songImageView, songSeekBar, portSeekTime, landSeekCurrent, landSeekTotal);
        // if returning from background or device orientation change, update the song
        if ((mMediaService.mPlayerState == MediaService.Player_State.STARTED) || (mMediaService.mPlayerState == MediaService.Player_State.PAUSED)) {
            Log.i(TAG, "Player is running, updating song info");
            mMediaService.updateSongInformation();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.i(TAG, "Service Disconnected");
        // Reset the values
        mBoundToService = false;
        mMediaService = null;
    }

}
