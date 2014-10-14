package com.example.angessmith.multiactivity;
// Created by AngeSSmith on 10/12/14 for Java 2 Week 3 Term 1410.
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.angessmith.multiactivity.Fragment.ButtonFragment;
import com.example.angessmith.multiactivity.Fragment.GiftListFragment;
import com.example.angessmith.multiactivity.Fragment.GiftObject;


public class MainListActivity extends Activity implements ButtonFragment.OnButtonClickListener, GiftListFragment.OnGiftItemClickListener {

    // Create the strings for the tag and request id
    public static final String TAG = "MainListActivity";
    public static final int GIFT_REQUEST_ADD_CODE = 15148643;
    public static final int GIFT_REQUEST_DETAIL_CODE = 34684151;
    public static final String DATA_KEY = "com.example.angessmith.GIFT";
    public static final String DATA_POSITION = "com.example.angessmith.POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        if (savedInstanceState == null) {
            // Replace the main activity containers with the fragments
            GiftListFragment giftListFragment = GiftListFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.gift_list_container, giftListFragment, GiftListFragment.TAG).commit();

            ButtonFragment buttonFragment = ButtonFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.button_container, buttonFragment, ButtonFragment.TAG).commit();

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // CREATE THE INTENT TO VIEW THE ADD A GIFT ACTIVITY
    @Override
    public void openAddStoryView() {
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
}
