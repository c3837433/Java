package com.example.angessmith.multiactivity;
// Created by AngeSSmith on 10/12/14 for Java 2 Week 3 Term 1410.
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.angessmith.multiactivity.Fragment.GiftDetailFragment;
import com.example.angessmith.multiactivity.Fragment.GiftObject;


public class DetailsViewActivity extends Activity  implements GiftDetailFragment.GiftDetailListener {

    public static final String TAG = "DetailsViewActivity";
    public static final String DATA_KEY = "com.example.angessmith.GIFT";
    public static final String DATA_POSITION = "com.example.angessmith.POSITION";
    private GiftObject mGiftObject;
    private int mListPosition;
    GiftDetailFragment mFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);

        // Add up functionality
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            // load the fragment
            mFragment = GiftDetailFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.details_container, mFragment, GiftDetailFragment.TAG).commit();
        }

        // Get the intent values for the list object and its position from the main listview
        Intent detailIntent = getIntent();
        if (detailIntent != null) {
            mGiftObject = (GiftObject) detailIntent.getSerializableExtra(DATA_KEY);
            mListPosition = detailIntent.getIntExtra(DATA_POSITION, -1);
            Log.i(TAG, "Gift data from activity: " + mGiftObject + " from position: " + mListPosition);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // case R.id.action_settings:
            // settings
            //   break;
            case R.id.action_share:
                // Tell the fragment to share the story
                mFragment.shareStory();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // set the interface values so the detail fragment can access the passed in object and listview postion
    @Override
    public GiftObject getGiftObject() {
        return mGiftObject;
    }
    /*
    @Override
    public void deleteGift() {
        // Finish the intent back to the main list passing the position to be deleted
        Log.i(TAG, "Deleting item at position: " + mListPosition);
        Intent intent = new Intent();
        intent.putExtra(MainListActivity.DATA_POSITION, mListPosition);
        setResult(RESULT_OK, intent);
        finish();
    }
    */
}
