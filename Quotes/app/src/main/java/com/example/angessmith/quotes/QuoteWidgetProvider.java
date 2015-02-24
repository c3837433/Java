package com.example.angessmith.quotes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

// Created by AngeSSmith on 11/7/14.

public class QuoteWidgetProvider extends AppWidgetProvider {

    // create strings
    public static final String ACTION_ADD_QUOTE = "com.example.angessmith.ACTION_ADD_QUOTE";
    public static final String ACTION_VIEW_QUOTE_DETAILS = "com.example.angessmith.ACTION_VIEW_QUOTE_DETAILS";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Loop through any widgets the user has from our app
        for (int i = 0; i < appWidgetIds.length; i++) {
            // Update the widget
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.widget_quote_list);

            // Create an intent to access the service
            Intent intent = new Intent(context, QuoteListWidgetService.class);
            // add the id to the intent
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            // Access the widget layout
            RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            // Set up the list view and the empty text field
            widgetView.setRemoteAdapter(R.id.widget_quote_list, intent);
            widgetView.setEmptyView(R.id.widget_quote_list, R.id.empty);

            // Set up the intent to view quote details
            Intent viewDetailIntent = new Intent(ACTION_VIEW_QUOTE_DETAILS);
            PendingIntent detailPendingIntent = PendingIntent.getBroadcast(context, 0, viewDetailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            widgetView.setPendingIntentTemplate(R.id.widget_quote_list, detailPendingIntent);

            // Set up the intent for the add button
            Intent addItemIntent = new Intent(ACTION_ADD_QUOTE);
            // add the id to the intent
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            PendingIntent addPendingIntent = PendingIntent.getBroadcast(context, 0, addItemIntent, 0);
            // add the intent to the button
            widgetView.setOnClickPendingIntent(R.id.launch_add_activity, addPendingIntent);
            // update the widget
            appWidgetManager.updateAppWidget(appWidgetIds[i], widgetView);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    // prepare for specific actions
    @Override
    public void onReceive(Context context, Intent intent) {
        // see which action was triggered

        if (intent.getAction().equals(ACTION_ADD_QUOTE)) {
            // Open the list view to access the intent for resuts
            // Launch the add activity
            Intent addIntent = new Intent(context, QuoteAddActivity.class);
            addIntent.putExtra(QuoteAddActivity.WIDGET_LAUNCH, true);
            // set up intent as new task that RETURNS to the widget
            addIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            // start the intent
            context.startActivity(addIntent);
        }

        if (intent.getAction().equals(ACTION_VIEW_QUOTE_DETAILS)){
            // get the item that was sent over
            QuoteObject quote = (QuoteObject)intent.getSerializableExtra(QuoteDetailActivity.EXTRA_SELECTED_QUOTE);
            if (quote != null) {
                // start the detail activity
                Intent detailIntent = new Intent(context, QuoteDetailActivity.class);
                // set up intent as new task (clear any previous) and return to the widget
                detailIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                // Add the object, position, and widget value to the activity
                detailIntent.putExtra(QuoteDetailActivity.EXTRA_SELECTED_QUOTE, quote);
                detailIntent.putExtra(QuoteDetailActivity.EXTRA_SELECTED_POSITION, intent.getIntExtra(QuoteDetailActivity.EXTRA_SELECTED_POSITION, -1));
                detailIntent.putExtra(QuoteDetailActivity.WIDGET_LAUNCH, true);
                context.startActivity(detailIntent);
            }
        }
        super.onReceive(context, intent);
    }
}
