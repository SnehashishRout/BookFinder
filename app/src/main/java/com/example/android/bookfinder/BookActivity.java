package com.example.android.bookfinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>> {
    
    private static final int BOOK_LOADER_ID = 1;

    /** Adapter for the list of Books */
    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        ListView bookListView = (ListView)findViewById(R.id.list);
        mAdapter = new BookAdapter(BookActivity.this,new ArrayList<Books>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter( mAdapter);

        // When individual list item is clicked upon, intent call to open corresponding pdf webLink open up
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Books BookObj = mAdapter.getItem(position);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(BookObj.getWebLink()));
                if (getIntent().resolveActivity(getPackageManager()) != null) {
                    startActivity(i);
                }
            }
        });
        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getSupportLoaderManager();
        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        Log.e("EarthquakeActivity", "initLoader()is called");
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);

    }

    @NonNull
    @Override
    public Loader<List<Books>> onCreateLoader(int id, @Nullable Bundle args) {
        // Create a new loader for the given URL
        Log.e("BookActivity","onCreateLoader method is called");
        return new BookLoader(BookActivity.this, MainActivity.REQUEST_URL);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Books>> loader, List<Books> booksList) {
        // Clear the adapter of previous earthquake data
        mAdapter.clear();
        if (booksList != null && !booksList.isEmpty()) {
            mAdapter.addAll(booksList);
        }
        Log.e("BookActivity","onLoadFinished()is called");

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Books>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
        Log.e("BookActivity","onLoaderReset()is called");

    }
}
