package com.example.angessmith.goodfeed;

// Created by AngeSSmith on 12/2/14.
// Registration for Good Feed activity
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.example.angessmith.goodfeed.Fragment.RegisterFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends Activity implements RegisterFragment.OnRegisterButtonsListener {

    public static final String TAG = "RegisterActivity";
    private HelperMethods mHelper = new HelperMethods();
    Uri mCreateUri =  null;
    public static final int REQUEST_CAMERA_FOR_PROFILE = 9111999;
    private CircleImageView mImageView;
    private Bitmap mThumbNail;
    private ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        RegisterFragment fragment = RegisterFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.register_container, fragment, RegisterFragment.TAG).commit();
    }

    @Override
    public void verifyUserValues(String name, String email, String password, String confirmPw) {
        Log.i(TAG, "User is trying to register. Name: " + name + " email: " + email + " pw1: " + password + " pw2: " + confirmPw);
        // verify fields are not blank
        if (name.length() > 2) {
            boolean emailValid = checkEmail(email);
            // if we have a valid email and passwords match
            if (emailValid) {
                 if (password.equals(confirmPw) && (password.length() != 0)) {
                     // User is valid, pass values to create new user
                     createNewUser(name, email, password);
                } else {
                     toastUser("Passwords do not match");
                 }
            } else {
                // Email invalid
                toastUser("Please provide a valid Email Address");
            }
        } else {
            // Name is invalid
            toastUser("Please provide a valid name");
        }
    }

    @Override
    public void addProfileImage(CircleImageView imageView) {
        Log.i(TAG, "User trying to add profile image");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the uri to get the captured image
        mCreateUri = mHelper.createImageUri();
        mImageView = imageView;
        if(mCreateUri != null) {
            Log.d(TAG, "Have valid uri");
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCreateUri);
            // start request
            startActivityForResult(cameraIntent, REQUEST_CAMERA_FOR_PROFILE);
        }
    }

    boolean checkEmail(String email) {
        // access androids patterns, and check to see it the passed in email matches the pattern
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void createNewUser(String name, String email, String password) {
        // Create a new user
        final StoryAuthor newUser = new StoryAuthor();
        // set the initial values
        newUser.setUsername(email.toLowerCase());
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.put("UserFullName", name);
        newUser.put("sharePermission", "NO");
        // see if we have a profile picture
        if (mThumbNail != null) {
            Log.d(TAG, "User has profile picture");
        } else {
            Log.d(TAG, "No Profile picture, set default");
            // sign up the user with the default image
            Drawable drawable = getResources().getDrawable(R.drawable.default_profile);
            mThumbNail = ((BitmapDrawable)drawable).getBitmap();
        }
        // Create a stream to compress the image and turn it into an array of bytes
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mThumbNail.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] data = stream.toByteArray();

        try
        {
            // Create the new parse file
            final ParseFile profileFile = new ParseFile ("userProfile.jpg", data);
            // save it to the user
            startSpinner("Starting Registration");
            profileFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    stopSpinner();
                    if (e == null) {
                        // add the profile picture
                        newUser.put("ProfilePicture", profileFile);
                        // Try to sign the user up
                        signUpUser(newUser);
                    }
                }
            });
        }
        catch (Exception e)
        {
            toastUser("Sorry, that image is too big, please try again.");
        }

    }

    private void signUpUser(ParseUser newUser) {
        startSpinner("Finishing...");
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                stopSpinner();
                // Handle the response
                if (e == null) {
                    Log.i(TAG, "User registration successful");
                    // Set default preference
                    setDefaultPermission();
                    registerCompleteReturnToMain();
                } else {
                    switch (e.getCode()) {
                        case ParseException.EMAIL_TAKEN:
                            toastUser("Sorry, that email address has already been registered.");
                            break;
                        case ParseException.USERNAME_TAKEN:
                            toastUser("Sorry, that username has already been registered.");
                            break;
                        default:
                            toastUser("Sorry, we are unable to register you right now, try again later.");
                            // find out what the problem is
                            Log.d(TAG, "Problem message: " + e.getMessage());
                            Log.d(TAG, "Problem code: " + e.getCode());
                            break;
                    }
                }
            }
        });
    }

    private void registerCompleteReturnToMain() {
        // Sign up is valid, return to the launch activity
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void toastUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void setDefaultPermission () {
        // Set the default permission for the user
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("com.example.angessmith.goodfeed.PERMISSION", "NO");
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure we returned without canceling
        Log.d(TAG, "Returning from intent");
        if (resultCode == RESULT_OK & requestCode == REQUEST_CAMERA_FOR_PROFILE) {
            Log.d(TAG, "Returned with image");
            if(data == null) {
                Log.d(TAG, "data is empty, decoding uri for bitmap");
                // no thumbnail, get the image sizes
                mThumbNail = mHelper.createThumbnailOfImage(mCreateUri);
                // set in the image view
                mImageView.setImageBitmap(mThumbNail);
            } else {
                Log.d(TAG, "setting thumbnail bitmap to imageview");
                Bitmap thumbnail = data.getParcelableExtra("data");
                // get the data from the intent
                mImageView.setImageBitmap(thumbnail);
            }
        }
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
