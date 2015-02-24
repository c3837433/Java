package com.example.angessmith.quotes;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

 // Created by AngeSSmith on 11/11/14.

public class DataHelper {

    public static final String TAG = "DataHelper.TAG";
    public static final String QUOTES_CACHED_FILE = "quotes.txt";

    public static ArrayList<QuoteObject> getStoredQuotes (Context context) {
        ArrayList<QuoteObject> mQuotesArray = new ArrayList<QuoteObject>();
        File external = context.getExternalFilesDir(null);
        // see if we can pull data from the file
        boolean haveStoredData = new File(external, QUOTES_CACHED_FILE).exists();
        if (haveStoredData) {
            Log.d(TAG, "Pulling stored data from file");
            // We have offline data
            try {
                // Get the file
                File file = new File(external, QUOTES_CACHED_FILE);
                // create the input streams
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                // Get the objects from the file
                mQuotesArray = (ArrayList<QuoteObject>) objectInputStream.readObject();
                // Close the stream
                objectInputStream.close();
            }
            catch (FileNotFoundException e) {
                Log.e(TAG, "File Not Found: ", e);
            }
            catch (StreamCorruptedException e) {
                Log.e(TAG, "Stream Corrupted: ", e);
            }
            catch (IOException e) {
                Log.e(TAG, "IO exception: ", e);
            }
            catch (ClassNotFoundException e) {
                Log.e(TAG, "Class Not Found: ", e);
            }
        }

        return mQuotesArray;
    }

    public static void SaveQuoteListToCache (ArrayList<QuoteObject> arrayList, Context context) {
        // Get the directory
        File filesDir = context.getExternalFilesDir(null);
        File file = new File(filesDir, QUOTES_CACHED_FILE);
        try {
            // make a new output stream
            FileOutputStream fOS = new FileOutputStream(file);
            ObjectOutputStream oS = new ObjectOutputStream(fOS);
            // write the quote list to the stream
            Log.i(TAG, "Writing new list to file");
            oS.writeObject(arrayList);
            // close the stream
            oS.close();
        }
        catch(Exception exception) {
            // If it can't write
            Log.e(TAG, "Output exemption, unable to write file: ", exception);
        }
    }

    public static void UpdateListWithNewItem (QuoteObject object, Context context) {
        // get the current list
        ArrayList<QuoteObject> arrayList = getStoredQuotes(context);
        // add the new item
        arrayList.add(object);
        // save this item back to the list
        SaveQuoteListToCache(arrayList, context);
        // then update the widget
        updateWidget(context);
    }

    public static void UpdateListByRemovingItem (int position, Context context) {
        // get the current list
        ArrayList<QuoteObject> arrayList = getStoredQuotes(context);
        // remove the selected item
        arrayList.remove(position);
        // save this item back to the list
        SaveQuoteListToCache(arrayList, context);
        // then update the widget
        updateWidget(context);
    }


    public static void updateWidget (Context context) {
        // then update the widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        // get a list of all active widgets
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, QuoteWidgetProvider.class));
        if (allWidgetIds.length != 0) {
            // if there are active widgets, update them
            new QuoteWidgetProvider().onUpdate(context, appWidgetManager, allWidgetIds);
        }
    }
}
