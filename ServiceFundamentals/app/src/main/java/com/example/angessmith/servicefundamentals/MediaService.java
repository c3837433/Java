package com.example.angessmith.servicefundamentals;

// Created by AngeSSmith on 10/27/14.

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.angessmith.servicefundamentals.Fragment.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.example.angessmith.servicefundamentals.MediaService.Player_State.*;


public class MediaService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener{

    public static final String TAG = "MediaService.TAG";
    public static final String ARG_PREFERENCES = "MediaService.PREFERENCES";
    public static final String ARG_PLAYER_STATE = "MediaService.PLAYER_STATE";
    public static final String ARG_LIST_POSITION = "MediaService.ARG_LIST_POSITION";
    private static final int STANDARD_NOTIFICATION = 0x03468;
    private static final int INTENT_NOTIFICATION = 0x1315;

    private ArrayList<Song> mSongList;
    // and the position
    private int mSongListPosition;
    MediaPlayer mMediaPlayer;

   // private boolean mIsPaused;
    public Player_State mPlayerState;
    // define the notification
    NotificationCompat.Builder mBuilder;
    NotificationManager mManager;
    TextView mArtistNameView;
    TextView mSongTitleView;
    TextView mPortSeekTime;
    TextView mLandSeekCurrent;
    TextView mLandSeekTotal;
    ImageView mAlbumImage;
    SeekBar mSeekbar;
    Handler mSeekHandler;
    public Boolean mRepeatSong = false;
    public Boolean mServiceRunning = false;


    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "Song complete, playing the next song");
        // change the position and change the state
        mPlayerState = PLAYBACK_COMPLETE;
        // increase the position
        //movePositionForward();
        // and play the next song
        playSong();
        showNotification();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "player prepared");
        mPlayerState = PREPARED;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "player error what = " + what + " and extra = " + extra);
        mPlayerState = ERROR;
        // reload the song
        playSong();
        return true;
    }

    public static enum Player_State {
         IDLE,  INITIALIZED, PREPARING, PREPARED, STARTED, PAUSED, STOPPED, END, ERROR, PLAYBACK_COMPLETE
    }

    // Create the Binder
    public class MediaServiceBinder extends Binder {
        // Add a method to access the service
        public MediaService getService() {
            // return the instance so the activity can call the methods
            return MediaService.this;
        }
    }

    // Create an object so the methods can access to the binder
    MediaServiceBinder mMediaBinder;


    @Override
    public IBinder onBind(Intent intent) {
        // return the binder object (never should be null if binding)
        //Toast.makeText(this, "Service Binded", Toast.LENGTH_SHORT).show();
        mMediaBinder = new MediaServiceBinder();
        //return new MediaServiceBinder();
        return mMediaBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mSongList == null) {
            mSongList =  new ArrayList<Song>();
            mSongList.add(Song.newInstance(R.raw.spring, "The Four Seasons: Spring", "Vivaldi"));
            mSongList.add(Song.newInstance(R.raw.summer, "The Four Seasons: Summer", "Vivaldi"));
            mSongList.add(Song.newInstance(R.raw.autumn, "The Four Seasons: Autumn", "Vivaldi"));
            mSongList.add(Song.newInstance(R.raw.winter, "The Four Seasons: Winter", "Vivaldi"));
        }
        // Create the media player
        mMediaPlayer = new MediaPlayer();

        // set the service to be running to keep track
        mServiceRunning = true;
        // Create the notification
        mBuilder = new NotificationCompat.Builder(this);
        mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        // Create the handler for the runnable
        mSeekHandler =  new Handler();
        // Reset the state value in case it is null
        if (mPlayerState == null) {
            mPlayerState = Player_State.IDLE;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // used each time the service is called
        //Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        // The service is running again
        mServiceRunning = true;
        // See if we need to update the bar
        if ((mMediaPlayer.isPlaying()) || (mPlayerState == Player_State.STARTED)) {
            // set the seek bar every second
            mSeekHandler.postDelayed(updateSeekBar, 100);
        }
        // START_STICKY tells the system to recreate as it is already started
        // START_NOT_STICKY tells service if killed there are no more to call
        return Service.START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
        mServiceRunning = false;
        SharedPreferences preferences = getSharedPreferences(ARG_PREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        // save the current state
        editor.putString(ARG_PLAYER_STATE, String.valueOf(mPlayerState));
        editor.putInt(ARG_LIST_POSITION, mSongListPosition);
        // Commit the save
        editor.commit();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //Toast.makeText(this, "Service Unbound", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }


    public void setViews(TextView artistNameView, TextView songTitleView, ImageView albumImageView, SeekBar seekbar, TextView portSeekTime, TextView landSeekCurrent, TextView landSeekTotal) {
        // set their text
        mArtistNameView = artistNameView;
        mSongTitleView = songTitleView;
        mAlbumImage = albumImageView;
        mSeekbar = seekbar;
        mPortSeekTime = portSeekTime;
        mLandSeekCurrent = landSeekCurrent;
        mLandSeekTotal = landSeekTotal;
    }

    public void updateSongInformation() {
        Log.d(TAG, "Updating song information");
        // Make sure the player is playing, then set the current songs info
        if ((mMediaPlayer.isPlaying()) || (mPlayerState == STARTED) || (mPlayerState == PAUSED)) {
            Log.d(TAG, "Player is playing " + mSongList.get(mSongListPosition).getSongArtist() + ": " + mSongList.get(mSongListPosition).getSongTitle());
            mArtistNameView.setText(mSongList.get(mSongListPosition).getSongArtist());
            mSongTitleView.setText(mSongList.get(mSongListPosition).getSongTitle());
            // Use the media retriever for the album image
            Bitmap bitmap = getAlbumArtwork();
            if (bitmap != null) {
                // set it in the image view
                mAlbumImage.setImageBitmap(bitmap);
            }
            // and update the seek bar
            mSeekHandler.postDelayed(updateSeekBar, 100);
        }
    }

    public void showNotification() {
        // Create an intent so we can return to the activity when user selects the notification
        Intent mIntent = new Intent(this, MainActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        PendingIntent pIntent = PendingIntent.getActivity(this, INTENT_NOTIFICATION, mIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_media_play);

        Bitmap bitmap = getAlbumArtwork();
        builder.setContentTitle(mSongList.get(mSongListPosition).getSongTitle());
        builder.setContentText(mSongList.get(mSongListPosition).getSongArtist());
        int number = mSongListPosition + 1;
        String trackNumber =  "Vivaldi: " + number;
        builder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .setSummaryText(trackNumber));
        builder.setContentIntent(pIntent);
        // start the intent and keep it in the foreground
        startService(mIntent);
        startForeground(STANDARD_NOTIFICATION, builder.build());
        //mManager.notify(STANDARD_NOTIFICATION, builder.build());

    }

    private Bitmap getAlbumArtwork() {
        // Get the image from the album
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        // find this id
        long resourceId = mSongList.get(mSongListPosition).getSongId();
        // Use the uri to get the data for the right one
        retriever.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + resourceId));
        // get the the data
        byte [] imageData = retriever.getEmbeddedPicture();
        // if null, no image is found
        if (imageData != null) {
            // Create and return the  bitmap of the data
            return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        }
        return null;
    }

    public void clearNotification() {
        Log.d(TAG, "Clearing notifications");
        // Stop the intent
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
        // remove the notification from the foreground
        stopForeground(true);
    }


    public void playSong() {
        Log.i(TAG, "Player state from playSong = " + mPlayerState);
        switch (mPlayerState) {
            // Make sure we are not paused
            case PAUSED:
                Log.d(TAG, "Player resuming playSong from pause");
                mMediaPlayer.start();
                // Set the default values
                mSeekbar.setMax(mMediaPlayer.getDuration());
                mSeekbar.setProgress(mMediaPlayer.getCurrentPosition());
                showNotification();
                mPlayerState = STARTED;
                // set the seek bar
                mSeekHandler.postDelayed(updateSeekBar, 100);
                break;
            case END:
                Log.d(TAG, "State = end");
                break;
            // if we are playing, restart
            case STARTED:
                Log.d(TAG, "Restarting song");
                mMediaPlayer.reset();
                mPlayerState = IDLE;
                LoadSong();
                break;
            // if we are stopped, we just need to prepare again
            case STOPPED:
                Log.d(TAG, "Player playSong from stop");
                mMediaPlayer.reset();
                mPlayerState = IDLE;
                LoadSong();
                break;
            case ERROR:
                Log.d(TAG, "Player reloading from error");
                mMediaPlayer.reset();
                mPlayerState = IDLE;
                LoadSong();
                break;
            // When song is completed, move position, reset and play next song
            case PLAYBACK_COMPLETE:
                if (!mRepeatSong) {
                    movePositionForward();
                }
                mMediaPlayer.reset();
                mPlayerState = IDLE;
                LoadSong();
               break;
            case IDLE:
                Log.d(TAG, "Player playSong from idle");
                // reset it just in case
                mMediaPlayer.reset();
                LoadSong();
            default:
                Log.d(TAG, "Player in unknown state");
                break;
        }
    }


    private void LoadSong() {
        // get the song at the current position of the list
        long resourceId = mSongList.get(mSongListPosition).getSongId();
        //Log.i(TAG, "ResourceId = " + resourceId);
        try {
            // Load media from URI
            mMediaPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + resourceId));
            // Player is initialized after data source set
            mPlayerState = INITIALIZED;
            // Prepare the player
            mMediaPlayer.prepareAsync();
            //state is preparing
            mPlayerState = PREPARING;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Once prepared, play a song
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // start the player
                mPlayerState = PREPARED;
                mp.start();
                mPlayerState = STARTED;
                // set the seek bar
                mSeekHandler.postDelayed(updateSeekBar, 100);
                // Update the notification data and view data
                showNotification();
                updateSongInformation();
                //mSeekHandler.postDelayed(updateSeekBar, 100);
                Log.d(TAG, "Player starting");
            }
        });
    }

    // Stop song
    public void stopSong() {
        Log.i(TAG, "Player state from stop = " + mPlayerState);
        // Make sure the player is actually playing
        if (mMediaPlayer.isPlaying()) {
            Log.i(TAG, "Player says it is playing");
            // Stop the player and change the state
            mMediaPlayer.stop();
            mPlayerState = STOPPED;
            clearNotification();
        }
    }

    // Pause player
    public void pauseSong() {
        // Make sure the player is still playing
        if (mMediaPlayer.isPlaying()) {
            // Pause the song and change the state
            mMediaPlayer.pause();
            mPlayerState = PAUSED;
            // stop the notification
            clearNotification();
        }
    }

    // Move the position of the index
    public void skipBackSong () {
        Log.i(TAG, "List has " + mSongList.size() + "items. Currently playing number " + mSongListPosition);
        // Reset the player
        mMediaPlayer.reset();
        mPlayerState = IDLE;
        if (!mRepeatSong) {
            // if we are at the first song, go back to the last one otherwise just go back one
            mSongListPosition = mSongListPosition == 0 ? mSongList.size() - 1 : mSongListPosition - 1;
            Log.i(TAG, "Not on repeat, Position moved back to: " + mSongListPosition);
        }
        // Play the current song
        playSong();

    }

    // same as go back but opposite
    public void skipForwardSong () {
        Log.i(TAG, "List has " + mSongList.size() + "items. Currently playing number " + mSongListPosition);
        mMediaPlayer.reset();
        mPlayerState = IDLE;
        if (!mRepeatSong) {
            Log.i(TAG, "Not on repeat, Position moved forward to: " + mSongListPosition);
            movePositionForward();
        }
        //movePositionForward();
        Log.i(TAG, "Playing Song");
        // play the new song
        playSong();
    }

    // Change the position as needed
    private void movePositionForward() {
        /// If we have reached the end, go to the beginning otherwise move ahead 1
        mSongListPosition = (mSongListPosition == mSongList.size() - 1) ? mSongListPosition = 0 : mSongListPosition + 1;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "Device changed orientation from service");
        super.onConfigurationChanged(newConfig);
        // cancel the runnable to stop errors, will be re run when service and views are ready
        mSeekHandler.removeCallbacks(updateSeekBar);
    }

    // Create a runnable method that will keep update the bar while it is playing
    public Runnable updateSeekBar = new Runnable() {
        public void run() {
            // make sure we are still playing
            if(mMediaPlayer.isPlaying()) {
                // set the bar values
                mSeekbar.setMax(mMediaPlayer.getDuration());
                mSeekbar.setProgress(mMediaPlayer.getCurrentPosition());
                // continue running this every second
                //Log.d(TAG, "Current position = " + mMediaPlayer.getCurrentPosition());
                mSeekHandler.postDelayed(this, 100);
                // Create a string for minutes and seconds from the number of milliseconds remaining of total song length and current position
                String totalMinutes = String.format("%d:", TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer.getDuration()));
                String totalSeconds = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer.getDuration()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer.getDuration())));
                String currentMinutes = String.format("%d:", TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer.getCurrentPosition()));
                String currentSeconds = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer.getCurrentPosition()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer.getCurrentPosition())));
                // Check the orientation for the fragment's different layouts
                int orientation = getResources().getConfiguration().orientation;
                 switch (orientation) {
                     case 1:
                         // We are in portrait, make sure the view is ready (or it may crash)
                         //Log.d(TAG, "We are in portrait mode");
                         if (mPortSeekTime != null) {
                             mPortSeekTime.setText(currentMinutes + currentSeconds + "/" + totalMinutes + totalSeconds);
                         }
                         break;
                     case 2:
                         //Log.d(TAG, "We are in landscape mode");
                         // Make sure the text view is ready, otherwise we will hit null exemption
                         if (mLandSeekTotal != null) {
                             mLandSeekTotal.setText(totalMinutes + totalSeconds);
                             mLandSeekCurrent.setText(currentMinutes + currentSeconds);
                         }
                         break;
                     default:
                         Log.d(TAG, "Not sure what mode we are in...");
                         break;
                 }
            }
        }
    };
}
