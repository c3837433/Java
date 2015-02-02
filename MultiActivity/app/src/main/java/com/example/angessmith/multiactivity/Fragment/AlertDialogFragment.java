package com.example.angessmith.multiactivity.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

// Created by AngeSSmith on 10/18/14 for Java 2 Term 1410.
// Set up the fragment to display an alert dialog upon inflation
public class AlertDialogFragment extends DialogFragment {

    public static final String TAG = "AlertDialogFragment.TAG";

    // Define the interface
    public interface DialogListener {
        public void onConfirmDeleteItem();
    }

    // Define the listener
    private DialogListener mListener;

    // Attach the activity to the listener
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (DialogListener) activity;
    }

    // Create the dialog
    @Override
    public Dialog onCreateDialog(Bundle _savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Confirm Delete");
        dialogBuilder.setMessage("Do you want to delete " + getArguments().getString("item") + "?");
        // set up the buttons
        dialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onConfirmDeleteItem();
            }
        });
        dialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               // do nothing, user canceled
            }
        });
        return dialogBuilder.create();

    }

}
