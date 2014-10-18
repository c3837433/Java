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
            GiftAddFragment fragment = GiftAddFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.gift_add_container, fragment, GiftAddFragment.TAG).commit();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save) {
            // Save this object
            onSaveItemClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSaveItemClick () {
        // Have the fragment get the values and create a new object
        GiftAddFragment fragment = (GiftAddFragment) getFragmentManager().findFragmentById(R.id.gift_add_container);
        // Create a new object if possible
        GiftObject gift = (GiftObject) fragment.onSaveItemClick();
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
}
