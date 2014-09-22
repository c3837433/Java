package com.example.angessmith.booklist;

// Created by AngeSSmith on 9/22/14.

public class BestSellersList {

    // Define the variables
    private Number mRank;
    private String mAuthorName;
    private String mTitle;
    private String mDescription;


    public static BestSellersList newInstance(Number _rank, String _authorName, String _title, String _description) {
        // Create the new object
        BestSellersList bestSellersList = new BestSellersList();
        // set the passed in parameters
        bestSellersList.mTitle = _title;
        bestSellersList.mAuthorName = _authorName;
        bestSellersList.mDescription = _description;
        bestSellersList.mRank = _rank;
        // return the bestSellersList
        return bestSellersList;
    }

    public BestSellersList() {
        mDescription = mAuthorName = mTitle = "";
        mRank = 0;
    }

    // Create the getters for the variables
    public Number getRank() {
        return mRank;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public  String getTitle() {
        return mTitle;
    }

    public String  getDescription() {
        return mDescription;
    }
}
