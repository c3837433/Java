package com.example.angessmith.multiactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.angessmith.multiactivity.Fragment.GiftAddFragment;


public class GiftAddActivity extends Activity implements  GiftAddFragment.OnSaveItemListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_add);

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void SaveItemsToList(String itemName, String itemLocation, String itemPrice, String itemUrl) {
        // Get the passed over values
        Intent intent = new Intent();
        intent.putExtra("com.example.angessmith.ITEM_NAME", itemName);
        intent.putExtra("com.example.angessmith.ITEM_LOCATION", itemLocation);
        intent.putExtra("com.example.angessmith.ITEM_PRICE", itemPrice);
        intent.putExtra("com.example.angessmith.ITEM_URL", itemUrl);
        setResult(RESULT_OK, intent);
        finish();
    }
}
