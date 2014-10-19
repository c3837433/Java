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
import android.widget.TextView;

import com.example.angessmith.navdrawerapp.Fragments.FeaturedStoryFragment;
import com.example.angessmith.navdrawerapp.Fragments.ImageCollectionFragment;
import com.example.angessmith.navdrawerapp.Fragments.StoriesFragment;

/*
Case Study 3
"Your client has requested that you build a news app prototype using static data with four main
features: a main page that shows a featured story, a news page which displays a list of recent stories,
an image gallery for viewing breaking news images, and a settings page to disable data over mobile."

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

    /*
    The activity overrides this method, which is named onNavigationDrawerItemSelected(),
    and places the proper fragment into the UI. By default, a placeholder fragment is used
    no matter which item is selected, but you can change this to be any fragment you want.
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FeaturedStoryFragment();
                break;
            case 1:
                fragment = new StoriesFragment();
                break;
            case 2:
                fragment = new ImageCollectionFragment();
                break;
            case 3:
                fragment = new ImageCollectionFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment).commit();
        }
        /*
        Log.i("Main", "The position selected: " + position);
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0:
                Log.i("Main", "Featured story Fragment called");
                fragmentManager.beginTransaction()
                        .replace(R.id.container, FeaturedStoryFragment.newInstance()).commit();
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, FeaturedStoryFragment.newInstance()).commit();
            case 2:
                Log.i("Main", "Image Collction Fragment Called");
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ImageCollectionFragment.newInstance(position + 1)).commit();
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, FeaturedStoryFragment.newInstance()).commit();
            default:

                break;
        }
        //fragmentManager.beginTransaction()
          //      .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
          */

    }

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



    /**
     * A placeholder fragment containing a simple view.
     */
    /*
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
       // private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        /*
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Main) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
    */

}
