package com.example.android.bookfinder;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Books>> {
    private String murl;
    /**
     * This is on a background thread.
     */
    public BookLoader(@NonNull Context context, String url) {
        super(context);
        murl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.e("BookLoader","Loader has Started Loading");
        forceLoad();
    }

    @Nullable
    @Override
    public List<Books> loadInBackground() {
        Log.e("BookLoader","loadInBackground()is called");
        // Don't perform the request if there are no URLs, or the first URL is null.
            /*if(murl.length<1 || murl[0]==null){
                return null;
            }*/
        // Perform the HTTP request for earthquake data and parse the response and extract a list of earthquakes.
        List<Books> booksList = QueryUtils.fetchEarthquakeData(murl);
        return booksList;
    }
}
