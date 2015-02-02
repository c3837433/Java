package com.example.angessmith.quotes;

import java.io.Serializable;

// Created by AngeSSmith on 11/6/14.

public class QuoteObject implements Serializable {

    // Create the serailizable id so we can save /pass these objects
    private static final long serialVersionUID = 346813153740L;


    // Define the properties
    private String mAuthorName;
    private String mSource;
    private String mQuote;

    // Create the factoru
    public static QuoteObject newInstance(String name, String source, String quote) {
        // set the properties
        QuoteObject newQuote = new QuoteObject();
        newQuote.mAuthorName = name;
        newQuote.mSource = source;
        newQuote.mQuote = quote;
        return newQuote;
    }

    public QuoteObject () {
        mQuote = mAuthorName = mSource = "";
    }

    // Define the getters/setters
    public String getQuote() {
        return mQuote;
    }

    public void setQuote(String mQuote) {
        this.mQuote = mQuote;
    }

    public String getSource() {
        return mSource;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

}
