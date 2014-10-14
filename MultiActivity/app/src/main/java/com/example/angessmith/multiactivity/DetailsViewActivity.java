package com.example.angessmith.multiactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.angessmith.multiactivity.Fragment.GiftDetailFragment;
import com.example.angessmith.multiactivity.Fragment.GiftObject;


public class DetailsViewActivity extends Activity  implements GiftDetailFragment.GiftDetailListener /*, GiftDetailFragment.OnDeleteClickListener */{

    public static final String TAG = "DetailsViewActivity";
    public static final String DATA_KEY = "com.example.angessmith.GIFT";
    public static final String DATA_POSITION = "com.example.angessmith.POSITION";
    private GiftObject mGiftObject;
    private int mListPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);

        if (savedInstanceState == null) {
            // load the fragment
            GiftDetailFragment fragment = GiftDetailFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.details_container, fragment, GiftDetailFragment.TAG).commit();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // set the interface values so the detail fragment can access the passed in object and listview postion
    @Override
    public GiftObject getGiftObject() {
        return mGiftObject;
    }

    @Override
    public int getGiftPosition() {
        return mListPosition;
    }

    @Override
    public void deleteGift() {
        // Finish the intent back to the main list passing the position to be deleted
        Log.i(TAG, "Deleting item at position: " + mListPosition);
        Intent intent = new Intent();
        //intent.putExtra(MainListActivity.DATA_POSITION, position);
        intent.putExtra(MainListActivity.DATA_POSITION, mListPosition);
        setResult(RESULT_OK, intent);
        finish();
    }


    /*
    @Override
    public void deleteDetailedGift(int position) {
        // Finish the intent back to the main list passing the position to be deleted
        Log.i(TAG, "Deleting item at position: " + position);
        Intent intent = new Intent();
        //intent.putExtra(MainListActivity.DATA_POSITION, position);
        intent.putExtra(MainListActivity.DATA_POSITION, mListPosition);
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void deleteDetailedGift() {
        Log.i(TAG, "Deleting item at position: " + mListPosition);
        Intent intent = new Intent();
        intent.putExtra(MainListActivity.DATA_POSITION, mListPosition);
        setResult(RESULT_OK, intent);
        finish();

    }
    */
}
