package com.example.angessmith.goodfeed;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

// Created by AngeSSmith on 12/9/14.

// Custom listview adapter for the Main Feed. It retrieves all the posts and displays the returned
// information within the list view.


public class CustomStoryAdapter extends ParseQueryAdapter<StoryPost> {

    // set up the interface
    public static MainFeedActivity.OnItemButtonClickListener mOnItemButtonClickListener;
    public static final String TAG = "CustomStoryAdapter.TAG";

    public CustomStoryAdapter(Context context, MainFeedActivity.OnItemButtonClickListener listener) {
        // create a query for all the Story objects
        super(context, new ParseQueryAdapter.QueryFactory<StoryPost>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery(StoryPost.class);
                // include their author
                query.include("author");
                // place the newest ones at the top
                query.addDescendingOrder("createdAt");
                // set the cache so we don't send requests for no reason
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                return query;
            }
        });
        mOnItemButtonClickListener = listener;
    }



    // Set up the adapter view
    @Override
    public View getItemView(final StoryPost post, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            // reuse a view if we can, otherwise create one
            convertView = View.inflate(getContext(), R.layout.post_item, null);
        }

        super.getItemView(post, convertView, viewGroup);

        // To prevent null exceptions, make sure we have valid data for each element
        if (post.getTitle() != null) {
            //Get and set the title text
            TextView postTitleView = (TextView) convertView.findViewById(R.id.post_item_title);
            postTitleView.setText(post.getTitle());
        }
        if (post.getCreatedAtDate() != null) {
            // Get the timestamp view
            TextView postTimeView = (TextView) convertView.findViewById(R.id.post_item_timestamp);
            // set the reformatted timestamp into the timestamp view
            postTimeView.setText(getTimeStamp(post.getCreatedAtDate()));
        }

        if (post.getStoryText() != null) {
            TextView textView = (TextView) convertView.findViewById(R.id.post_item_text);
            textView.setText(post.getStoryText());
        }


        // Check for story images
        final ImageView storyImage = (ImageView) convertView.findViewById(R.id.post_item_image);
        if (post.getStoryPhoto() != null) {
            storyImage.setVisibility(View.VISIBLE);
            ParseFile imageFile = post.getStoryPhoto();
            String urlString = imageFile.getUrl();
            // asynchronously load the image into the imageview
            Picasso.with(getContext())
                    .load(urlString)
                    .into(storyImage);

        } else {
            // set image to none, so a previous image doesn't fill it
            storyImage.setVisibility(View.GONE);
            storyImage.setImageBitmap(null);
        }

        // Get the author values
        if (post.getStoryAuthor() != null) {
            // get the picasso view
            ImageView picassoImageView = (ImageView) convertView.findViewById(R.id.picasso_image_view);
            if (post.getStoryAuthor().getUserName() != null) {
                Button authorNameButton = (Button) convertView.findViewById(R.id.post_item_author);
                authorNameButton.setText(post.getStoryAuthor().getUserName());
                authorNameButton.setTag(post.getStoryAuthor().getAuthorId());
                authorNameButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (mOnItemButtonClickListener != null)
                            mOnItemButtonClickListener.ViewUserDetails(post.getStoryAuthor());
                    }
                });
            }
            // See if we can get the url of a profile picture
            if (post.getStoryAuthor().getProfilePic() != null) {
                ParseFile profileFile = post.getStoryAuthor().getProfilePic();
                String urlString = profileFile.getUrl();
                // see if we have a profile image url from registering or if user updated it
                Picasso.with(getContext())
                        .load(urlString)
                        .placeholder(R.drawable.default_profile)
                        .tag(post.getStoryAuthor().getAuthorId())
                        .into(picassoImageView);
            }  else if (post.getStoryAuthor().getFacebookId() != null) {
                // Load the users facebook profile picture
                String fbProfileString = "https://graph.facebook.com/" + post.getStoryAuthor().getFacebookId() + "/picture?type=large";
                Picasso.with(getContext()).load(fbProfileString)
                        .placeholder(R.drawable.default_profile)
                        .into(picassoImageView);

            } else {
                // use the default image
                Resources resources = getContext().getResources();
                Drawable defaultImage = resources.getDrawable(R.drawable.default_profile);
                picassoImageView.setImageDrawable(defaultImage);
            }

            // set the buttons
            ImageButton flagButton = (ImageButton)convertView.findViewById(R.id.flag_item_button);
            flagButton.setTag(post.getStoryId());
            flagButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (mOnItemButtonClickListener != null)
                        mOnItemButtonClickListener.FlagStory(post);
                }
            });
            ImageButton shareButton = (ImageButton)convertView.findViewById(R.id.share_item_button);
            shareButton.setTag(post.getStoryId());
            shareButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (mOnItemButtonClickListener != null)
                        mOnItemButtonClickListener.ShareStory(post);
                }
            });

            Button profilePicButton = (Button) convertView.findViewById(R.id.open_user_details_image_button);
            profilePicButton.setTag(post.getStoryAuthor().getAuthorId());
            profilePicButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (mOnItemButtonClickListener != null)
                        mOnItemButtonClickListener.ViewUserDetails(post.getStoryAuthor());
                }
            });

        }
        return convertView;
    }

    // Conver the Date time of creation to an elapsed time string
    public String getTimeStamp(Date date) {
        // Change to time
        long dateTime = date.getTime();
        // Use the pretty time library to reformat the time
        PrettyTime prettyTime = new PrettyTime();
        // return the new string
        return (prettyTime.format(new Date(dateTime)));
    }

}
