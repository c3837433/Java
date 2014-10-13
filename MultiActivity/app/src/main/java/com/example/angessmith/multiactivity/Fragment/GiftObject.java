package com.example.angessmith.multiactivity.Fragment;


// Created by AngeSSmith on 10/12/14 for Java 2 Term 1410.

import java.io.Serializable;

public class GiftObject implements Serializable {

    // Create a unique identifier
    private static final long serialVersionUID = 264352789266376484L;

    // Define the variables
    private String mName;
    private String mLocation;
    private String mPrice;
    private String mUrl;

    // Define new instance factory method
    public static GiftObject newInstance(String _name, String _location, String _price, String _url) {
        GiftObject gift = new GiftObject();
        gift.mName = _name;
        gift.mLocation = _location;
        gift.mPrice = _price;
        gift.mUrl = _url;
        return gift;
    }

    public GiftObject() {
        mLocation = mName = mUrl = mPrice = "";
        //mPrice = 0;
    }

    // Define the getters and setters
    public String getName() {
        return mName;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getPrice() {
        return mPrice;
    }

    public String getUrl() {
        return mUrl;
    }

    // Set the gift name in the list view
    @Override
    public String toString() {
        return mName;
    }
}
