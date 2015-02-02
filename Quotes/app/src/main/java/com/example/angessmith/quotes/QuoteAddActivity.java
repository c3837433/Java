package com.example.angessmith.quotes;

// Created by AngeSSmith on 11/6/14 for MDF3 1412.


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.angessmith.quotes.Fragment.AddItemFragment;

public class QuoteAddActivity extends Activity {
    public static final String TAG = "QuoteAddActivity.TAG";
    // Create a fragment variable
    AddItemFragment mAddItemFragment;
    boolean mDidlaunchFromWidget = false;
    public static final String WIDGET_LAUNCH = "com.example.angessmith.quotes.QuoteAddActivity.WIDGET_LAUNCH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_add);
        Intent intent = getIntent();
        if (intent.hasExtra(WIDGET_LAUNCH)) {
            Log.d(TAG, "Recieved call from widget");
            mDidlaunchFromWidget = intent.getBooleanExtra(WIDGET_LAUNCH, false);
        }

        if (savedInstanceState == null) {
            // Inflate the fragment to the container
            mAddItemFragment = AddItemFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.add_quote_container, mAddItemFragment, AddItemFragment.TAG).commit();
        }
    }

    // Set up menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // add the add menu to allow the user to save the item
        getMenuInflater().inflate(R.menu.add_activity, menu);
        return true;
    }

    // Set up the menu action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Make sure we have the right id
        int id = item.getItemId();
        if (id == R.id.action_save_quote) {
            Log.i(TAG, "User pressed save item");
            // Save this item
            saveQuote();
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveQuote () {
        // See if we have an object
        QuoteObject quoteObject = (QuoteObject) mAddItemFragment.checkAndSaveQuote();
        if (quoteObject != null) {
            if (mDidlaunchFromWidget) {
                Log.d(TAG, "Updating the list and widget with new item");
                // Update the list and widget
                DataHelper.UpdateListWithNewItem(quoteObject, this);
                // And finish the intent
                Intent finishIntent = new Intent(this, QuoteWidgetProvider.class);
                setResult(RESULT_OK, finishIntent);
                // finish the activity
                Log.d(TAG, "Finishing activity");
                finish();
            } else {
                // finish the intent request
                Log.d(TAG, "Returning new object to list activity to save");

                // Then we can return to the list activity
                Intent intent = new Intent(this, QuoteListActivity.class);
                // create a new bundle to pass the serializable object
                Bundle bundle = new Bundle();
                bundle.putSerializable(QuoteListActivity.QUOTE_DATA_KEY, quoteObject);
                // add the bundle to the intent
                intent.putExtras(bundle);
                // set the result to good
                setResult(RESULT_OK, intent);
                // return to the list with the quote
                finish();
            }
        }
    }
}
