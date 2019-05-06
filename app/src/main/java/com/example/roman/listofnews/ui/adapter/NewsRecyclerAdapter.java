package com.example.roman.listofnews.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.roman.listofnews.R;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;


public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsViewHolder>{
    @Nullable
    private final List<AllNewsItem> news = new ArrayList<>();
    @Nullable
    private OnItemClickListener newsListener;
    private RequestManager glideRequestManager;
    private final LayoutInflater inflater;
    final String TAG = "myLogs";


    public interface OnItemClickListener {
        void OnItemClick(@NonNull AllNewsItem allnewsItem); }

    public NewsRecyclerAdapter(Context context) {
        //@Nullable OnItemClickListener clickListener;
        //this.news = news;   @Nullable List<NewsItemDTO> news,
        //this.clickListener = clickListener;
        this.inflater = LayoutInflater.from(context);
        final RequestOptions imageOption = new RequestOptions()
                .placeholder(R.drawable.image_placeholder)
                .fallback(R.drawable.image_placeholder)
                .centerCrop();
        this.glideRequestManager = Glide.with(context).applyDefaultRequestOptions(imageOption);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return NewsViewHolder.create (inflater, parent, glideRequestManager);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int position) {
        final AllNewsItem NIDTO = news.get(position);
        newsViewHolder.bindItem(NIDTO, newsListener);
    }

    public void setOnClickNewsListener(@NonNull OnItemClickListener newsListener) {
        this.newsListener = newsListener;
    }

    public void replaceItems (List<AllNewsItem> news ) {
        this.news.clear();
        this.news.addAll(news);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return news.size();
    }

}

