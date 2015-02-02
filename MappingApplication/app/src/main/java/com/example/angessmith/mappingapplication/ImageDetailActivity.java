package com.example.angessmith.mappingapplication;

// Created by AngeSSmith on 11/15/14.

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.angessmith.mappingapplication.Fragment.DetailFragment;


public class ImageDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        Intent intent = getIntent();
        // Make sure it is not empty
        ImageObject imageObject = null;
        if (intent != null) {
            // get the object
            imageObject = (ImageObject) intent.getSerializableExtra(MapViewActivity.IMAGE_OBJECT);
        }
        if (savedInstanceState == null) {
            // inflate the fragment and pass over the object
            DetailFragment fragment = DetailFragment.newInstance(imageObject);
            getFragmentManager().beginTransaction().replace(R.id.detail_container, fragment, DetailFragment.TAG).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
