package com.example.angessmith.goodfeed.Fragment;


 // Created by AngeSSmith on 12/11/14.
// Fragment that displays the users main profile information (name and image)
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.angessmith.goodfeed.R;
import com.example.angessmith.goodfeed.StoryAuthor;
import com.facebook.widget.ProfilePictureView;
import com.parse.ParseFile;
import com.parse.ParseImageView;

public class UserProfileFragment extends Fragment {

    public static final String TAG = "UserProfileFragment";
    StoryAuthor mStoryAuthor;
    TextView mNameView;
    ParseImageView mRegisteredImage;
    ProfilePictureView mFacebookImage;

    // Set up the fragment
    public static UserProfileFragment newInstance(StoryAuthor author) {
        // Get and set the author
        UserProfileFragment fragment = new UserProfileFragment();
        fragment.mStoryAuthor = author;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the container
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mNameView = (TextView) view.findViewById(R.id.user_profile_name);
        mRegisteredImage = (ParseImageView) view.findViewById(R.id.user_parse_profile_picture);
        mFacebookImage = (ProfilePictureView) view.findViewById(R.id.user_facebook_profile_picture);
        setUserProfileData();
        return view;
    }

    private void setUserProfileData() {
        // Make sure we have a valid author
        if (mStoryAuthor!= null) {
            Log.d(TAG, "User profile data passed");
            if (mStoryAuthor.getUserName() != null) {
                Log.d(TAG, "User name = " + mStoryAuthor.getUserName());
                mNameView.setText(mStoryAuthor.getUserName());
            }
            // check for a profile picture
            if (mStoryAuthor.getProfilePic() != null) {
                Log.d(TAG, "User has registered profile picture");
                // user has a profile picture
                ParseFile profileFile = mStoryAuthor.getProfilePic();
                mRegisteredImage.setParseFile(profileFile);
                mRegisteredImage.loadInBackground();
                mFacebookImage.setVisibility(View.GONE);

            } else if (mStoryAuthor.getFacebookId() != null) {
                Log.d(TAG, "User has Facebook profile picture");
                // user has a facebook id which we can use to display their profile picture
                mFacebookImage.setProfileId(mStoryAuthor.getFacebookId());
                mRegisteredImage.setVisibility(View.GONE);
            } else {
                // use default image
                Log.d(TAG, "User has no profile picture");
                mFacebookImage.setProfileId(null);
                mRegisteredImage.setVisibility(View.GONE);
            }
        } else {
            Log.d(TAG, "No profile data passed");
        }
    }

}
