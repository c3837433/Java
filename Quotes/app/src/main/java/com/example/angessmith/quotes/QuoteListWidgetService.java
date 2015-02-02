package com.example.angessmith.quotes;

import android.content.Intent;
import android.widget.RemoteViewsService;

// Created by AngeSSmith on 11/7/14.

public class QuoteListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new QuoteListWidgetFactory (getApplicationContext());
    }
}