package com.example.angessmith.fundamentalsapp;

//Created by AngeSSmith on 9/30/14 for Java 2 Term 1410.

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.angessmith.fundamentalsapp.Fragment.BookListFragment;

import java.io.File;


public class Main extends Activity   {
    final String TAG = "FundamentalsApp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Log.d(TAG, "Getting Fragment");
            // Create the fragment
            BookListFragment bookListFragment = BookListFragment.newInstance();
            // get the fragment and commit it so we can access it with the spinner
            getFragmentManager().beginTransaction().replace(R.id.book_list_container, bookListFragment, BookListFragment.TAG).commit();
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
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
