package com.example.angessmith.goodfeed.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.angessmith.goodfeed.R;

// Created by AngeSSmith on 12/2/14.
// Fragment that displays the log in view

public class LoginFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "LoginFragment";
    private TextView mEmailView;
    private TextView mPasswordView;
    private OnButtonListener mButtonListener;

    // Define a new instance
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    // Define the button interface for user clicks
    public interface  OnButtonListener {
        public void checkLogInInfo(String email, String password);
        public void moveToRegistrationView();
        public void requestPasswordChange();
        public void logInWithFacebook();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the container with the fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        // get all the buttons
        Button logInButton = (Button) view.findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(this);
        Button logInFacebookButton = (Button) view.findViewById(R.id.log_in_facebook_button);
        logInFacebookButton.setOnClickListener(this);
        Button registerButton = (Button) view.findViewById(R.id.login_register_button);
        registerButton.setOnClickListener(this);
        Button forgotPassButton = (Button) view.findViewById(R.id.forgot_password_btn);
        forgotPassButton.setOnClickListener(this);
        // and the text views
        mEmailView = (TextView) view.findViewById(R.id.user_email_text);
        mPasswordView = (TextView) view.findViewById(R.id.user_pass_text);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Make sure the activity attaches these listeners
        if (activity instanceof OnButtonListener) {
            // set the listener
            mButtonListener = (OnButtonListener) activity;
        }
    }

    @Override
    public void onClick(View v) {
        // see which button triggered the click
        switch (v.getId()) {
            case R.id.log_in_button:
                // Check values
                mButtonListener.checkLogInInfo(mEmailView.getText().toString(), mPasswordView.getText().toString());
                break;
            case R.id.log_in_facebook_button:
                // check log in facebook
                mButtonListener.logInWithFacebook();
                break;
            case R.id.login_register_button:
                // move to registration view
                mButtonListener.moveToRegistrationView();
                break;
            case R.id.forgot_password_btn:
                // load forgot password dialog
                mButtonListener.requestPasswordChange();
                break;
        }
    }


}
