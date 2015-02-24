package com.example.angessmith.goodfeed.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.angessmith.goodfeed.R;
import com.example.angessmith.goodfeed.StoryPost;

// Created by AngeSSmith on 12/12/14.
// Fragment that displays the list dialog when the user flags an item within the main feed

public class FlagDialogFragment extends DialogFragment {

    public static final String TAG = "FlagDialogFragment";
    // define the listener
    FlagReasonListener mFlagReasonListener;
    public static StoryPost mFlaggedPost;

    public static FlagDialogFragment newInstance(StoryPost post) {
        FlagDialogFragment fragment = new FlagDialogFragment();
        mFlaggedPost = post;
        return fragment;
    }

    // set up the dialog when it is created
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the title
        builder.setTitle("Flag Post")
                // Get the string array items
                .setItems(R.array.flag_reason, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        // return the reason index to the activity so we know which one was selected
                        mFlagReasonListener.onFlagReasonClick(which, mFlaggedPost);
                    }
                });

        return builder.create();
    }

    public interface FlagReasonListener {
        // listen for the user to click and get the selected index
        public void onFlagReasonClick(int reason, StoryPost post);
    }

    // Attach the listener to the activity
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FlagReasonListener) {
            // set the listener
            mFlagReasonListener = (FlagReasonListener) activity;
        }
    }
}
