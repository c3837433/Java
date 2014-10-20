package com.example.angessmith.navdrawerapp;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angessmith.navdrawerapp.Fragments.FeaturedStoryFragment;
import com.example.angessmith.navdrawerapp.Fragments.ImageCollectionFragment;
import com.example.angessmith.navdrawerapp.Fragments.StoriesFragment;

import java.util.ArrayList;
import java.util.HashMap;

/*
Case Study 3
"Your client has requested that you build a news app prototype using static data with four main
features:

    1. a main page that shows a featured story
    2. a news page which displays a list of recent stories,
    3. an image gallery for viewing breaking news images
    4. a settings page to disable data over mobile."

For each navigation app you must, at a minimum, choose the correct navigation type and build the
app using the generated controls, adapters, and fragments. To demonstrate mastery of the navigation
topics, you must replace the generated controls, adapters, and fragments with ones that make sense
for the app being built. This means using the proper type of fragment where necessary
(ListFragment, PreferenceFragment, etc.), switching from the PlaceholderFragment class to one
you created yourself, and switching the ArrayAdapters out for adapters that make sense for the
data being displayed. If an ArrayAdapter makes sense, stick with it. If it doesn't make sense in
the context of the data being shown, change it to one that fits.
 */
public class Main extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    int mSection = -1;
    private ArrayList<NewsStory> mNewsStories;
    ArrayList<HashMap<String, Object>> mStories;
    // Create the static string keys that will be used in the hashmap and custom adapter
    static final String STORY_TITLE = "title";
    static final String STORY_TIME_STAMP = "timestamp";
    static final String STORY_IMAGE = "image";
    static final String STORY_TEXT = "story";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private ArrayList<NewsStory> setUpNewsStories() {
        // If the stories have not been created make them
        if (mNewsStories == null) {
            mNewsStories = new ArrayList<NewsStory>();
            // title,  timeStamp, story, image
            mNewsStories.add(NewsStory.newInstance("\"The Undertaker\" Succeeds Again!", "8:58 PM ET", "The secret agent only known as \"The Undertaker\", truly a master of disguise is victorious once again. While working undercover in Rio de Janeiro. Aliquam in ipsum sit amet urna congue aliquam. Curabitur tortor diam, maximus sed scelerisque quis, vestibulum sed tellus. Sed interdum dolor nisl, in commodo nibh auctor et. Nunc commodo accumsan nisl, non tristique justo. Etiam mollis accumsan est sit amet condimentum. Integer sit amet porttitor nibh.", R.drawable.begging));
            mNewsStories.add(NewsStory.newInstance("The Dorsey Diamonds Recovered", "Tuesday, 2:07 PM", "A high speed chase, balloons, and an unnamed casino. The only clues uncovered from the incredible recovery of the Dorsey heist last year. Praesent elementum neque lacus, pellentesque convallis est auctor et. Nullam lacinia, sem ac scelerisque viverra, nibh mauris dapibus sem, ac finibus nisl lorem ac justo. Pellentesque dignissim diam sit amet pulvinar cursus. Vestibulum consequat ex vitae tellus accumsan, nec iaculis orci sagittis. Ut pretium lorem libero, sed condimentum nibh fringilla id.", R.drawable.clown));
            mNewsStories.add(NewsStory.newInstance("When Undercover, Attitude is just as valuable as Disguise", "Monday, 7:35 PM ET", "Last week we scored a quick interview with the tireless, but patient \"Barefoot\". Praesent sagittis non magna sit amet commodo. Duis quis neque aliquet, tristique purus in, viverra sem. In mollis libero et viverra lacinia. Aenean sagittis egestas dui eget faucibus. Pellentesque massa justo, tincidunt tincidunt elementum ac, iaculis id tortor. Integer sed magna eget nibh tempus aliquet. Vestibulum aliquam porttitor rutrum. Mauris luctus tellus in tortor accumsan, et commodo sem congue.", R.drawable.mexdog));
            mNewsStories.add(NewsStory.newInstance("5 Saved In Overturned Boat", "Sunday, 11:11 PM ET", "An unknown agent was seen rescuing four children and their parent after their boar capsized in heavy winds early this morning. Proin aliquet mattis leo, lobortis tincidunt enim fringilla id. In hac habitasse platea dictumst. Duis eu ex accumsan, commodo lorem sit amet, volutpat nunc. Ut nibh nisi, tincidunt rhoncus quam vel, euismod accumsan magna. Proin accumsan turpis arcu, vitae tristique eros malesuada nec. Nunc eget posuere urna, in mollis nibh.", R.drawable.rescue));
            mNewsStories.add(NewsStory.newInstance("The Waiting Game of Surveillance", "Sunday, 7:53 PM ET", "Months of work waiting, watching, and tracking. Nulla convallis non mauris at mattis. Donec hendrerit purus scelerisque, mollis ex id, sagittis tellus. Sed pulvinar libero eget molestie varius. Duis vitae cursus tortor. Etiam maximus augue et lectus sagittis, rhoncus placerat quam pretium. Sed non congue purus.", R.drawable.survelence));
            mNewsStories.add(NewsStory.newInstance("Trying to Hide in Plain Sight", "Saturday, 8:42 PM ET", "The art of remaining hidden, yet without drawing undue attention can be challenging. Curabitur porta sagittis augue. Quisque ac aliquet ligula. Donec posuere aliquet ligula, at ultrices leo tincidunt sed. Proin sollicitudin id metus vitae fringilla. Cras egestas maximus congue. Nullam odio est, faucibus sed sodales eget, viverra in diam.", R.drawable.undercover));
        }
        return mNewsStories;
    }

    private ArrayList<HashMap<String, Object>> SetUpHashMap() {
        if (mStories == null) {
            // Create an arraylist to hold the hashmap
            mStories = new ArrayList<HashMap<String, Object>>();
            // Loop through the stories and add them to the list
            for (NewsStory story : mNewsStories) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                // set the properties in the map
                map.put(STORY_TITLE, story.getTitle());
                map.put(STORY_TIME_STAMP, story.getTimeStamp());
                map.put(STORY_TEXT, story.getStory());
                map.put(STORY_IMAGE, story.getImageId());
                // Add the map to the arraylist
                mStories.add(map);
            }
        }
        return mStories;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // empty the fragment so we start fresh on each load
        ArrayList<NewsStory>  storyList = setUpNewsStories();
        Fragment fragment = null;
        // save the position
        mSection = position;
        // See which position was selected
        switch (position) {
            // Create a new instance of each fragment, passing in the
            case 0:
                // Pass in the first story from the list (newest)
                // See what is in the array
                Log.d("Main", "Feature Title = " + storyList.get(0).getTitle() + "Feature Time = " + storyList.get(0).getTimeStamp());
                fragment = FeaturedStoryFragment.newInstance(position + 1, storyList.get(0));
                break;
            case 1:
                // Pass in the arraylist of stories
                ArrayList<HashMap<String, Object>> mapList = SetUpHashMap();
                        fragment = StoriesFragment.newInstance(position + 1, mapList);
                break;
            case 2:
                fragment = ImageCollectionFragment.newInstance(position + 1);
                break;
            case 3:
                fragment = ImageCollectionFragment.newInstance(position + 1);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment).commit();
        }
    }

    // Set the title to the correct string
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
}
