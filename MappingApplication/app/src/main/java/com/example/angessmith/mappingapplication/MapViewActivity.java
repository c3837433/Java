package com.example.angessmith.mappingapplication;

// Created by AngeSSmith on 11/14/14.

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.angessmith.mappingapplication.Fragment.PictureMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;


public class MapViewActivity extends Activity implements LocationListener, PictureMapFragment.OnMapOpenListener {

    // Define the map properties
    private static final String TAG = "MapViewActivity";
    public static final int ADD_MAP_IMAGE_REQUEST = 34681315;
    public static final int OPEN_LOCATION_SERVICES = 51318643;
    LocationManager mLocManager;
    Location mCurrentLocation;
    public static final String IMAGE_FILE_NAME = "mappedimages.txt";
    public static final String EXTRA_LATITUDE = "MapViewActivity.EXTRA_LATITUDE";
    public static final String EXTRA_LONGITUDE = "MapViewActivity.EXTRA_LONGITUDE";
    public static final String IMAGE_OBJECT = "MapViewActivity.IMAGE_OBJECT";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        // Launch map
        PictureMapFragment frag = new PictureMapFragment();
        getFragmentManager().beginTransaction().replace(R.id.map_container, frag).commit();

        // Start getting users location
        Log.i(TAG, "Starting map, getting location");
        mLocManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        getLocationData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.map_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Make sure user selected add image button
        int id = item.getItemId();
        if (id == R.id.action_add_image) {
            // get the current location and pass it to add to the image
            LatLng newLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            openAddImage(newLatLng);
        }
        return super.onOptionsItemSelected(item);
    }

    private void openAddImage(LatLng latLng) {
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        // Create a new intent to make a new image
        Intent intent = new Intent(this, AddMapImageActivity.class);
        // start the activity
        intent.putExtra(EXTRA_LATITUDE,latitude);
        intent.putExtra(EXTRA_LONGITUDE, longitude);
        startActivityForResult(intent, ADD_MAP_IMAGE_REQUEST);
    }

    public void getLocationData() {
        Log.d(TAG, "Getting device location");
        // make sure the user is able to get gps position
        if (mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d(TAG, "GPS enabled");
            // get the data
            mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
            mCurrentLocation = mLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else if (mLocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.i(TAG, "No GPS, checking network access");
            // check network
            mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, this);
            mCurrentLocation = mLocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } else {
            Log.d(TAG, "Alerting the user");
            alertNeedLocation();
        }
        if(mCurrentLocation != null) {
            Log.d(TAG, "Location data available: Lat: " + mCurrentLocation.getLatitude() + " long: " + mCurrentLocation.getLongitude());
            // pass the properties to the fragment
            PictureMapFragment.setCurrentLocation(mCurrentLocation);
        }
    }

    private void alertNeedLocation() {
        // Create an alert that has the user enable location services in settings
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Location Services Disabled");
        builder.setMessage("Please enable WiFi or GPS services in Location Settings.");
        builder.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(locationIntent, OPEN_LOCATION_SERVICES);
            }
        });
        builder.create().show();
    }
    private void addMarkersToMap(ArrayList<ImageObject> arrayList) {
        // make sure we have a valid map
        if (PictureMapFragment.mImagesMap != null) {
            Log.i(TAG, "Map is not null");
            // clear the map in case there are already pins on it
            PictureMapFragment.mImagesMap.clear();
            // create a new hashmap so we can store the created object with the marker
            PictureMapFragment.mImagesHashMap = new HashMap<Marker, ImageObject>();
            // create markers for all the objects
            for (ImageObject object : arrayList) {
                Marker marker = PictureMapFragment.mImagesMap.addMarker(new MarkerOptions()
                        .position(new LatLng(object.getImageLatitude(), object.getImageLongitude()))
                        .title(object.getImageName())
                        .snippet(object.getImageLocation())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                // add the marker and the object to the hashmap
                PictureMapFragment.mImagesHashMap.put(marker, object);
            }

            // Add the current location marker
            PictureMapFragment.mImagesMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 12));
            // add the marker for this location
            PictureMapFragment.mImagesMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                    .title("You are")
                    .snippet("here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        }
    }

    // When user returns from add activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Return from activity");
        // See if anything was returned with it
        if (resultCode == RESULT_OK && requestCode == ADD_MAP_IMAGE_REQUEST) {
            Log.i(TAG, "receiving data from add image");
            // get the new list
            ArrayList<ImageObject> imageList = StoredImageData.getStoredImages(this);
            addMarkersToMap(imageList);
        }
        else if (requestCode == OPEN_LOCATION_SERVICES){
            Log.d(TAG, "Returning from settings");
            // Check the location services again
           getLocationData();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        // update location properties
        PictureMapFragment.setCurrentLocation(mCurrentLocation);
        // update the map
        ArrayList<ImageObject> imageList = StoredImageData.getStoredImages(this);
        addMarkersToMap(imageList);
        Log.i(TAG, "Location Changed");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(TAG, "Status Changed");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(TAG, "Provider Enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(TAG, "Provider disabled");
    }

    @Override
    public void OpenAddItemActivity(LatLng latLng) {
        // pass the location to the add activity
        openAddImage(latLng);
    }

    @Override
    public void OpenImageDetailActivity(ImageObject object) {
        Intent intent = new Intent(this, ImageDetailActivity.class);
        // add the object to a bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable(IMAGE_OBJECT, object);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    protected void onPause() {
        super.onPause();
        // Stop getting location data
        mLocManager.removeUpdates(this);
    }

}
