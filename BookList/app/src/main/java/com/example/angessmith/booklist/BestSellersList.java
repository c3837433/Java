package com.example.angessmith.booklist;

// Created by AngeSSmith on 9/22/14.

public class BestSellersList  {

    // Define the variables
    private Number mRank;
    private String mAuthorName;
    private String mTitle;
    private String mDescription;
    private String mISBN;
    private String mListName;


    public static BestSellersList newInstance(Number _rank, String _authorName, String _title, String _description, String _isbn, String _listName) {
        // Create the new object
        BestSellersList bestSellersList = new BestSellersList();
        // set the passed in parameters
        bestSellersList.mTitle = _title;
        bestSellersList.mAuthorName = _authorName;
        bestSellersList.mDescription = _description;
        bestSellersList.mRank = _rank;
        bestSellersList.mISBN = _isbn;
        bestSellersList.mListName = _listName;
        // return the bestSellersList
        return bestSellersList;
    }

    public BestSellersList() {
        mDescription = mAuthorName = mTitle = mISBN = mListName = "";
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

    public String getISBN() {
        return mISBN;
    }

    public String getListName() {
        return mListName;
    }

}
