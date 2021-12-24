package com.example.android.bookfinder;

public class Books {
    private String mTitle;
    private String mAuthor;
    private String mPublisher;
    private String mPrice;
    private String pdfLink;
    private String mbuyLink;
    private String mDownloadLink;

    public Books(String title,String Author, String Publisher,String Price,String link,String buyLink,String downloadLink) {
        mTitle = title;
        mAuthor = Author;
        mPublisher = Publisher;
        mPrice = Price;
        pdfLink = link;
        mbuyLink = buyLink;
        mDownloadLink = downloadLink;

    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPrice() {
        return mPrice;
    }

    public String getWebLink() {
        return pdfLink;
    }

    public String getPublisher() {
        return mPublisher;
    }
    public String getBuyLink()
    {
        return mbuyLink;
    }
    public String getDownloadLink(){
        return mDownloadLink;
    }
}
