package com.example.angessmith.goodfeed.Fragment;


// Created by AngeSSmith on 12/11/14.
// Fragment that displays the edit mode of the user's profile view including their name within the textview
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.angessmith.goodfeed.Listeners.OnEditProfileListener;
import com.example.angessmith.goodfeed.R;
import com.example.angessmith.goodfeed.StoryAuthor;


public class EditUserProfileFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "EditUserProfileFragment.TAG";
    StoryAuthor mStoryAuthor;
    EditText mEditUserNameText;
    ImageView mImageCover;
    ImageView mImagePictureView;

    private OnEditPictureButton mOnEditPictureButton;

    // Set up the fragment
    public static EditUserProfileFragment newInstance(StoryAuthor author) {
        // Get and set the author
        EditUserProfileFragment fragment = new EditUserProfileFragment();
        fragment.mStoryAuthor = author;
        return fragment;
    }

    OnEditProfileListener mOnClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the container
        View view = inflater.inflate(R.layout.fragment_user_edit, container, false);
        // get the edit text view
        mEditUserNameText = (EditText) view.findViewById(R.id.edit_user_profile_name);
        // and the image views
        mImageCover = (ImageView) view.findViewById(R.id.edit_user_cover_view);
        mImagePictureView = (ImageView) view.findViewById(R.id.edit_user_profile_picture_view);
        // and the change image button
        Button addImageButton = (Button) view.findViewById(R.id.edit_add_image_button);
        addImageButton.setOnClickListener(this);
        // Set the current user's name in the edit text view
        setUsername();
        return view;
    }

    public void setUsername() {
        if (mStoryAuthor != null) {
            // make sure we have a valid name
            if (mStoryAuthor.getUserName() != null) {
                Log.d(TAG, "User name = " + mStoryAuthor.getUserName());
                mEditUserNameText.setText(mStoryAuthor.getUserName());
            }
        }
    }

    // set the listener from the User Details Activity
    public void setListener(OnEditProfileListener listener) {
        this.mOnClickListener = listener;
    }

    // Set the interface
    public interface OnEditPictureButton {
        // Pass the image views over to the activity
        public void AddNewProfileImage (ImageView coverView, ImageView pictureView);

    }

    @Override
    public void onClick(View v) {
        // When the user clicks on the add image button
        if (v.getId() == R.id.edit_add_image_button) {
            // send the imageviews to the activity
            mOnEditPictureButton.AddNewProfileImage(mImageCover, mImagePictureView);
        }
    }

    // Attach the listener to the activity
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnEditPictureButton) {
            // attach the listener
            mOnEditPictureButton = (OnEditPictureButton) activity;
        }
    }

    // When the user presses share, send the current name to the activity
    public void getUserName() {
        // get the values
        mOnClickListener.checkValuesAndUpdateProfile(mEditUserNameText.getText().toString());
    }
}
