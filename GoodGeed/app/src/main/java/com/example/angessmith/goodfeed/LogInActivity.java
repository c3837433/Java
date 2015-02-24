package com.example.angessmith.goodfeed;

// Created by Angela Smith for ADP2 Term 1412 December 2, 2014

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.angessmith.goodfeed.Fragment.LoginFragment;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.Request;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LogInActivity extends Activity implements LoginFragment.OnButtonListener {

    public static final String TAG = "LogInActivity";
    public static final int REGISTER_NEW_USER = 34681315;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //set up fragment
        LoginFragment fragment = LoginFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.login_container, fragment, LoginFragment.TAG).commit();
    }

    @Override
    public void checkLogInInfo(String email, String password) {
        Log.i(TAG, "User clicked log in with email: " + email + " and password: " + password);
        // Make sure either field is not blank
        if ((!email.equals("")) || (!password.equals(""))) {
            boolean validEmail = checkEmail(email);
            if (validEmail) {
                // Check against parse
                ParseUser.logInInBackground(email, password, new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (e == null && user != null) {
                            // Check if the user has given permission
                            String permission = (String) user.get("sharePermission");
                            // save to preferences
                            setUserPermissionPreference(permission);
                            // Log in success, return to Launch
                            loginSuccessReturnToMain();
                        } else {
                            if (e != null) {
                                switch (e.getCode()) {
                                    case 101:
                                        toastUser("Sorry, your email and/or password are incorrect.");
                                        break;

                                    default:
                                        toastUser("Sorry, we are unable to log in right now, try again later.");
                                        // find out what the problem is
                                        Log.d(TAG, "Problem message: " + e.getMessage());
                                        Log.d(TAG, "Problem code: " + e.getCode());
                                        break;
                                }
                            }
                        }
                    }
                });

            } else {
                // email is not valid
                toastUser("Please enter a valid email address");
            }

        } else {
            // Alert user we need both an email and password
            toastUser("Login requires both an Email and Password");
        }

    }

    public void setUserPermissionPreference (String permission) {
        // Save to preference to get when user wants to share a story
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("com.example.angessmith.goodfeed.PERMISSION", permission);
        editor.apply();
    }


    // When user presses Register Bow button
    @Override
    public void moveToRegistrationView() {
        Log.i(TAG, "User clicked register");
        Intent intent = new Intent(this, RegisterActivity.class);
        // Send the request code to add the quote upon returning
        startActivityForResult(intent, REGISTER_NEW_USER);
    }

    @Override
    public void requestPasswordChange() {
        Log.i(TAG, "User clicked forgot password");
        // Create the alert prompt to get the user's email address
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        // get the view from the layout
        View view = inflater.inflate(R.layout.alert_prompt, null);
        // set the view for the alert
        builder.setView(view);
        builder.setTitle("Password Reset");
        // create buttons that should not close until confirmed
        builder.setPositiveButton("Reset", null);
        builder.setNegativeButton("Cancel", null);
        // get the edit text within the view
        final EditText emailText = (EditText)view.findViewById(R.id.prompt_email);
        // build and show the alert
        final AlertDialog dialog = builder.create();
        dialog.show();
        // set the listener for the reset button to verify a valid email was entered before calling Parse
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.i(TAG, "User clicked reset");
                // get the entered text
                final String emailEntered = emailText.getText().toString().toLowerCase();
                // make sure it is valid
                boolean valid = checkEmail(emailEntered);
                if (valid) {
                    Log.i(TAG, "Email is valid");
                    // issue the password reset
                    ParseUser.requestPasswordResetInBackground(emailEntered, new RequestPasswordResetCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                switch (e.getCode()) {
                                    // if the email is not registered, alert the user
                                    case ParseException.EMAIL_NOT_FOUND:
                                        toastUser("Sorry, " + emailEntered + " has not been registered.");
                                        break;
                                    default:
                                        toastUser("Sorry, we are unable to make this request at this time");
                                        dialog.dismiss();
                                        break;
                                }
                            } else {
                                // reset was successful
                                dialog.dismiss();
                                toastUser("Password reset");
                            }
                        }
                    });
                } else {
                    // when email is invalid
                    toastUser("Please enter a valid email");
                }
            }
        });
    }

    @Override
    public void logInWithFacebook() {
        Log.i(TAG, "Uer selected to log in with facebook");
        // Create the list of permissions we want to access from facebook
        List<String> fbPermissions = Arrays.asList("public_profile", "email");
        // Open facebook and log in through it
        ParseFacebookUtils.logIn(fbPermissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                // see what was returned
                if (user == null) {
                    Log.d(TAG, "User canceled");
                } else if (user.isNew()) {
                    Log.d(TAG, "New user is logging in");
                    // get user values and set to parse
                    getUserFacebookInfo();
                } else {
                    Log.d(TAG, "Existing user logged in");
                    // Logged user in, move to main feed
                    loginSuccessReturnToMain();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if user logged in, return to launch
        Log.i(TAG, "User returned with request code: " + requestCode + " and result code: " + resultCode);
        if ((requestCode == REGISTER_NEW_USER) && (resultCode == RESULT_OK)) {
            Log.i(TAG, "Returning to launch");
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        // This code means it is a new user
        if (requestCode == 32665) {
            // User has successfully logged in, override the current result
            ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void toastUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    boolean checkEmail(String email) {
        // make sure email is valid before we make a call
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void getUserFacebookInfo() {
        // Make sure we still have an active session
        Session fbSession = ParseFacebookUtils.getSession();
        if (fbSession != null && fbSession.isOpened()) {
            // make the request to get the user properties
            Request.newMeRequest(fbSession, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    Log.i(TAG,"Request Completed");
                    if (user != null) {
                        // Create a new parse user with the user's facebook values
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        currentUser.put("UserFullName",
                                user.getName());
                        currentUser.put("email", user.getProperty("email").toString().toLowerCase());
                        currentUser.put("sharePermission", "NO");
                        currentUser.put("FacebookId", user.getId());
                        // Save this user
                        currentUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Facebook log in is successful, move to main view
                                    loginSuccessReturnToMain();
                                } else {
                                    switch (e.getCode()) {
                                        case ParseException.EMAIL_TAKEN:
                                            toastUser("You have already registered with this email address.");
                                            break;
                                        default:
                                            // find out what the problem is
                                            Log.d(TAG, "Problem message: " + e.getMessage());
                                            Log.d(TAG, "Problem code: " + e.getCode());
                                            break;
                                    }
                                }
                            }
                        });
                    }
                }
            // Execute the task
            }).executeAsync();
        }
    }

    // User has properly logged in, return them to launch/main feed
    private void loginSuccessReturnToMain() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

}
