package com.example.angessmith.tabbednavigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

 // Created by AngeSSmith on 10/21/14.

public class WeeklyListviewAdapter  extends BaseAdapter {

    // Define the properties
    private Context mContext;
    public ArrayList<HashMap<String, Object>> mWeeklyList = new ArrayList<HashMap<String, Object>>();
    public static HashMap<String, Object> mWeeklyForecast;

    // Build the factory
    public WeeklyListviewAdapter(Context context, ArrayList<HashMap<String, Object>> _listData) {
        mContext = context;
        mWeeklyList = _listData;
    }

    // Set the number of items in the list
    @Override
    public int getCount() {
        return mWeeklyList.size();
    }

    // Find the object at the list position
    @Override
    public Object getItem(int position) {
        return mWeeklyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // recycle view if possible
        if (convertView == null) {
            // create one
            convertView = LayoutInflater.from(mContext).inflate(R.layout.weekly_list, parent, false);
        }

        // Create the map
        mWeeklyForecast = new HashMap<String, Object>();
        // set the map at the right position
        mWeeklyForecast = mWeeklyList.get(position);

        // get the textviews
        TextView dayView = (TextView)convertView.findViewById(R.id.weekly_day);
        TextView descriptionView = (TextView)convertView.findViewById(R.id.weekly_description);

        // Set their values
        dayView.setText((String)mWeeklyForecast.get(MainActivity.WEEKLY_DAY));
        descriptionView.setText((String)mWeeklyForecast.get(MainActivity.WEEKLY_DESCRIPTION));
        return convertView;
    }
}