package com.example.angessmith.fundamentalsapp.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


//Created by AngeSSmith on 9/30/14 for Java 2 Term 1410.
// Book (Animal) best seller api http://api.nytimes.com/svc/books/v2/lists.json?list-name=animals&api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529


public class BookListFragment extends android.app.ListFragment {

    // Create the TAG
    public static final String TAG = "BookListFragment.TAG";

    // Create a factory instance of the fragment
    private static BookListFragment newInstance() {
        // Create a new instance of the fragment
        BookListFragment fragment = new BookListFragment();
        // and return it
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: GET THE LIST OF BOOK OBJECTS, CREATE ARRAY OF NAMES


        // TODO: PUT ITEMS IN ARRAY ADAPTER
    }

    @Override
    public void onListItemClick(ListView _listview, View _view, int _position, long _id) {

        // TODO: GET THE ITEM AT POSITION AND SET THAT ITEM IN THE DETAIL FRAGMENT
    }


}
