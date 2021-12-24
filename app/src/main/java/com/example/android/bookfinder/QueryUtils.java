package com.example.android.bookfinder;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private QueryUtils() {
    }
    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    /**
     * Query the USGS dataset and return an object to represent a single Book.
     */
    public static List<Books> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Books> booksList = extractEarthquakes(jsonResponse);

        // Return the {@link Event}
        Log.e("EarthquakeLoader","fetchEarthquakeData() is called");
        return booksList;
    }
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    private static List<Books> extractEarthquakes(String bookJSON)
    {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding Book to
        List<Books> booksList = new ArrayList<>();
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try{
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Books objects with the corresponding data.
            // Create a JSONObject from the JSON response string
            JSONObject root = new JSONObject(bookJSON);
            // Extract the JSONArray associated with the key called "items",
            // which represents a list of Books.
            JSONArray items = root.getJSONArray("items");
            int length = items.length();
            //if(length>20) length =20;
            for(int i=0;i<length;i++)
            {
                // Get a single earthquake at position i within the list of earthquakes
                JSONObject book = items.getJSONObject(i);
                // For a given earthquake, extract the JSONObject associated with the
                // key called "volumeInfo", which represents a list of all Volume info
                // for that Book.
                JSONObject volInfo = book.getJSONObject("volumeInfo");
                String authors = "Not Found";
                if(volInfo.has("authors")) {
                    JSONArray authorArr = volInfo.getJSONArray("authors");
                    authors="";
                    for (int j = 0; j < authorArr.length(); j++) {
                        if (j != 0) authors += ",";
                        authors = authors + authorArr.getString(j);
                    }
                }
                String title = volInfo.getString("title");
                String publisher = "Not Found";
                if(volInfo.has("publisher")) {
                    publisher = volInfo.getString("publisher");
                }
                String buyLink = "http://play.google.com/books/reader?id=iEPFd-SBVUEC&hl=&printsec=frontcover&source=gbs_api";
                String amt = "0(FREE)/NOT AVAILABLE";
                if(book.has("saleInfo")) {
                    JSONObject saleInfo = book.getJSONObject("saleInfo");
                    if(saleInfo.has("buyLink")) {
                         buyLink = saleInfo.getString("buyLink");
                    }
                    if(saleInfo.has("retailPrice")) {
                        JSONObject retailPrice = saleInfo.getJSONObject("retailPrice");
                        amt = retailPrice.getString("amount");

                    }
                }
                String webLink="http://play.google.com/books/reader?id=wSoDAAAAMBAJ&hl=&printsec=frontcover&source=gbs_api";
                String download_link = "http://play.google.com/books/reader?id=wSoDAAAAMBAJ&hl=&printsec=frontcover&source=gbs_api";
                if(book.has("accessInfo")) {
                    JSONObject accessInfo = book.getJSONObject("accessInfo");
                    webLink = accessInfo.getString("webReaderLink");
                    if(accessInfo.has("epub"))
                    {
                        JSONObject pdf = accessInfo.getJSONObject("pdf");
                         if(accessInfo.has("epub")) {
                             JSONObject epub = accessInfo.getJSONObject("epub");
                             if (epub.has("downloadLink")) {
                                 download_link = epub.getString("downloadLink");
                             }
                         }
                        else if(pdf.has("downloadLink")){
                            download_link = pdf.getString("downloadLink");
                        }

                    }

                }


                booksList.add(new Books(title,authors,publisher,amt,webLink,buyLink,download_link));

            }

        }
        catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        // Return the list of Books
        return booksList;


    }





}
