package com.example.angessmith.goodfeed;

// Created by Angela Smith for ADP2 Term 1412 December 2, 2014

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.angessmith.goodfeed.Fragment.FlagDialogFragment;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class MainFeedActivity extends Activity implements FlagDialogFragment.FlagReasonListener {

    public static final String TAG = "MainFeedActivity";
    public static final int LAUNCH_POST_STORY = 10024791;
    CustomStoryAdapter mCustomStoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        // if we still have a valid connection, load the objects
        checkConnectionAndLoadObjects();
    }

    // Make sure we are connected to the internet, then set up fragments and adapter
    private void checkConnectionAndLoadObjects() {
        // Make sure we are still have a network connection
        ConnectionChecker connection = new ConnectionChecker(this);
        boolean connected = connection.canConnectInternet();
        if (connected) {
            // get the list adapter ready
            mCustomStoryAdapter = new CustomStoryAdapter(this,new OnItemButtonClickListener() {
                @Override
                public void FlagStory(StoryPost post) {
                    // get the reason why from the dialog
                    FlagDialogFragment fragment = FlagDialogFragment.newInstance(post);
                    fragment.show(getFragmentManager(), FlagDialogFragment.TAG);
                }

                @Override
                public void ViewUserDetails(final StoryAuthor author) {
                    Log.d(TAG, "View user: " + author.getAuthorId());
                    // Just open the view and query the user
                    viewUserDetailsView(author.getAuthorId());

                }

                @Override
                public void ShareStory(StoryPost post) {
                    // Get the Author, title, and message
                    String shareMessage = post.getStoryAuthor().getUserName() + " shared " + post.getTitle() + "Story: " + post.getStoryText();
                    // Start share intent
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    // add the message
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    shareIntent.setType("text/plain");
                    // Start the activity
                    startActivity(Intent.createChooser(shareIntent, "Share Story"));
                }
            });

            // set up the list view and load the initial items
            ListView mPostList = (ListView) findViewById(R.id.main_feed_list);
            mPostList.setAdapter(mCustomStoryAdapter);
            mCustomStoryAdapter.loadObjects();
        } else {
            Toast.makeText(this, "Unable to connect, please check your network settings.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_share_story:
                // User selected share a story
                Log.i(TAG, "User wants to share a story");
                Intent postStoryIntent = new Intent(this, SharePostActivity.class);
                startActivityForResult(postStoryIntent, LAUNCH_POST_STORY);
                break;
            case R.id.action_log_out:
                // User selected Log out
                Log.i(TAG, "User wants to log out");
                // First see if this user is connected through facebook
                if (ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {
                    Log.d(TAG, "User is connected to facebook and is logging out");
                    // log out of facebook active session
                    com.facebook.Session fbs = com.facebook.Session.getActiveSession();
                    if (fbs == null) {
                        Log.d(TAG, "Getting the active session");
                        // get the active session
                        fbs = new com.facebook.Session(this);
                        com.facebook.Session.setActiveSession(fbs);
                    }
                    Log.d(TAG, "remove information");
                    // remove information
                    fbs.closeAndClearTokenInformation();
                }
                // Finally, log out of parse
                ParseUser.logOut();

                // Move back to the launch screen
                Intent logoutIntent = new Intent();
                setResult(RESULT_OK, logoutIntent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "User returned from post story");
        // prepare for when user returns to update the list
        if ((requestCode == LAUNCH_POST_STORY) && (resultCode == RESULT_OK)) {
            Log.i(TAG, "Story posted");
            Toast.makeText(this, "Thank you for sharing!", Toast.LENGTH_SHORT).show();
            // update the listview
            mCustomStoryAdapter.loadObjects();
        }
    }

    @Override
    public void onFlagReasonClick(int reason, StoryPost post) {
        switch (reason) {
            case 0:
                // Spam
                Log.d(TAG, "User selected index 0");
                flagPost(post, "Spam");
                break;
            case 1:
                // Inappropriate
                Log.d(TAG, "User selected index 1");
                flagPost(post, "Inappropriate");
                break;
            case 2:
                // user cancel
                Log.d(TAG, "User selected index 2");
                break;
            default:
                Log.d(TAG, "Default hit");
                break;

        }

    }

    public void flagPost(StoryPost post, final String reason) {
        // Create a new flag
        final Flag flag = new Flag();
        // Set this use as the author
        flag.setAuthor(ParseUser.getCurrentUser());
        // set the selected post and reason
        flag.setStory(post);
        flag.setReason(reason);
        // Save this flag
        flag.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Flag set
                    toastUser("Post was flagged as " + reason);
                } else {
                    // problem saving
                    toastUser("Unable to flag this pose as " + reason + " right now. Pleas try again later.");
                }
            }
        });
    }


    // Login Fragment listener that calls button methods
    public interface OnItemButtonClickListener {
        // Prepare for the events
        public void FlagStory(StoryPost post);
        public void ViewUserDetails(StoryAuthor author);
        public void ShareStory(StoryPost post);
    }

    public void toastUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    // When user presses a user's name or image within the main list posts
    public void viewUserDetailsView(String userId) {
        Intent intent = new Intent(this, UserDetailsActivity.class);
        intent.putExtra(UserDetailsActivity.PROFILEID, userId);
        startActivity(intent);
    }

}
