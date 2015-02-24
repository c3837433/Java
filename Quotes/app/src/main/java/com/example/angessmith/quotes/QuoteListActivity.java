package com.example.angessmith.quotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.angessmith.quotes.Fragment.ConfirmDeleteDialogFragment;
import com.example.angessmith.quotes.Fragment.QuoteListFragment;

// Created by AngeSSmith on 11/6/14 for MDF3 1412.

public class QuoteListActivity extends Activity implements QuoteListFragment.QuoteClickListener, ConfirmDeleteDialogFragment.DialogListener{

    // Define the tag
    public static final String TAG = "QuoteListActivity.TAG";
    // And the key when passing the quote back from the add activity
    public static final String QUOTE_DATA_KEY = "com.example.angessmith.quotes.QUOTE";
    public static final String QUOTE_POSITION_KEY = "com.example.angessmith.quotes.QUOTE_POSITION_KEY";
    // and create the request code to get a new quote from the add activity
    public static final int ADD_QUOTE_REQUEST = 34681315;
    public static final int DETAIL_REQUEST = 51318642;
    // And the strings for the adapter
    public static final String ADAPTER_QUOTE = "quote_text";
    public static final String ADAPTER_AUTHOR = "quote_author";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_list);
        Log.d(TAG, "Launching List Activity onCreate");
        if (savedInstanceState == null) {
            // Load the fragment into the container
            QuoteListFragment fragment = QuoteListFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.listView_container, fragment, QuoteListFragment.TAG).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_add_quote) {
            openAddQuoteActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openAddQuoteActivity () {
        // Start the intent to load the add activity
        Intent intent = new Intent(this, QuoteAddActivity.class);
        // Send the request code to add the quote upon returning
        startActivityForResult(intent, ADD_QUOTE_REQUEST);
    }

     // When user returns from add activity
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         Log.d(TAG, "Return from activity");
        // See if anything was returned with it
        if (resultCode == RESULT_OK && requestCode == ADD_QUOTE_REQUEST) {
            Log.i(TAG, "Object returned from add activity, adding to list");
            // We have returned a new quote successfully, get it
            QuoteObject object = (QuoteObject) data.getSerializableExtra(QUOTE_DATA_KEY);
            // Update the list
            DataHelper.UpdateListWithNewItem(object, this);
            // reload the data list
            QuoteListFragment.reloadListWithData(this);
            // then refresh the list
            QuoteListFragment.addNewItemtoHashmap();
        } else if (resultCode == RESULT_OK && requestCode == DETAIL_REQUEST) {
            Log.i(TAG, "Object returned from detail activity, checking for item to delete");
            // see if we need to delete an item
            if (data.hasExtra(QUOTE_POSITION_KEY)) {
                Log.d(TAG, "Need to remove item from list");
                int position = data.getIntExtra(QUOTE_POSITION_KEY, -1);
                // delete this from the list
                DataHelper.UpdateListByRemovingItem(position, this);
                // update the list
                QuoteListFragment.reloadListWithData(this);
                // and reset the list
                QuoteListFragment.addNewItemtoHashmap();
            }
        }
    }

    @Override
    public void viewQuoteInDetailView(QuoteObject quote, int position) {
        // pass this object to the detail view
        Intent intent = new Intent(this, QuoteDetailActivity.class);
        // Add the object to a bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable(QuoteDetailActivity.EXTRA_SELECTED_QUOTE, quote);
        intent.putExtra(QuoteDetailActivity.EXTRA_SELECTED_POSITION, position);
        intent.putExtras(bundle);
        //setResult(RESULT_OK, intent);
        // Start detail activity
        //startActivity(intent);
        startActivityForResult(intent, DETAIL_REQUEST);
    }

    @Override
    public void deleteSelectedQuote() {
        Log.d(TAG, "User chose to delete an item");
        // get the fragment
        QuoteListFragment fragment = (QuoteListFragment) getFragmentManager().findFragmentById(R.id.listView_container);
        // delete the selected item
        fragment.deleteSelectedQuoteFromList();
    }
}
