package com.example.angessmith.multiactivity;
// Created by AngeSSmith on 10/12/14 for Java 2 Week 3 Term 1410.
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.angessmith.multiactivity.Fragment.AlertDialogFragment;
import com.example.angessmith.multiactivity.Fragment.GiftListFragment;
import com.example.angessmith.multiactivity.Fragment.GiftObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/*
    MINIMUM
    DONE Contain at least two screens: list and add.
    DONE List screen will show a list of data with the ability to add a new entry using an add button in the action bar.
         Add screen will allow the user to enter data and save that data to the list shown on the list screen using a save button in the action bar.
    DONE User can long press on an action bar item and utilize a contextual action bar to delete an item from the list.
    DONE Contextual action bar closes after an action is selected.

MASTERY
         Overflow section of the action bar is utilized on the add screen to allow the user to reset the contents of the form.
    DONE When the contextual action bar closes due to a delete action being selected, the altered list is automatically updated.
 */
public class MainListActivity extends Activity implements GiftListFragment.OnGiftItemLongClickListener,GiftListFragment.OnGiftItemClickListener, AlertDialogFragment.DialogListener {

    // Create the strings for the tag and request id
    public static final String TAG = "MainListActivity";
    public static final int GIFT_REQUEST_ADD_CODE = 15148643;
    public static final int GIFT_REQUEST_DETAIL_CODE = 34684151;
    public static final String DATA_KEY = "com.example.angessmith.GIFT";
    public static final String DATA_POSITION = "com.example.angessmith.POSITION";
    public static final String CACHE_FILE = "giftList.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        if (savedInstanceState == null) {
            // Replace the main activity containers with the fragments
            GiftListFragment giftListFragment = GiftListFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.gift_list_container, giftListFragment, GiftListFragment.TAG).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           // case R.id.action_settings:
                // settings
             //   break;
            case R.id.action_add_item:
                openAddStoryView();
            default:
                break;
        }
        /*
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    // CREATE THE INTENT TO VIEW THE ADD A GIFT ACTIVITY
    private void openAddStoryView() {
        Log.i(TAG, "Opening add a item activity");
        // Start a new intent so the add activity opens
        Intent intent = new Intent(this, GiftAddActivity.class);
        // Start the add activity request
        startActivityForResult(intent, GIFT_REQUEST_ADD_CODE);
    }

    // WHEN THE ACTIVITY RETURNS FROM THE ADD A GIFT VIEW
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check to see if we have valid data and which request is being returned
        if (resultCode == RESULT_OK && requestCode == GIFT_REQUEST_ADD_CODE) {
            addItemToListView(data);
        }
        if (resultCode == RESULT_OK && requestCode == GIFT_REQUEST_DETAIL_CODE){
            deleteGiftItem(data);
        }
    }

    private void deleteGiftItem(Intent data) {
        Log.i(TAG, "Data returned from detail view" + data);
        int giftPosition = data.getIntExtra(DATA_POSITION, -1);
        Log.i(TAG, "Item to be deleted is at position: " + giftPosition);
        // remove this item from the listview
        GiftListFragment.mGifts.remove(giftPosition);
        // update the listview
        GiftListFragment.mArrayAdapter.notifyDataSetChanged();

        // Update the file with the current objects
        cacheGiftObjects();
    }

    private void cacheGiftObjects() {
        File externalFilesDir = this.getExternalFilesDir(null);
        File file = new File(externalFilesDir, CACHE_FILE);
        try {
            // create a new stream
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            // write the gifts and close the stream
            outputStream.writeObject(GiftListFragment.mGifts);
            outputStream.close();
        }
        catch(Exception exception) {
            // if it couldn't write the file
            Log.e(TAG, "Output Exception: ", exception);
        }
    }

    private void addItemToListView(Intent data) {
        // Get the data that was sent back
        //Log.i(TAG, "Data from add item: " + data);
        GiftObject gift = (GiftObject) data.getSerializableExtra(DATA_KEY);
        //Log.i(TAG, "Gift Object Data: " + gift);
        //Log.i(TAG, "Gift Object Data Location: " + gift.getLocation());
        // Add the object to the array list
        GiftListFragment.mGifts.add(gift);
        // update the array adapter
        GiftListFragment.mArrayAdapter.notifyDataSetChanged();
        // Update the cache
        cacheGiftObjects();
    }

    @Override
    public void openGiftInDetailView(GiftObject object, int position) {
        Log.i(TAG, "Selected object = " + object.getLocation());
        // Start the intent
        Intent intent = new Intent(this, DetailsViewActivity.class);
        // Create a new bundle to pass in the current object
        Bundle bundle = new Bundle();
        bundle.putSerializable(DetailsViewActivity.DATA_KEY, object);
        // Set the bundle and listview position in the intent
        intent.putExtras(bundle);
        intent.putExtra(DetailsViewActivity.DATA_POSITION, position);
        setResult(RESULT_OK, intent);
        // Start activity
        startActivityForResult(intent, GIFT_REQUEST_DETAIL_CODE);
    }

    // Implement the interface to delete an item when the Alert Dialog confirms delete
    public void onConfirmDeleteItem () {
        // Remove the selected item in the list
        GiftListFragment fragment = (GiftListFragment) getFragmentManager().findFragmentById(R.id.gift_list_container);
        fragment.onConfirmDeleteItem();
    }


}
