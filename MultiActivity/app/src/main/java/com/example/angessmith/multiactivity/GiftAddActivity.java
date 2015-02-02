package com.example.angessmith.multiactivity;
// Created by AngeSSmith on 10/12/14 for Java 2 Week 3 Term 1410.
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.angessmith.multiactivity.Fragment.GiftAddFragment;
import com.example.angessmith.multiactivity.Fragment.GiftObject;


public class GiftAddActivity extends Activity {
    GiftAddFragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_add);

        // Add up functionality
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            mFragment = GiftAddFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.gift_add_container, mFragment, GiftAddFragment.TAG).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gift_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // See which button was called
        switch (item.getItemId()) {
            // case R.id.action_settings:
            // settings
            //   break;
            case R.id.action_save:
                // Save this object
                onSaveItemClick();
            case R.id.action_clear:
                // Clear the fields
                onClearFields();
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSaveItemClick () {
        // Create a new object if possible from the fragment
        GiftObject gift = (GiftObject) mFragment.onSaveItemClick();
        if (gift != null) {
            // Create a new intent and pass the object through a bundle
            Intent intent = new Intent(this, MainListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(MainListActivity.DATA_KEY, gift);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            // return to the main list with the new object
            finish();
        }
    }

    private void onClearFields () {
        // Call the fragment to clear the textfields
        mFragment.clearTextViews();
    }

}
