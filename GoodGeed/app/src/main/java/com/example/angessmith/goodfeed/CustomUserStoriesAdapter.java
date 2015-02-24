package com.example.angessmith.goodfeed;

// Created by AngeSSmith on 12/12/14.

// Custom list adapter for the user details story list
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.List;

public class CustomUserStoriesAdapter extends BaseAdapter {

    Context mContext;
    List<StoryPost> mStoryPosts;

    public CustomUserStoriesAdapter(Context context, List<StoryPost> storyPost) {
            mContext = context;
            mStoryPosts = storyPost;

    }

    @Override
    public int getCount() {
        return mStoryPosts.size();
    }

    @Override
    public Object getItem(int position) {
        return mStoryPosts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // reuse a view if we can, otherwise create one
            convertView = View.inflate(mContext, R.layout.user_post_item, null);
        }
        StoryPost post = mStoryPosts.get(position);
        // get data for each story
        if (post.getTitle() != null) {
            //Get and set the title text
            TextView postTitleView = (TextView) convertView.findViewById(R.id.user_post_title);
            postTitleView.setText(post.getTitle());
        }
        if (post.getCreatedAtDate() != null) {
            // Get the timestamp view
            TextView postTimeView = (TextView) convertView.findViewById(R.id.user_post_timestamp);
            // set the reformatted timestamp into the timestamp view
            postTimeView.setText(getTimeStamp(post.getCreatedAtDate()));
        }

        if (post.getStoryText() != null) {
            TextView textView = (TextView) convertView.findViewById(R.id.user_post_story);
            textView.setText(post.getStoryText());
        }

        // Check for story images
        ImageView storyImage = (ImageView) convertView.findViewById(R.id.user_post_image);
        if (post.getStoryPhoto() != null) {
            storyImage.setVisibility(View.VISIBLE);
            // Get the file and load in parse view
            ParseFile imageFile = post.getStoryPhoto();
            String urlString = imageFile.getUrl();
            // asynchronously load the image into the imageview
            Picasso.with(mContext)
                    .load(urlString)
                    .into(storyImage);

        } else {
            // set image to none, so a previous image doesn't fill it
            storyImage.setVisibility(View.GONE);
            storyImage.setImageBitmap(null);
        }

        return convertView;
    }

    public String getTimeStamp(Date date) {
        // Change to time
        long dateTime = date.getTime();
        // Use the pretty time library to reformat the time
        PrettyTime prettyTime = new PrettyTime();
        // return the new string
        return (prettyTime.format(new Date(dateTime)));
    }
}
