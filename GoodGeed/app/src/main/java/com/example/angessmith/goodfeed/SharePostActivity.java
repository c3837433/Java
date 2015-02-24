package com.example.angessmith.goodfeed;

// Created by Angela Smith for ADP2 1412 on December 4, 2014
// Share post activity launched when the user presses the add button within the main feed action bar
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.angessmith.goodfeed.Fragment.PostCancelFragment;
import com.example.angessmith.goodfeed.Fragment.PostMediaFragment;
import com.example.angessmith.goodfeed.Fragment.PostTextFragment;
import com.example.angessmith.goodfeed.Listeners.OnShareListener;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;



public class SharePostActivity extends Activity implements OnShareListener, PostMediaFragment.OnMediaButtonClickListener, PostCancelFragment.OnCancelButtonClickListener {

    public static final String TAG = "SharePostActivity";
    PostTextFragment mTextFragment;
    public static final int REQUEST_TAKE_PICTURE = 34681315;
    public static final int REQUEST_ACCESS_IMAGE_GALLERY = 5200611;
    Uri mImageUri =  null;
    private HelperMethods mHelper = new HelperMethods();
    private Bitmap mStoryThumbNail;
    private ProgressDialog mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_post);

        //set up text and media fragment
        mTextFragment = PostTextFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.post_text_container, mTextFragment, PostTextFragment.TAG).commit();
        mTextFragment.setListener(this);

        // check the state
        if (savedInstanceState == null) {
            PostMediaFragment mediaFragment = PostMediaFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.post_media_container, mediaFragment, PostMediaFragment.TAG).commit();
        }
        // Make sure user has verified app expectation
        checkUserSharePermission();
    }

    private void checkUserSharePermission() {
        // Make sure user has given permission
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String permission = preferences.getString("com.example.angessmith.goodfeed.PERMISSION","NO");
        if (permission.equals("NO")) {
            // Alert User requesting permission
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Good Feed Expectations");
            builder.setMessage("We strive to provide users a positive environment. Only share encouraging posts.");
            builder.setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Change the user permission in share preferences and on Parse
                    Log.i(TAG, "User is giving permission to add a story");
                    setSharingPermission();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // return the user to the main view
                    Log.i(TAG, "User is canceling add a story");
                    Intent intent = new Intent();
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
            });
            builder.create().show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        // If user presses share
        if (id == R.id.action_share) {
            // get the listener for the fragment so we can get the values
            getFragmentListener();
        }
        return super.onOptionsItemSelected(item);
    }

    // Share listener connecting the text fragment to the activity
    public void getFragmentListener() {
        Log.i(TAG, "User needs values");
        mTextFragment.returnValuesToActivity();
    }

    // When the user wants to share the story
    @Override
    public void prepareStoryForShare(final String title, final String story) {
        // Get the passed back values
        Log.i(TAG, "Story title: " + title + " story: " + story);
        // verify fields are not empty
        if ((!title.equals("")) || (!story.equals(""))) {
            // start the post
            final StoryPost post = new StoryPost();
            // see if we have an image
            if (mStoryThumbNail != null) {
                startSpinner("Setting Image");
                // compress the image
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mStoryThumbNail.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] byteArray = baos.toByteArray();

                try
                {
                    // Create the new parse file
                    final ParseFile storyFile = new ParseFile ("storyimage" + ".png", byteArray);
                    // save the story
                    storyFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            stopSpinner();
                            if (e == null) {
                                post.setStoryImage(storyFile);
                                saveStory(title, story, post);
                            } else {
                                Log.d(TAG, "Story image error code: " + e.getCode() + " message: " + e.getMessage());
                                toastUser("Sorry, we are having trouble sharing this image");
                            }
                        }
                    });
                }
                catch (Exception e)
                {
                    toastUser("Sorry, that image is too big, please try again.");
                }

            } else {
                //share the story
                saveStory(title, story, post);

            }
        } else {
            // Alert user they need a title and story
            Toast.makeText(this, "Pleas enter both a title and story to share.", Toast.LENGTH_SHORT);
        }
    }

    private void saveStory(String title, String story, StoryPost post) {
        // save the rest
        post.setAuthor(ParseUser.getCurrentUser());
        post.setTitle(title);
        post.setStory(story);
        startSpinner("Posting Story");
        post.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                stopSpinner();
                if (e == null) {
                    // Yea! Story saved
                    Log.d(TAG, "Story posted!");
                    // empty the views and return to the main feed
                    resetAndReturn();
                } else {
                    Log.d(TAG, "Story error code: " + e.getCode() + " message: " + e.getMessage());
                    toastUser("Sorry, we are unable to share this story right now. Try again later.");
                }
            }
        });
    }


    // reset the fields and return to the main feed when story saves successfully
    private void resetAndReturn(){
        mStoryThumbNail = null;
        mImageUri = null;
        mHelper = null;
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    // Media button methods from Post Media Fragment
    @Override
    public void OpenCamera() {
        Log.i(TAG, "User pressed the camera button");
        // Create the camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the uri to get the captured image
        mImageUri = mHelper.createImageUri();
        if(mImageUri != null) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            // open the camera
            startActivityForResult(cameraIntent, REQUEST_TAKE_PICTURE);
        }
    }

    @Override
    public void OpenGallery() {
        Log.i(TAG, "User pressed the gallery button");
        // Create the intent to get an image from the gallery
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent , REQUEST_ACCESS_IMAGE_GALLERY );
    }

    // Update the user preference so they have permission to share a story
     public void setSharingPermission () {
        // Set the preference
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("com.example.angessmith.goodfeed.PERMISSION", "YES");
        editor.apply();
        // Update Parse
        ParseUser user = ParseUser.getCurrentUser();
        user.put("sharePermission", "YES");
        // set it to save when it can, not an immediate priority
        user.saveEventually();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure we returned without canceling
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = null;
            switch (requestCode) {
                case REQUEST_TAKE_PICTURE:
                    if(data == null) {
                        Log.d(TAG, "data is empty, decoding uri for bitmap");
                        // no thumbnail,create one
                        mStoryThumbNail = mHelper.createThumbnailOfImage(mImageUri);
                        // set it in the image view
                        addImageToView(mStoryThumbNail);
                    } else {
                        // get the thumbnail data from the intent
                        addImageToView((Bitmap)data.getParcelableExtra("data"));
                    }
                    break;
                case REQUEST_ACCESS_IMAGE_GALLERY:
                    if (data != null) {
                        Log.d(TAG, "Gallery image returned");
                        // get the data
                        mImageUri = data.getData();
                        try {
                            // get this image from the gallery
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // is bitmap is good, set in the image view
                        if (bitmap != null) {
                            // create a thumbnail of the image
                            mStoryThumbNail = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/4, bitmap.getHeight()/4, false);
                            addImageToView(mStoryThumbNail);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void addImageToView(Bitmap bitmap) {
        // Get the cancel fragment and pass in the image
        PostCancelFragment cancelFragment = PostCancelFragment.newInstance(bitmap);
        // Switch the add media fragment for the cancel media fragment
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.post_media_container, cancelFragment, PostCancelFragment.TAG);
        ft.commit();
    }

    @Override
    public void ClearImage(ImageView imageView) {
        Log.d(TAG, "Removing image and cancel fragment, placing media back in.");
        // clear the image in the view
        imageView.setImageDrawable(null);
        // empty out the thumb
        mStoryThumbNail = null;
        // change the fragments back
        PostMediaFragment mediaFragment = PostMediaFragment.newInstance();
        // Switch back
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.post_media_container, mediaFragment, PostMediaFragment.TAG);
        ft.commit();
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
