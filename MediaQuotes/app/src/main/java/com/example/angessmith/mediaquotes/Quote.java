package com.example.angessmith.mediaquotes;


 // Created by AngeSSmith on 11/20/14.

public class Quote {

    // Create the properties
    private String mQuote;
    private String mSource;
    private String mName;


    public static Quote newInstance(String _quote, String _source, String _name) {
        // set the properties
        Quote quote = new Quote();
        quote.mQuote = _quote;
        quote.mSource = _source;
        quote.mName = _name;
        return quote;
    }


    public Quote () {
        mQuote = mName = mSource = "";
    }

    // And the getters for the daydream
    public String getQuote() {
        return mQuote;
    }

    public String getSource() {
        return mSource;
    }

    public String getName() {
        return mName;
    }

}
