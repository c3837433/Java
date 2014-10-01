package com.example.angessmith.fundamentalsapp;


// Created by AngeSSmith on 9/30/14 for Java 2 term 1410.

import java.io.File;
import java.io.Serializable;

public class BestSellerList implements Serializable {

    // Add id for serialization
    private static final long serialVersionUID =  484763662987253462L;

    // Define the variables
    private String mDisplayName;
    private String mEncodedName;
    private String mDataString;

    public static BestSellerList newInstance(String __displayName, String _encodedName) {
        // Create a new best seller list object
        BestSellerList bestSellerList = new BestSellerList();
        bestSellerList.mDisplayName = __displayName;
        bestSellerList.mEncodedName = _encodedName;
        // return the list
        return bestSellerList;
    }

    public BestSellerList() {
        mEncodedName = mDisplayName = mDataString = "";
    }

    // build the getter for the encoded name to pass to the next call
    public String getEncodedName() {
        return mEncodedName;
    }
    public void setDataString(String _dataString) {
        mDataString = _dataString;
    }

    public String getDataString() {
        return mDataString;
    }
    // Set the display name to the spinner
    @Override
    public String toString() {
        return mDisplayName;
    }

}
