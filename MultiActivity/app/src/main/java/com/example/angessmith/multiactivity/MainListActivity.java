package com.example.angessmith.multiactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.angessmith.multiactivity.Fragment.ButtonFragment;
import com.example.angessmith.multiactivity.Fragment.GiftAddFragment;
import com.example.angessmith.multiactivity.Fragment.GiftDetailFragment;
import com.example.angessmith.multiactivity.Fragment.GiftListFragment;
import com.example.angessmith.multiactivity.Fragment.GiftObject;

import java.util.ArrayList;


public class MainListActivity extends Activity implements ButtonFragment.OnButtonClickListener, GiftListFragment.OnGiftItemClickListener {

    // Create the strings for the tag and request id
    public static final String TAG = "MainListActivity";
    public static final int GIFT_REQUESTCODE = 15148643;
    public static final String ARG_GIFTLIST = "GiftListFragment.ARG_GIFTLIST";
    public static final String DATA_KEY = "com.example.angessmith.GIFT";
    public static GiftObject GIFTOBJECT;

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
        // Create the request code
        // start the intent (will change to for result when ready
        startActivityForResult(intent, GIFT_REQUESTCODE);
    }

    // WHEN THE ACTIVITY RETURNS FROM THE ADD A GIFT VIEW
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check to see if we have valid data
        if (resultCode == RESULT_OK && requestCode == GIFT_REQUESTCODE) {
            // see what data came back
            Log.i(TAG, "Data from add item: " + data);
            GiftObject gift = (GiftObject) data.getSerializableExtra(DATA_KEY);
            //GiftObject gift = (GiftObject) data.getSerializableExtra(ARG_GIFTLIST);
            Log.i(TAG, "Gift Object Data: " + gift);
            Log.i(TAG, "Gift Object Data Location: " + gift.getLocation());
            // Add the object to the array list
            GiftListFragment.mGifts.add(gift);
            // update the array adapter
            GiftListFragment.mArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void openGiftInDetailView(GiftObject object) {
        Log.i(TAG, "Selected object = " + object.getLocation());
        Intent intent = new Intent(this, DetailsViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DetailsViewActivity.DATA_KEY, object);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        startActivity(intent);
    }
}
