package com.example.angessmith.fundamentalsapp;

import java.io.Serializable;

//Created by AngeSSmith on 9/30/14 for Java 2 Term 1410.

public class Book implements Serializable {
    // Add requirements for serialization
    private static final long serialVersionUID = 264352789266376484L;

    // Define the variables
    private int mRank;
    private String mTitle;
    private String mAuthor;
    private String mDescription;

    // Create a new instance factory method
    public static Book newInstance(int _rank, String _title, String _author, String _description) {
        // Create the new object
        Book book = new Book();
        // and set the variables
        book.mRank = _rank;
        book.mTitle = _title;
        book.mAuthor = _author;
        book.mDescription = _description;
        // return the book
        return book;
    }

    public Book() {
        mAuthor = mDescription = mTitle = "";
        mRank = 0;
    }

    //Create the getters for each value
     public int getRank() {
         return mRank;
     }
    public String getTitle () {
        return mTitle;
    }
    public String getAuthor () {
        return mAuthor;
    }
    public String getDescription () {
        return mDescription;
    }

    // Set the title in the listview
    @Override
    public String toString() {
        return mTitle;
    }
}
