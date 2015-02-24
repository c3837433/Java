package com.example.angessmith.mediaquotes;

// Created by Angela Smith on 11/20/2014

import java.util.ArrayList;
import java.util.Random;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.dreams.DreamService;
import android.text.Html;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
    Daydream must be present in the Android Daydream menu in the device settings.
    Daydream must show some sort of data or filled out UI. UI cannot be a blank screen.
    Daydream must contain a configuration activity that allows the user to set at least
    one preference that has an effect on the daydream UI.
 */


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)

public class QuoteDaydream extends DreamService {

    private final Random mRandom = new Random();

    TextView mQuoteTextView;
    TextView mAuthorSourceView;
    int mDuration = 10000;
    Handler mQuoteHandler;
    ArrayList<Quote> mArrayList;
    String [] mColorList;


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Exit dream on touch, make fullscreen and bright
        setInteractive(false);
        setFullscreen(true);
        setScreenBright(true);

        // Set the content view and get the textviews
        setContentView(R.layout.quote_daydream);
        mQuoteTextView = (TextView)findViewById(R.id.quote);
        mAuthorSourceView = (TextView)findViewById(R.id.name_source);

        // Get the time duration from the preference
        String timeDuration = getTimeDurationFromPreference();
        if (timeDuration.equals("five")) {
            mDuration = 5000;

        } else if (timeDuration.equals("ten")) {
            mDuration = 10000;

        } else if (timeDuration.equals("thirty")) {
            mDuration = 30000;
        }
        
        // Create the quotes
        mArrayList = new ArrayList<Quote>();
        mArrayList.add(Quote.newInstance("Welcome to the real world. It sucks. You’re gonna love it!", "Friends", "Monica"));
        mArrayList.add(Quote.newInstance("It’s a moo point. It’s like a cow’s opinion; it doesn’t matter. It’s moo.", "Friends", "Joey"));
        mArrayList.add(Quote.newInstance("You could not be any more wrong. You could try, but you would not be successful.", "Friends", "Ross"));
        mArrayList.add(Quote.newInstance("I’m not so good with the advice. Can I interest you in a sarcastic comment?", "Friends", "Chandler"));
        mArrayList.add(Quote.newInstance("Come on Ross, you’re a paleontologist, dig a little deeper.", "Friends", "Phoebe"));
        mArrayList.add(Quote.newInstance("How long do cats live? Like assuming you don’t throw ‘em under a bus or something?", "Friends", "Rachel"));

        mColorList = new String[] {
                // Create an array of android colors
                "#8AD5F0", // Baby blue
                "#D6ADEB", // Bright Ube Purple
                "#C5E26D", // Sulu Green
                "#FFD980", // Salomie (orange)
                "#FF9494", // Light salmon pink
        };

        mQuoteHandler = new Handler();
    }

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();
        // Start the runnable right away
        mQuoteHandler.postDelayed(updateQuote, 0);
    }

    @Override
    public void onDreamingStopped() {
        super.onDreamingStopped();
        // Stop the handler
        mQuoteHandler.removeCallbacks(updateQuote);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private String getTimeDurationFromPreference() {
        // access the preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // if there is one set, get it
        if (preferences.contains("QUOTE_TIME_FREQUENCY")) {
            return preferences.getString("QUOTE_TIME_FREQUENCY", getString(R.string.pref_dream_default));
        } else {
            // otherwise set up the default
            return getString(R.string.pref_dream_default);
        }
    }

    public Runnable updateQuote = new Runnable() {
        public void run() {
            // get a random item
            int randomQuote = mRandom.nextInt(mArrayList.size());
            Quote quote = mArrayList.get(randomQuote);
            // and random color
            int randomColor = mRandom.nextInt(mColorList.length);
            String color = mColorList[randomColor];
            // Convert this color to a string
            int intColor = Color.parseColor(color);
            // Get the background of the layout
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.daydream_layout);
            layout.setBackgroundColor(intColor);

            mQuoteTextView.setText("\"" + quote.getQuote() + "\"");
            // and create the reference
            String reference = "- " + quote.getName() + ", <i>" + quote.getSource() + "<i>";
            mAuthorSourceView.setText(Html.fromHtml(reference));

            // rotate through other quotes and backgrounds based on the users duration preference
            mQuoteHandler.postDelayed(this, mDuration);
        }
    };
}
