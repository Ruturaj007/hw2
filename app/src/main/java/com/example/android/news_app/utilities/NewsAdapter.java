package com.example.android.news_app.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.news_app.R;

import java.util.ArrayList;

/**
 * Created by Welcome To Future on 6/29/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder>{

     ArrayList<newsitem> data;
    private static final String TAG = NewsAdapter.class.getSimpleName();

    final private ItemClickListener newsListener;

    public NewsAdapter(ArrayList<newsitem> data, ItemClickListener listener) {
        this.data = data;
        this.newsListener = listener;
    }

    public interface ItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToParent = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, attachToParent);

        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView title;
        public final TextView description;
        public final TextView newsTime;

        public NewsAdapterViewHolder(View newsView) {
            super(newsView);
            title = (TextView) newsView.findViewById(R.id.title);
            description = (TextView) newsView.findViewById(R.id.description);
            newsTime = (TextView) newsView.findViewById(R.id.publishedAt);
            newsView.setOnClickListener(this);
        }

        public void bind(int pos) {
            newsitem newsItem = data.get(pos);
            title.setText(newsItem.getTitle());
            description.setText(newsItem.getDescription());
            newsTime.setText(newsItem.getPublishedAt());
        }

        @Override
        public void onClick(View view) {
            newsListener.onListItemClick(getAdapterPosition());
        }
    }

    public void setData(ArrayList<newsitem> data) {

        this.data = data;
    }


}
