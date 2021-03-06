package com.example.angessmith.booklist;


// Created by Angela Smith on 9/19/14.

public class BookList {

    // Define the variables
    private String mDisplayName;
    private String mEncodedName;

    public static BookList newInstance(String __displayName, String _encodedName) {
        // Create the new object
        BookList bookList = new BookList();
        // set the passed in parameters
        bookList.mDisplayName = __displayName;
        bookList.mEncodedName = _encodedName;
        // return the book list
        return bookList;
    }

    public BookList() {
        mEncodedName = mDisplayName = "";
    }

    // Create the getters for the encoded name
    public String getEncodedName() {
        return mEncodedName;
    }

    // Set the display name within the spinner views
    @Override
    public String toString() {
        return mDisplayName;
    }



}


