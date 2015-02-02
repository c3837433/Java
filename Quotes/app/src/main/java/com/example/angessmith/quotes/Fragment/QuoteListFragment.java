package com.example.angessmith.quotes.Fragment;

// Created by AngeSSmith on 11/6/14 for MDF3 1412.

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.angessmith.quotes.DataHelper;
import com.example.angessmith.quotes.QuoteListActivity;
import com.example.angessmith.quotes.QuoteListAdapter;
import com.example.angessmith.quotes.QuoteObject;
import com.example.angessmith.quotes.R;

import java.util.ArrayList;
import java.util.HashMap;

public class QuoteListFragment extends Fragment {
    // Define the variables
    public static final String TAG = "QuoteListFragment.TAG";
    public static final String ARG_QUOTE_LIST = "QuoteListFragment.ARG_QUOTE_LIST";

    // and the list view variables
    public static ArrayList<QuoteObject> mQuoteList;
    public static ArrayList<HashMap<String, Object>> mQuoteHashMapList;
    public static ListView mQuoteListView;
    public static QuoteListAdapter mCustomQuoteListAdapter;
    private ActionMode mActionMode;

    // Define the interface for the listview click
    private QuoteClickListener mOnQuoteClickListener;
    private int mSelectedQuote;
    // efine the listener for the long click
    private QuoteLongClickListener mQuoteLongClickListener;


    // Define the fragment instance
    public static QuoteListFragment newInstance() {
        return new QuoteListFragment();
    }

    // Set the list interface
    public interface QuoteClickListener {
        public void viewQuoteInDetailView (QuoteObject quote, int position);
    }

    public interface QuoteLongClickListener {

    }

    // Set up the contextual action bar for the long click
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Set up the delete menu item
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.delete_action_bar, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        // when deleting
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                // if they selected delete quote
                case R.id.item_delete:
                    // Confirm they want to delete
                    ConfirmDeleteDialogFragment fragment = new ConfirmDeleteDialogFragment();
                    fragment.show(getActivity().getFragmentManager(), ConfirmDeleteDialogFragment.TAG);
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // clear out the action mode
            mActionMode = null;
        }
    };

    // Set up the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the container
        View view = inflater.inflate(R.layout.fragment_quote_list, container, false);
        // Get the listview
        mQuoteListView = (ListView) view.findViewById(R.id.quote_list);
        mQuoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get the selecteted quote
                QuoteObject giftObject = mQuoteList.get(position);
                // set it to the listener
                mOnQuoteClickListener.viewQuoteInDetailView(giftObject, position);
            }
        });

        mQuoteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // see if we have an action mode yet
                if(mActionMode != null){
                    // recycle the current one
                    return false;
                }
                // save the position of the selected cell to delete it
                mSelectedQuote = position;
                // Create a new one
                mActionMode = getActivity().startActionMode(mActionModeCallback);
                return true;
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "Checking adapter");
        if (mCustomQuoteListAdapter == null) {
            Log.d(TAG, "Creating adapter");
            mCustomQuoteListAdapter = new QuoteListAdapter(getActivity().getApplicationContext(), mQuoteHashMapList);
        }            Log.i(TAG, "Loading data  in on activity created");
        mQuoteList = DataHelper.getStoredQuotes(getActivity());
        // update list
        loadListInListView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
         // set the listener
         mOnQuoteClickListener = (QuoteClickListener) activity;
    }

    public static void reloadListWithData (Context context) {
        mQuoteList = DataHelper.getStoredQuotes(context);
    }

    public void loadListInListView() {
        Log.i(TAG, "Creating hashmap from list");
        // Create the hashmap list from the array list
        mQuoteHashMapList = new ArrayList<HashMap<String, Object>>();
        for (QuoteObject quote : mQuoteList) {
            // Create a hash map and add the properties to it
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(QuoteListActivity.ADAPTER_AUTHOR, quote.getAuthorName());
            map.put(QuoteListActivity.ADAPTER_QUOTE, quote.getQuote());
            mQuoteHashMapList.add(map);
        }
        // make sure it is not empty
        if (mQuoteListView != null) {
            Log.i(TAG, "setting map to listview");
            // Set the adapter with the hashmap list to the listview
            mCustomQuoteListAdapter = new QuoteListAdapter(getActivity().getApplicationContext(), mQuoteHashMapList);
            mQuoteListView.setAdapter(mCustomQuoteListAdapter);
        }
    }

    public static void addNewItemtoHashmap () {
        // create the new hashmap list from the array list
        mQuoteHashMapList = new ArrayList<HashMap<String, Object>>();
        for (QuoteObject quote : mQuoteList) {
            // Create a hash map and add the properties to it
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(QuoteListActivity.ADAPTER_AUTHOR, quote.getAuthorName());
            map.put(QuoteListActivity.ADAPTER_QUOTE, quote.getQuote());
            mQuoteHashMapList.add(map);
        }
        // Set the new list to the adapter
        mCustomQuoteListAdapter.setQuoteArrayList(mQuoteHashMapList);
        // reset the adapter
        mQuoteListView.setAdapter(mCustomQuoteListAdapter);
        // Tell the adapter it needs to update the list
        mCustomQuoteListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // set the current list
        Log.i(TAG, "Saving list to bundle");
        savedInstanceState.putSerializable(ARG_QUOTE_LIST, mQuoteList);
        super.onSaveInstanceState(savedInstanceState);
    }

    // when user confirms delete from ling click
    public void deleteSelectedQuoteFromList() {
        Log.d(TAG, "Removint item from lists and updating widget");
        // Remove the selected item in the list
        mQuoteList.remove(mSelectedQuote);
        // update the file
        DataHelper.SaveQuoteListToCache(mQuoteList, getActivity());
        // update the widget
        DataHelper.updateWidget(getActivity());
        // update the listview
        loadListInListView();
    }

}
