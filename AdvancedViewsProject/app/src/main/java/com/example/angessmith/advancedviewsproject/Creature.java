package com.example.angessmith.advancedviewsproject;
// Created by AngeSSmith on 9/14/14 for Java 1 Week 3 Term 1409.

public class Creature  {

    // Create the variables
    private String mScientificName;
    private String mCommonName;
    private int mImageId;


    // Use a factory to create the instance of a new creature
    public static Creature newInstance(String _scientific, String _common, int _image) {
        //Create the creature object
        Creature creature = new Creature();
        // Set the parameters to the creature
        creature.setScientificName(_scientific);
        creature.setCommonName(_common);
        creature.setImageId(_image);
        // Return the new creature object
        return creature;
    }


    public Creature() {
        mScientificName = mCommonName = "";
        mImageId = 0;
    }

    // Create the getters and Setters for the scientific name
    public String getScientificName() {
        return mScientificName;
    }

    public void setScientificName(String mScientificName) {
        this.mScientificName = mScientificName;
    }
    // Create the getters and Setters for the common name
    public String getCommonName() {
        return mCommonName;
    }

    public void setCommonName(String mCommonName) {
        this.mCommonName = mCommonName;
    }
    // Create the getters and Setters for the image id
    public int getImageId() {
        return mImageId;
    }

    public void setImageId(int mImageId) {
        this.mImageId = mImageId;
    }
}
