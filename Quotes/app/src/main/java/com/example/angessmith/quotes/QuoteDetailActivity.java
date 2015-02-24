package com.example.angessmith.quotes;

// Created by AngeSSmith on 11/6/14 for MDF3 1412.

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.angessmith.quotes.Fragment.DetailFragment;

public class QuoteDetailActivity extends Activity  {
    public static final String TAG = "QuoteDetailActivity.TAG";
    public static QuoteObject mQuoteObject;
    public static final String EXTRA_SELECTED_QUOTE = "com.example.angessmith.QuoteDetailActivity.EXTRA_SELECTED_QUOTE";
    public static final String EXTRA_SELECTED_POSITION = "com.example.angessmith.QuoteDetailActivity.EXTRA_SELECTED_POSITION";
    boolean mDidlaunchFromWidget = false;
    public static final String WIDGET_LAUNCH = "com.example.angessmith.quotes.QuoteDetailActivity.WIDGET_LAUNCH";
    public static int mSelectedPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_detail);
        Intent intent = getIntent();
        // Make sure it is not empty
        if (intent != null) {
            if (intent.hasExtra(WIDGET_LAUNCH)) {
                Log.d(TAG, "Recieved call from widget");
                mDidlaunchFromWidget = intent.getBooleanExtra(WIDGET_LAUNCH, false);
            }
            mQuoteObject = (QuoteObject) intent.getSerializableExtra(EXTRA_SELECTED_QUOTE);
            mSelectedPosition = intent.getIntExtra(EXTRA_SELECTED_POSITION, -1);
            // set up the view for the object
            Log.d(TAG, "Current object: " + mQuoteObject);
        }
        if (savedInstanceState == null) {
            // Get the object passed over
            // Inflate the fragment to the container
            DetailFragment fragment = DetailFragment.newInstance(mQuoteObject);
            getFragmentManager().beginTransaction().replace(R.id.detail_container, fragment, DetailFragment.TAG).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // set up the menu to show the delete button
        getMenuInflater().inflate(R.menu.delete_action_bar, menu);
        return true;
    }
    // Set up the delete action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_delete) {
            Log.i(TAG, "User deleting quote from detail activity");
            //Confirm delete with user

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.dialog_confirm_title);
            alert.setMessage(R.string.dialog_confirm_mesage);
            // set up the click actions
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // delete this item from the list
                    deleteSelectedQuote();
                }
            });

            alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // return to list
                }
            });
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteSelectedQuote() {
        Log.d(TAG, "Removing item from lists and updating widget");
        // see where the intent origionated from
        if (mDidlaunchFromWidget) {
            Log.d(TAG, "Launched from widget");
            // Remove the selected item in the list
            DataHelper.UpdateListByRemovingItem(mSelectedPosition, this);
            // close the activity
            Intent finishIntent = new Intent(this, QuoteWidgetProvider.class);
            setResult(RESULT_OK, finishIntent);
            // finish the activity
            Log.d(TAG, "Finished deleting item");
            finish();
        } else {
            // Pass the selected position to the list to remove
            Intent finishIntent = new Intent(this, QuoteListActivity.class);
            finishIntent.putExtra(QuoteListActivity.QUOTE_POSITION_KEY, mSelectedPosition);
            setResult(RESULT_OK, finishIntent);
            // finish the activity
            Log.d(TAG, "Finished deleting item");
            finish();
        }
    }
}
