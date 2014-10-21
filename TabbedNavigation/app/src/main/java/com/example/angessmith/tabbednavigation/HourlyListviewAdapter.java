package com.example.angessmith.tabbednavigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

// Created by AngeSSmith on 10/20/14.

public class HourlyListviewAdapter extends BaseAdapter {

    // set the context
    private Context mContext;

    // Define the arraylist and hashmap
    public ArrayList<HashMap<String, Object>> mHourlyList = new ArrayList<HashMap<String, Object>>();
    public static HashMap<String, Object> mHourlyForecast;

    // Create the factory
    public HourlyListviewAdapter(Context context, ArrayList<HashMap<String, Object>> _listData) {
        mContext = context;
        mHourlyList = _listData;
    }

    @Override
    public int getCount() {
        return mHourlyList.size();
    }

    @Override
    public Object getItem(int position) {
        return mHourlyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // See if we can recycle the view
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.hourly_list, parent, false);
        }

        // set the map to the position of the list
        mHourlyForecast = new HashMap<String, Object>();
        mHourlyForecast = mHourlyList.get(position);
        // get the views
        TextView hourView = (TextView)convertView.findViewById(R.id.hourly_hour);
        TextView conditionView = (TextView)convertView.findViewById(R.id.hourly_condition);
        TextView tempView = (TextView)convertView.findViewById(R.id.hourly_temp);
        TextView feelsView = (TextView)convertView.findViewById(R.id.hourly_feels);
        // get the values from the
        hourView.setText((String)mHourlyForecast.get(MainActivity.HOURLY_TIME));
        conditionView.setText((String)mHourlyForecast.get(MainActivity.HOURLY_CONDITION));
        tempView.setText("Temperature " + mHourlyForecast.get(MainActivity.HOURLY_TEMP));
        feelsView.setText("Feels Like " + mHourlyForecast.get(MainActivity.HOURLY_FEELS_LIKE));

        return convertView;
    }
}
