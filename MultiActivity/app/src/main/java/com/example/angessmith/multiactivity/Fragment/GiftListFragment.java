package com.example.angessmith.multiactivity.Fragment;
// Created by AngeSSmith on 10/12/14 for Java 2 Week 3 Term 1410.
import android.app.Activity;
import android.app.Fragment;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.angessmith.multiactivity.MainListActivity;
import com.example.angessmith.multiactivity.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

public class GiftListFragment extends Fragment {
    // create fragment tag
    public static final String TAG = "GiftListFragment.TAG";
    public static final String ARG_GIFTLIST = "GiftListFragment.ARG_GIFTLIST";

    // Define properties for the listview
    public static ArrayList<GiftObject> mGifts;
    public static ListView mListview;
    public static ArrayAdapter<GiftObject> mArrayAdapter;
    private ActionMode mActionMode;
    private int mItemClicked;

    // Set the interface variable
    private OnGiftItemLongClickListener mOnGiftItemLongClickListener;
    private OnGiftItemClickListener mOnGiftItemClickListener;

    // Create the factory
    public static GiftListFragment newInstance() {
        return new GiftListFragment();
    }

    // SET UP THE FRAGMENT LAYOUT
    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        // Get the view
        View view = _inflater.inflate(R.layout.gift_list_fragment, _container, false);
        // get the listview
        ListView giftList = (ListView) view.findViewById(R.id.giftlist);
        // Add it to the array
        giftList.setAdapter(mArrayAdapter);
        // Set up the long click listener to call the action mode
        giftList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // see if an action mode already exists
                if(mActionMode != null){
                    // if it does, the user can interact with the one previously created
                    return false;
                }
                mItemClicked = position;
                // If one doesn't exist, get the activity and start the action mode
                mActionMode = getActivity().startActionMode(mActionModeCallback);
                return true;
            }
        });

        giftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "User Clicked List");
                // Get the object that was selected
                GiftObject giftObject = mGifts.get(position);
                Log.i(TAG, "User Clicked gift: " + giftObject);
                // Attach it to the listener to open in the view
                mOnGiftItemClickListener.openGiftInDetailView(giftObject, position);
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if the state was saved
        if (savedInstanceState != null) {
            // get the values back
            mGifts = (ArrayList<GiftObject>) savedInstanceState.getSerializable(ARG_GIFTLIST);
            mArrayAdapter = new ArrayAdapter<GiftObject>(getActivity(), android.R.layout.simple_list_item_1, (mGifts));
            //Log.i(TAG, "Gift Data from onCreate: " + mGifts);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Update the listview with the list items
            SetGiftsInList();
        }
        else {
            // check if we have a saved list offline
            File external = getActivity().getExternalFilesDir(null);
            File file = new File(external, MainListActivity.CACHE_FILE);
            boolean fileExists = new File(external, MainListActivity.CACHE_FILE).exists();
            if (fileExists)
            {
                try {
                    // create a new imput stream
                    FileInputStream fileInputStream = new FileInputStream(file);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    // Get the objects from the file
                    mGifts = (ArrayList<GiftObject>) objectInputStream.readObject();
                    // Close the stream
                    objectInputStream.close();
                }
                catch (FileNotFoundException exception) {
                    Log.e(TAG, "Input Stream Exception: ", exception);
                }
                catch (StreamCorruptedException exception) {
                    Log.e(TAG, "Stream Exception: ", exception);
                }
                catch (IOException exception) {
                    Log.e(TAG, "IO Exception: ", exception);
                }
                catch (ClassNotFoundException exception) {
                    Log.e(TAG, "Class Exception: ", exception);
                }
            }

            else {
                mGifts = new ArrayList<GiftObject>();
            }
            //Log.i(TAG, "Creating new arraylist for gift data in onActivityCreated ");
            // Get the listview, and create and set the adapter to it
            SetGiftsInList();
        }
    }


    private void SetGiftsInList() {
        mListview =  (ListView) getView().findViewById(R.id.giftlist);
        //mListview.setOnItemClickListener(this);
        mArrayAdapter = new ArrayAdapter<GiftObject>(getActivity(), android.R.layout.simple_list_item_1, (mGifts));
        mListview.setAdapter(mArrayAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Attach the activity to the correct listeners
        if (activity instanceof OnGiftItemLongClickListener) {
            // set the listener
            mOnGiftItemLongClickListener = (OnGiftItemLongClickListener) activity;
        }
        if (activity instanceof OnGiftItemClickListener) {
            mOnGiftItemClickListener = (OnGiftItemClickListener) activity;
        }
    }

    public interface OnGiftItemLongClickListener {

    }

    // Define the list item interface
    public interface OnGiftItemClickListener {
        public void openGiftInDetailView(GiftObject object, int position);
    }

    // Define the contextual action bar callback for the list on long click
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        // When created, get the menu
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Get the menu, inflate it, return it
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.item_delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        // When the user clicks on the delete button
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // Make sure they clicked on the delete button
            switch (item.getItemId()) {
                case R.id.item_delete:
                    // Create the dialog fragment
                    AlertDialogFragment fragment = new AlertDialogFragment();
                    // Pass the name of the item to the fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("item", mGifts.get(mItemClicked).getName());
                    fragment.setArguments(bundle);
                    fragment.show(getActivity().getFragmentManager(), AlertDialogFragment.TAG);
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // Destroy the action mode so it can be used again
            mActionMode = null;
        }
    };

    // Define the interface method when user deletes an item
    public void onConfirmDeleteItem() {
        // Remove the selected item in the list
        mArrayAdapter.remove(mGifts.get(mItemClicked));
        // update the adapter
        mArrayAdapter.notifyDataSetChanged();
    }

    // SAVE THE ARRAY LIST WHEN NECESSARY
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // set the current values to the bundle
        savedInstanceState.putSerializable(ARG_GIFTLIST, mGifts);
        Log.i(TAG, "Saving gift data: " + mGifts);
        // save the state
        super.onSaveInstanceState(savedInstanceState);
    }

}
