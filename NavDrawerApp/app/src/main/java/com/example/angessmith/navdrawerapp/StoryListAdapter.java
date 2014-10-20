package com.example.angessmith.navdrawerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AngeSSmith on 10/20/14.
 */
public class StoryListAdapter extends BaseAdapter {

    //Define the context
    private Context mContext;

    // Create an arraylist to hold the hashmap
    private ArrayList<HashMap<String, Object>> mStoryList = new ArrayList<HashMap<String, Object>>();
    // Create the hashmap
    public static HashMap<String, Object> mStory;

    // Set the adapter
    public StoryListAdapter(Context _context, ArrayList<HashMap<String, Object>> _storyDataArray) {
        mContext = _context;
        mStoryList = _storyDataArray;
    }

    @Override
    public int getCount() {
        // get the number of items in the array
        return mStoryList.size();
    }

    @Override
    public Object getItem(int position) {
        // find the item at the position
        return mStoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // get the id of the item
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Make sure there are no views that can be recycled
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.story_list_item, parent, false);
        }

        // Set this story object and it's position
        mStory = new HashMap<String, Object>();
        mStory = mStoryList.get(position);

        // Create the views
        TextView titleView = (TextView)convertView.findViewById(R.id.story_time);
        TextView timeView = (TextView)convertView.findViewById(R.id.story_time);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.story_image);
        // Set the story in the list cells
        titleView.setText((String)mStory.get(Main.STORY_TITLE));
        timeView.setText((String)mStory.get(Main.STORY_TIME_STAMP));
        imageView.setImageResource((Integer)mStory.get(Main.STORY_IMAGE));
        return convertView;
    }
}
