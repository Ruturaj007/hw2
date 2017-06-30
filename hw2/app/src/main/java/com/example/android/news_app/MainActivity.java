package com.example.android.news_app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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

import static com.example.android.news_app.R.id.nsearch;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "mainactivity";
    private TextView mNewsTextView;
    private TextView mUrlNewsView;
    private EditText mSearchBoxNewsText;
    private ProgressBar mLoadingIndicator;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = (RecyclerView)findViewById(R.id.recyclerView);

        rv.setLayoutManager(new LinearLayoutManager(this));
       // mNewsTextView = (TextView) findViewById(R.id.ndata);
        //mSearchBoxNewsText = (EditText) findViewById(R.id.nsearch);
        //mUrlNewsView = (TextView) findViewById(R.id.nurl);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pindicator);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nsearch) {

            String s = nsearch.getText().toString();
            newsinfo task = new newsinfo(s);
            task.execute();
        }
        return true;
    }




 /*  private void loadNewsData() {

        URL NewsSearchUrl = NetworkUtils.buildUrl();
        mUrlNewsView.setText(NewsSearchUrl.toString());


        new newsinfo("result").execute();
    }*/
    public class newsinfo extends AsyncTask<URL, Void, ArrayList<newsitem>> {
        String query;
        newsinfo(String s) {
            query = s;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<newsitem> doInBackground(URL... params) {
            ArrayList<newsitem> result = null;
            URL url = NetworkUtils.buildUrl();
           Log.d(TAG, "url: " + url.toString());
            try {
                String json=NetworkUtils.getResponseFromHttpUrl(url);

                result = NetworkUtils.parseJSON(json);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;
        }

        @Override
        protected void onPostExecute(final ArrayList<newsitem> data) {
            super.onPostExecute(data);
            mLoadingIndicator.setVisibility(View.GONE);
            if (data != null) {
                NewsAdapter adapter = new NewsAdapter(data, new NewsAdapter.ItemClickListener() {
                   @Override
                    public void onItemClick(int clickedItemIndex)
                    {
                        String url = data.get(clickedItemIndex).getUrl();
                       // Uri webpage=Uri.parse(url);
                        //Intent intent=new Intent(Intent.ACTION_VIEW,webpage);
                        //startActivity(intent);
                        Log.d("main activity", String.format("Url %s", url));
                    }
                });
                rv.setAdapter(adapter);

            }
        }
    }
}
