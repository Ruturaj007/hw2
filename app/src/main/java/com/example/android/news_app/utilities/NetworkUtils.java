package com.example.android.news_app.utilities;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

/**
 * Created by Welcome To Future on 6/21/2017.
 */

public class NetworkUtils {
    private static final String BASE_URL = "https://newsapi.org/v1/articles";

    final static String SRC_PARAM = "source";
    final static String SORT_PARAM = "sortBy";
    final static String NEWSAPIKEY_PARAM = "apiKey";


    private final static String src = "the-next-web";
    private final static String sort = "latest";
    private final static String key = "30c34a91fb6945068d88183293b50e2c";


    public static URL buildUrl() {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SRC_PARAM , src)
                .appendQueryParameter(SORT_PARAM, sort)
                .appendQueryParameter(NEWSAPIKEY_PARAM, key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        //Log.v(TAG, " URI " + url);
        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner sc = new Scanner(in);
            sc.useDelimiter("\\A");

            boolean hasInput = sc.hasNext();
            if (hasInput) {
                return sc.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static ArrayList<newsitem> parseJSON(String json) throws JSONException
    {
        ArrayList<newsitem> newsInfo = new ArrayList<>();
        JSONObject newsJson = new JSONObject(json);
        JSONArray newsArray = newsJson.getJSONArray("articles");
        String nSource = newsJson.getString("source");
        String nAuthor;
        String nTitle;
        String nDescription;
        String nUrl;
        String nUrlToImage;
        String nPublishedAt;

        for(int j = 0; j < newsArray.length(); j++)
        {
            JSONObject newsdata = newsArray.getJSONObject(j);
            nAuthor = newsdata.getString("author");
            nDescription= newsdata.getString("description");
            nTitle = newsdata.getString("title");
            nUrl = newsdata.getString("url");
            nUrlToImage = newsdata.getString("urlToImage");
            nPublishedAt = newsdata.getString("publishedAt");

            newsitem newsItem = new newsitem(nSource, nAuthor, nTitle, nDescription, nUrl, nUrlToImage, nPublishedAt);
            newsInfo.add(newsItem);
        }

        return newsInfo;
    }


}
