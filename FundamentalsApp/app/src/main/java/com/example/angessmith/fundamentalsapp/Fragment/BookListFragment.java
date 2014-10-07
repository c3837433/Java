package com.example.angessmith.fundamentalsapp.Fragment;

//Created by AngeSSmith on 9/30/14 for Java 2 Term 1410.
// Book (Animal) best seller api http://api.nytimes.com/svc/mBooks/v2/lists.json?list-name=animals&api-key=f728de24bc37bd5a9d96255d947a47fc%3A15%3A69830529

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.angessmith.fundamentalsapp.BestSellerList;
import com.example.angessmith.fundamentalsapp.Book;
import com.example.angessmith.fundamentalsapp.R;

import java.util.ArrayList;

import static android.widget.AdapterView.OnItemClickListener;


public class BookListFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, OnItemClickListener {

    // Create the TAG
    public static final String TAG = "BookListFragment.TAG";
    private OnListItemClickListener mListListener;
    private OnSpinnerListener mSpinnerListener;
    private OnButtonListener mButtonListener;

    public static Spinner mSpinner;
    public static ListView mListview;
    public static ArrayList<Book> mBooks;
    ArrayList<BestSellerList> bestSellerList;

    // Create a factory instance of the fragment
    public static BookListFragment newInstance() {
        // Create a new instance of the fragment and return it
        return new BookListFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Make sure the activity attaches these listeners
        if (activity instanceof OnListItemClickListener) {
            // set the listener
            mListListener = (OnListItemClickListener) activity;
        }
        if (activity instanceof OnSpinnerListener) {
            mSpinnerListener = (OnSpinnerListener) activity;
        }

        if (activity instanceof OnButtonListener) {
            mButtonListener = (OnButtonListener) activity;
        }
    }

    // SET UP FOR WHEN THE USER CLICKS THE BUTTON TO GET THE LISTS
    @Override
    public void onClick(View v) {
        mButtonListener.GetBookLists();
    }

    // SET UP LISTENER FOR LISTVIEW
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // get the passed in object
        String title = mBooks.get(position).getTitle();
        String author = mBooks.get(position).getAuthor();
        String description = mBooks.get(position).getDescription();
        int rank = mBooks.get(position).getRank();
        String listType = mBooks.get(position).getList();
        // and send it to the main view
        mListListener.setBookDetails(title, author, description, rank, listType);

    }
    // SET UP LISTENER FOR SPINNER
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSpinnerListener.GetBooksOnList(parent, view, position, id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // DEFINE THE INTERFACES
    // Listview interface
    public interface OnListItemClickListener {

        public void setBookDetails(String title, String author, String description, int rank, String list);
    }

    // Define the spinner interface
    public interface OnSpinnerListener {
        public void GetBooksOnList(AdapterView<?> parent, View view, int position, long id);
    }

    // Define the button interface
    public interface  OnButtonListener {
        public void GetBookLists();
    }

    // SET THE FRAGMENT IN THE LAYOUT
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle _savedInstanceState) {
        View view = inflater.inflate(R.layout.book_list_fragment, container, false);
        return view;
    }

    // SET UP BUTTON TO CHECK FOR INTERNET AND PULL BEST SELLER LISTS FROM WEB OR CACHE FOR SPINNER
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Access the fragment view
        View view = getView();
        // get the spinner and listview for when we have data
        assert view != null;
        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(this);

        Button button = (Button) view.findViewById(R.id.get_list_button);
        button.setOnClickListener(this);

        mListview = (ListView) view.findViewById(R.id.bookslist);
        mListview.setOnItemClickListener(this);

    }

}
