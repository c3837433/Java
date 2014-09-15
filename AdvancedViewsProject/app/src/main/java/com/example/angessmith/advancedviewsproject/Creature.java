package com.example.angessmith.advancedviewsproject;

/**
 * Created by AngeSSmith on 9/14/14.
 */
public class Creature {

    // Create the instance of a new creature
    public static Creature newInstance(String _scientific, String _common, int _image) {

        // Set the parameters to the creature
        Creature creature = new Creature();
        creature.setScientificName(_scientific);
        creature.setCommonName(_common);
        creature.setImageId(_image);
        return creature;
    }

    // Create the variables
    private String mScientificName;
    private String mCommonName;
    private int mImageId;

    public Creature() {
        mScientificName = mCommonName = "";
        mImageId = 0;
    }

    // Create the getters and Setters for each field
    public String getScientificName() {
        return mScientificName;
    }

    public void setScientificName(String mScientificName) {
        this.mScientificName = mScientificName;
    }

    public String getCommonName() {
        return mCommonName;
    }

    public void setCommonName(String mCommonName) {
        this.mCommonName = mCommonName;
    }

    public int getImageId() {
        return mImageId;
    }

    public void setImageId(int mImageId) {
        this.mImageId = mImageId;
    }
}
