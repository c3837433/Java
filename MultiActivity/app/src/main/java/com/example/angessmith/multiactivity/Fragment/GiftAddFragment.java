package com.example.angessmith.multiactivity.Fragment;
// Created by AngeSSmith on 10/12/14 for Java 2 Term 1410.
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.angessmith.multiactivity.GiftAddActivity;
import com.example.angessmith.multiactivity.R;

public class GiftAddFragment extends Fragment  {

    // CREATE TAG
    public static final String TAG = "GiftAddFragment.TAG";

    EditText mTitleTextView;
    EditText mLocTextView;
    EditText mPriceTextView;
    EditText mUrlTextView;

    // CREATE THE LISTENER
    //private OnSaveItemListener mOnSaveItemListener;

    // CREATE FACTORY
    public static GiftAddFragment newInstance() {
        return new GiftAddFragment();
    }

    // SET UP THE FRAGMENT LAYOUT
    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        // Return the fragment
        return _inflater.inflate(R.layout.gift_add_fragment, _container, false);
    }

    // Add the listener to the view
    public void  onAttach(Activity activity) {
        super.onAttach(activity);
      //  mOnSaveItemListener = (OnSaveItemListener) activity;
    }

    /*
    @Override
    public void onClick(View v) {
        // Get the values out of the EditText views and send them to the activity
        mTitleTextView   = (EditText) getView().findViewById(R.id.item_title_textView);
        String itemName = mTitleTextView.getText().toString();
        mLocTextView   = (EditText) getView().findViewById(R.id.item_location_textView);
        String itemLocation = mLocTextView.getText().toString();
        mPriceTextView   = (EditText) getView().findViewById(R.id.item_price_textView);
        String itemPrice = (mPriceTextView.getText().toString());
        mUrlTextView   = (EditText) getView().findViewById(R.id.item_url_textView);
        String itemUrl = mUrlTextView.getText().toString();

        // Make sure we at least have a title
        if (itemName != null)
        {
            //mOnSaveItemListener.SaveItemsToList(itemName, itemLocation, itemPrice, itemUrl);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Missing Name");
            builder.setMessage("This item needs a name to be saved.");
            builder.show();
        }
    }
    */
    public Object onSaveItemClick () {
        mTitleTextView   = (EditText) getView().findViewById(R.id.item_title_textView);
        String itemName = mTitleTextView.getText().toString();
        mLocTextView   = (EditText) getView().findViewById(R.id.item_location_textView);
        String itemLocation = mLocTextView.getText().toString();
        mPriceTextView   = (EditText) getView().findViewById(R.id.item_price_textView);
        String itemPrice = (mPriceTextView.getText().toString());
        mUrlTextView   = (EditText) getView().findViewById(R.id.item_url_textView);
        String itemUrl = mUrlTextView.getText().toString();

        // Make sure we at least have a title
        if (!itemName.equals(""))
        {
            //GiftAddActivity.SaveItemsToList(itemName, itemLocation, itemPrice, itemUrl);
            return GiftObject.newInstance(itemName, itemLocation, itemPrice, itemUrl);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Missing Name");
            builder.setMessage("This item needs a name to be saved.");
            builder.show();
            return null;
        }
    }
    /*
    // Create the interface
    public interface OnSaveItemListener {
        public void SaveItemsToList(String name, String location, String price, String url);
    }
*/
    // set up the listener to the button
    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        //Button button = (Button) getView().findViewById(R.id.save_gift_button);
        //button.setOnClickListener(this);
    }

}
