package com.example.angessmith.quotes;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import java.util.ArrayList;

 // Created by AngeSSmith on 11/7/14.

public class QuoteListWidgetFactory  implements RemoteViewsFactory {

    private ArrayList<QuoteObject> mQuotesArray;
    private Context mContext;

    public QuoteListWidgetFactory (Context context) {
        mContext = context;
    }
    @Override
    public void onCreate() {
        // Get the current list
        mQuotesArray = DataHelper.getStoredQuotes(mContext);

    }

    @Override
    public void onDataSetChanged() {
        // Get the current list
        mQuotesArray = DataHelper.getStoredQuotes(mContext);
    }

    @Override
    public void onDestroy() {
        // empty the list
        mQuotesArray.clear();
    }

    @Override
    public int getCount() {
        // get the number of items in the array
        return mQuotesArray.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // Get the objects
        QuoteObject quote = mQuotesArray.get(position);
        // get the item view
        RemoteViews quoteView = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        // set the views
        quoteView.setTextViewText(R.id.widget_item_quote, "\"" + quote.getQuote() + "\"");
        // create a string of the reference
        String sourceString = "- " + quote.getAuthorName() + ", <i>" + quote.getSource() + "<i>";
        quoteView.setTextViewText(R.id.widget_item_reference,Html.fromHtml(sourceString));
        // Set the intent to the item
        Intent intent = new Intent();
        // pass the object and it's position
        intent.putExtra(QuoteDetailActivity.EXTRA_SELECTED_QUOTE, quote);
        intent.putExtra(QuoteDetailActivity.EXTRA_SELECTED_POSITION, position);
        quoteView.setOnClickFillInIntent(R.id.widget_quote_list_item, intent);
        return quoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        // don't want an empty list view
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
