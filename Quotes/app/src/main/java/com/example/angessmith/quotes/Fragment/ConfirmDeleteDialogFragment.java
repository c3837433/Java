package com.example.angessmith.quotes.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.angessmith.quotes.R;

 // Created by AngeSSmith on 11/12/14.

public class ConfirmDeleteDialogFragment extends DialogFragment {

    public static final String TAG = "ConfirmDeleteDialogFragment.TAG";

    // Set up the interface
    public interface DialogListener {
        // run the dialog
        public void deleteSelectedQuote();
    }

    private DialogListener mDialogListener;

    // Attach the activity to the listener
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDialogListener = (DialogListener) activity;
    }

    // Create the dialog
    @Override
    public Dialog onCreateDialog(Bundle _savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(R.string.dialog_confirm_title);
        alert.setMessage(R.string.dialog_confirm_mesage);
        // set up the click actions
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // delete this item from the list
                mDialogListener.deleteSelectedQuote();
            }
        });

        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // return to list
            }
        });

        return alert.create();
    }
}
