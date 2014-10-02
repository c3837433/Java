package com.example.angessmith.fundamentalsapp;

//Created by AngeSSmith on 9/30/14 for Java 2 Term 1410.

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.angessmith.fundamentalsapp.Fragment.BookListFragment;
import com.example.angessmith.fundamentalsapp.Fragment.DetailFragment;

import java.io.File;


public class Main extends Activity implements BookListFragment.OnListItemClickListener  {
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

    @Override
    public void setBookDetails(String title, String author, String description, int rank, String listType) {
        // Get the passed over data
        Log.i(TAG, "Retrieved title: " + title + " author: " + author + " rank: " + rank + " description: " + description);
        // TODO: pass the selected data to the Details fragment
        // Get the detail fragment
        DetailFragment detailFragment = (DetailFragment) getFragmentManager().findFragmentByTag(DetailFragment.TAG);
        // check if it was created
        if (detailFragment == null) {
            // Then create it
            detailFragment = DetailFragment.newInstance(title, author, description, rank, listType);
            // Set the fragment within the container
            getFragmentManager().beginTransaction().replace(R.id.book_detail_container, detailFragment, DetailFragment.TAG).commit();
        } else {
            // pass the data to the current fragment
            detailFragment.setBookData(title, author, description, rank, listType);
        }

    }
}
