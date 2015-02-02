package com.example.angessmith.mappingapplication.Fragment;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.angessmith.mappingapplication.ImageObject;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

// Created by AngeSSmith on 11/15/14.

public class PictureMapFragment extends MapFragment implements  GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener {

    public static final String TAG = "PictureMapFragment";
    public static GoogleMap mImagesMap;
    public static Location mLocation;
    private OnMapOpenListener mOnMapOpenListener;
    public static HashMap<Marker, ImageObject> mImagesHashMap;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Get the map and set the listeners

        Log.i(TAG, "Map Fragment Created");
        mImagesMap = getMap();
        // Set the listeners
        mImagesMap.setOnInfoWindowClickListener(this);
        mImagesMap.setOnMapClickListener(this);
        mImagesMap.setOnMapLongClickListener(this);
        mImagesMap.setInfoWindowAdapter(new CustomMarkerAdapter());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Attach the listener
        if (activity instanceof OnMapOpenListener) {
            mOnMapOpenListener = (OnMapOpenListener) activity;
        }
    }

    public static void setCurrentLocation(Location location) {
        Log.i(TAG, "Map Fragment setting current location and pin");
        // Get the passed location
        mLocation = new Location("");
        mLocation = location;
        Log.i(TAG, "Current location = " + location);
        // and zoom the map in to the current location
        if (mImagesMap != null) {
            mImagesMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
            // add the marker for this location
            mImagesMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title("You are")
                    .snippet("here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.i(TAG, "User long clicked at latlng:" + latLng);
        mOnMapOpenListener.OpenAddItemActivity(latLng);
    }

    // Define the activity interface for when user long clicks the map
    public interface OnMapOpenListener {
        public void OpenAddItemActivity(LatLng latLng);
        public void OpenImageDetailActivity(ImageObject object);
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        // Get the object from the hashmap for this marker
        ImageObject imageObject = mImagesHashMap.get(marker);
        // make sure they didn't select the current location marker
        if (imageObject != null) {
            Log.i(TAG, "User selected: " + imageObject.getImageName());
            // Pass the object to the detail view
            mOnMapOpenListener.OpenImageDetailActivity(imageObject);
        } else {
            Log.i(TAG, "Unable to find marker");
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        Log.i(TAG, "User selected latlng:" + latLng);

    }


    private class CustomMarkerAdapter implements GoogleMap.InfoWindowAdapter {

        TextView mWindwoTextView;

        public CustomMarkerAdapter() {
            mWindwoTextView = new TextView(getActivity());
        }

        @Override
        public View getInfoContents(Marker marker) {
            mWindwoTextView.setText(marker.getTitle() + " @ " + marker.getSnippet());
            return mWindwoTextView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }
    }
}
