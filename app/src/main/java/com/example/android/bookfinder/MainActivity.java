package com.example.android.bookfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.midi.MidiDeviceService;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    public static String REQUEST_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = (SearchView)findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                REQUEST_URL= "https://www.googleapis.com/books/v1/volumes?q="+query;
                Intent bookIntent = new Intent(MainActivity.this,BookActivity.class);
                startActivity(bookIntent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}