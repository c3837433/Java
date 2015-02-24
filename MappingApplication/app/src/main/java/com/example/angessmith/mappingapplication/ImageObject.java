package com.example.angessmith.mappingapplication;

import java.io.Serializable;

 // Created by AngeSSmith on 11/15/14.

public class ImageObject implements Serializable {
    
    private static final long serialVersionUID = 346813153740L;

    // Define the properties
    private String mUriString;
    private String mImageName;
    private String mImageLocation;
    private Double mImageLatitude;
    private Double mImageLongitude;

    // Define the factory
    public static ImageObject newInstance (String uriString, String title, String location, Double latitude, Double longitude) {
        // set the properties
        ImageObject newImage = new ImageObject();
        newImage.mUriString = uriString;
        newImage.mImageName = title;
        newImage.mImageLocation = location;
        newImage.mImageLatitude = latitude;
        newImage.mImageLongitude = longitude;
        return newImage;
    }

    public ImageObject () {
        mImageLocation = mUriString = mImageName = "";
        mImageLongitude = mImageLatitude = (double) 0;
    }

    public String getImageLocation() {
        return mImageLocation;
    }


    public String getImageName() {
        return mImageName;
    }


    public String getUriString() {
        return mUriString;
    }


    public Double getImageLatitude() {
        return mImageLatitude;
    }

    public Double getImageLongitude() {
        return mImageLongitude;
    }


}
