package com.example.android.bookfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Books> {
    private Activity mContext;

    public BookAdapter(Activity context,List<Books> booksList)
    {

        super(context,0,booksList);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_layout, parent, false);
        }
        Books currentBook = getItem(position);
        TextView title = (TextView)listItemView.findViewById(R.id.title);
        title.setText(currentBook.getTitle());
        TextView author = (TextView)listItemView.findViewById(R.id.author);
        author.setText(currentBook.getAuthor());
        TextView publisher = (TextView)listItemView.findViewById(R.id.publisher);
        publisher.setText(currentBook.getPublisher());
        TextView price = (TextView)listItemView.findViewById((R.id.price));
        price.setText(currentBook.getPrice());
        Button buyButton = (Button)listItemView.findViewById(R.id.button);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(currentBook.getBuyLink()));
                if (i.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(i);
                }
            }
        });
        ImageView pdfButton = (ImageView)listItemView.findViewById(R.id.pdf_button);
        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentBook.getDownloadLink()));
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                }
            }
        });

        return listItemView;

    }
}
