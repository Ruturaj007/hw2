package com.example.android.news_app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.news_app.utilities.NetworkUtils;
import com.example.android.news_app.utilities.NewsAdapter;
import com.example.android.news_app.utilities.newsitem;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    private TextView mNewsTextView;
    private TextView mUrlNewsView;
    private EditText mSearchBoxNewsText;
    private ProgressBar mLoadingIndicator;
    static final String TAG = "mainactivity";
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxNewsText = (EditText) findViewById(R.id.nurl);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pindicator);

        rv = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        loadNewsData();
    }

    private void loadNewsData() {
        new getNewsData("").execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nsearch:
                String news_search = mSearchBoxNewsText.getText().toString();
                getNewsData task = new getNewsData(news_search);
                task.execute();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class getNewsData extends AsyncTask<URL, Void, ArrayList<newsitem>> implements NewsAdapter.ItemClickListener {
        String query;
        ArrayList<newsitem> data;

        getNewsData(String search)
        {
            query = search;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<newsitem> doInBackground(URL... params) {
            ArrayList<newsitem> nResult = null;
            URL newsRequestURL = NetworkUtils.buildUrl();

            try {
                String json = NetworkUtils.getResponseFromHttpUrl(newsRequestURL);
                nResult = NetworkUtils.parseJSON(json);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return nResult;
        }

        @Override
        protected void onPostExecute(final ArrayList<newsitem> newsData) {
            this.data = newsData;
            super.onPostExecute(data);
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (data != null) {
                NewsAdapter adapterData = new NewsAdapter(data, this);
                rv.setAdapter(adapterData);
            }
        }

        @Override
        public void onListItemClick(int clickedItemIndex) {
            openWebPage(data.get(clickedItemIndex).getUrl());
        }

        public void openWebPage(String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
}
