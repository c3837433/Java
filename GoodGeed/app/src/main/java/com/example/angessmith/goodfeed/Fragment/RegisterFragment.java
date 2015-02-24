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

import de.hdodenhof.circleimageview.CircleImageView;

// Created by AngeSSmith on 12/2/14.
// Fragment that displays the register activity views

public class RegisterFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "RegisterFragment";
    // define the button click interface
    private OnRegisterButtonsListener mOnRegisterButtonsListener;
    private TextView mNameView;
    private TextView mEmailView;
    private TextView mPasswordView;
    private TextView mConfirmPasswordView;
    private CircleImageView mProfileImageView;

    // Set up the fragment
    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the container
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        // set the buttons
        Button registerButton = (Button) view.findViewById(R.id.register_user_button);
        registerButton.setOnClickListener(this);
        Button addImageButton = (Button) view.findViewById(R.id.add_image_button);
        addImageButton.setOnClickListener(this);
        // the image/camera view
        mProfileImageView = (CircleImageView) view.findViewById(R.id.camera_profile_image);

        // and the text views
        mNameView = (TextView) view.findViewById(R.id.register_name_text);
        mEmailView = (TextView) view.findViewById(R.id.register_email_text);
        mPasswordView = (TextView) view.findViewById(R.id.register_pass_text);
        mConfirmPasswordView = (TextView) view.findViewById(R.id.register_pass_two_text);
        return view;
    }

    @Override
    public void onClick(View v) {
        // set the methods based on which button was selected
        switch (v.getId()) {
            case R.id.register_user_button:
                // get the values from the text views
                mOnRegisterButtonsListener.verifyUserValues(mNameView.getText().toString(), mEmailView.getText().toString(), mPasswordView.getText().toString(), mConfirmPasswordView.getText().toString());
                break;
            case R.id.add_image_button:
                // take a photo
                mOnRegisterButtonsListener.addProfileImage(mProfileImageView);
                break;
            default:
                break;
        }
    }

    // Set the interface
    public interface OnRegisterButtonsListener {
        public void verifyUserValues(String name, String email, String password, String confirmPw);
        public void addProfileImage(CircleImageView imageView);
    }

    // attach the listener to the activity
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnRegisterButtonsListener) {
            // attach the listener
            mOnRegisterButtonsListener = (OnRegisterButtonsListener) activity;
        }
    }

}
