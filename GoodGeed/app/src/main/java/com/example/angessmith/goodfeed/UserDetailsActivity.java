package com.example.angessmith.goodfeed;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.angessmith.goodfeed.Fragment.EditUserProfileFragment;
import com.example.angessmith.goodfeed.Fragment.UserProfileFragment;
import com.example.angessmith.goodfeed.Fragment.UserStoriesFragment;
import com.example.angessmith.goodfeed.Listeners.OnEditProfileListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.List;

// Created by Angela Smith for ADP2 Term 1412 December 11, 2014
// User Details activity launched when the user selects a post's author image or name

public class UserDetailsActivity extends Activity implements EditUserProfileFragment.OnEditPictureButton, OnEditProfileListener {

    public static final String TAG = "UserDetailsActivity";
    public static final String PROFILEID = "UserDetailsActivity.PROFILEID";
    StoryAuthor mStoryAuthor;
    private HelperMethods mHelper = new HelperMethods();
    Uri mCreateNewUri =  null;
    public static final int REQUEST_CAMERA_FOR_NEW_PROFILE = 9991114;
    private ImageView mProfileImageView;
    private ImageView mCoverView;
    private Bitmap mNewThumbnail;
    private MenuItem mEditItem;
    private MenuItem mCancelItem;
    private MenuItem mSaveItem;
    EditUserProfileFragment mEditProfileFragment;
    private ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userId = getIntent().getStringExtra(PROFILEID);
        Log.d(TAG, "Story author id = " + userId);
        setContentView(R.layout.activity_user_details);
        // Make a query for all stories for this user
        getUserValues(userId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_details, menu);
        // set up the items
        mEditItem = menu.findItem(R.id.action_edit);
        mCancelItem = menu.findItem(R.id.action_cancel_edit);
        mSaveItem = menu.findItem(R.id.action_save_edit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // See which button was selected, then call that method
        switch (item.getItemId()) {
            case R.id.action_edit:
                changeToEditUserView();
                break;
            case R.id.action_save_edit:
                saveUsesEditedProfile();
                break;
            case R.id.action_cancel_edit:
                returnToMainProfileView(mStoryAuthor);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getUserValues(String id) {
        Log.d(TAG, "The passed id = " + id);

        // Make the first query for the selected user
        ParseQuery userQuery = new ParseQuery(StoryAuthor.class);
        userQuery.whereEqualTo("objectId", id);
        // Then the second one getting only stories of that user
        ParseQuery postQuery = new ParseQuery(StoryPost.class);
        // include that user's information so we have access to their name and image
        postQuery.whereMatchesQuery("author", userQuery);
        // get the author values
        postQuery.include("author");
        // and display most recent first
        postQuery.addDescendingOrder("createdAt");
        // get the list
        postQuery.findInBackground(new FindCallback<StoryPost>() {
            public void done(List<StoryPost> postList, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Retrieved " + postList.size() + " stories");
                    // pass the list to the list fragment, and the user name and image to the profile fragment
                    setUpFragments(postList);
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });

    }

    public void setUpFragments(List<StoryPost> postList) {
        // see if this is the current user
        String postId = postList.get(0).getStoryAuthor().getAuthorId();
        String currentId = ParseUser.getCurrentUser().getObjectId();
        // if it is, switch to the edit fragment so they can change their profile name or picture
        if (postId.equals(currentId)) {
            Log.d(TAG, "This is the current user");
            // allow user to edit their profile
            mEditItem.setVisible(true);
            mEditItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            mCancelItem.setVisible(false);
            mSaveItem.setVisible(false);
            // the author is the current user
            mStoryAuthor = (StoryAuthor) ParseUser.getCurrentUser();
        }
        // If not, hide all the menu buttons
        else {
            Log.d(TAG, "This is NOT the current user");
            // hide all the edit and save edit buttons
            mEditItem.setVisible(false);
            mSaveItem.setVisible(false);
            mCancelItem.setVisible(false);
            // Get the user values for the selected user
            mStoryAuthor = postList.get(0).getStoryAuthor();
        }


        // set up the user profile fragment
        setUpProfileFragment();
        // and the list fragment
        UserStoriesFragment storiesFragment = UserStoriesFragment.newInstance(postList);
        getFragmentManager().beginTransaction().replace(R.id.user_list_container, storiesFragment, UserStoriesFragment.TAG).commit();

    }

    private void setUpProfileFragment() {
        Log.d(TAG, "Author info: " + mStoryAuthor);
        String authorName = mStoryAuthor.getUserName();
        String facebookId = mStoryAuthor.getFacebookId();
        Log.d(TAG, "Name = " + authorName + " and facebookId = " + facebookId);
        // set up the profile and list fragments
        UserProfileFragment profileFragment = UserProfileFragment.newInstance(mStoryAuthor);
        getFragmentManager().beginTransaction().replace(R.id.user_profile_container, profileFragment, UserProfileFragment.TAG).commit();
    }


    // When the user needs to view the edit fragment
    public void changeToEditUserView() {
        Log.d(TAG, "This is the current user, changing fragments");
        mEditProfileFragment = EditUserProfileFragment.newInstance(mStoryAuthor);
        // set the OnEditProfileListener
        mEditProfileFragment.setListener(this);
        // switch fragments
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.user_profile_container, mEditProfileFragment, EditUserProfileFragment.TAG);
        ft.commit();
        // Change the action bar buttons
        mEditItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        mEditItem.setVisible(false);
        mSaveItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mSaveItem.setVisible(true);
        mCancelItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mCancelItem.setVisible(true);
    }

    public void saveUsesEditedProfile() {
        Log.i(TAG, "User needs values");
        // call the fragment method to get the value in the edit text field
        mEditProfileFragment.getUserName();
    }

    public void returnToMainProfileView(StoryAuthor author) {
        Log.d(TAG, "User wants to changes the changes and return to the main fragment");
        UserProfileFragment userProfileFragment = UserProfileFragment.newInstance(author);
        // Switch back
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.user_profile_container, userProfileFragment, UserProfileFragment.TAG);
        ft.commit();
        // Change the action bar buttons
        mEditItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mEditItem.setVisible(true);
        mSaveItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        mSaveItem.setVisible(false);
        mCancelItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        mCancelItem.setVisible(false);
    }

    @Override
    public void AddNewProfileImage(ImageView coverView, ImageView profileView) {
        mCoverView = coverView;
        mProfileImageView = profileView;
        Log.i(TAG, "User updating profile image");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Get the uri
        mCreateNewUri = mHelper.createImageUri();
        mProfileImageView = profileView;
        if(mCreateNewUri != null) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCreateNewUri);
            // start request
            startActivityForResult(cameraIntent, REQUEST_CAMERA_FOR_NEW_PROFILE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure we returned without canceling
        if (resultCode == RESULT_OK & requestCode == REQUEST_CAMERA_FOR_NEW_PROFILE) {
            Log.d(TAG, "Returned with image");
            Resources resources = getResources();
            Drawable drawableCover = resources.getDrawable(R.drawable.profile_pic_cover);
            if(data == null) {
                mNewThumbnail = mHelper.createThumbnailOfImage(mCreateNewUri);
                // set in the image view
                mProfileImageView.setImageBitmap(mNewThumbnail);
                mCoverView.setImageDrawable(drawableCover);
            } else {
                Bitmap thumbnail = data.getParcelableExtra("data");
                // get the data from the intent
                mProfileImageView.setImageBitmap(thumbnail);
                mCoverView.setImageDrawable(drawableCover);
            }
        }
    }

    @Override
    public void checkValuesAndUpdateProfile(final String userName) {
        // User is required to have a name
        if (!userName.equals("")) {
            // see if we have an image
            if (mNewThumbnail != null) {
                startSpinner("Setting new profile picture..");
                // create the thumbnail of the image from the bitmap stream
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mNewThumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] data = stream.toByteArray();
                try
                {
                    // Create the new parse file
                    final ParseFile profileFile = new ParseFile ("userProfile.png", data);
                    // save it to the user
                    profileFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                stopSpinner();
                                // add the profile picture
                                ParseUser.getCurrentUser().put("ProfilePicture", profileFile);
                                // update the name too
                                updateUserWithNewInformation(userName);
                            }
                        }
                    });
                }
                catch (Exception e)
                {
                    toastUser("Sorry, that image is too big, please try again.");
                }
            } else {
                // just save the name
                updateUserWithNewInformation(userName);
            }
        } else {
            // Alert the user we need a name
            toastUser("Update requires a name");
        }
    }

    private void updateUserWithNewInformation(String userName) {
        startSpinner("Finishing update..");
        final StoryAuthor author = (StoryAuthor) ParseUser.getCurrentUser();
        author.put("UserFullName", userName);
        author.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                stopSpinner();
                // switch the fragments and load the new image
                returnToMainProfileView(author);
                toastUser("Profile Updated");
            }
        });
    }

    public void toastUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Spinner methods when the user is uploading data or images to parse
    public void startSpinner(String message) {
        mProgressBar = new ProgressDialog(this);
        mProgressBar.setMessage(message);
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar.setCancelable(false);
        mProgressBar.show();
    }

    public void stopSpinner() {
        mProgressBar.dismiss();
        mProgressBar = null;
    }

}
