package com.example.roman.listofnews.ui.adapter;

import android.view.ViewGroup;
import com.bumptech.glide.RequestManager;
import com.example.roman.listofnews.ux.NewsDTO.NewsItemDTO;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;


public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsViewHolder>{
    @Nullable
    //private final List<NewsItemDTO> news = new ArrayList<>();
    private final List<AllNewsItem> news = new ArrayList<>();
    @Nullable
    private final OnItemClickListener clickListener;
    private RequestManager glideRequestManager;


    final String TAG = "myLogs";
    //private Handler mUiHandler = new Handler();
    //private  MyWorkerThread  mWorkerHandler;// = new NewsAboutActivity.MyWorkerThread("loadingImageThread");



    public NewsRecyclerAdapter(RequestManager glideRequestManager,  @Nullable OnItemClickListener clickListener) {
        //this.news = news;   @Nullable List<NewsItemDTO> news,
        this.clickListener = clickListener;
        this.glideRequestManager = glideRequestManager;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return NewsViewHolder.create (parent, glideRequestManager);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int position) {
        //final NewsItemDTO NIDTO = news.get(position);
        final AllNewsItem NIDTO = news.get(position);
        newsViewHolder.bindItem(NIDTO);
    }

    //public void replaceItems (List<NewsItemDTO> news ) {
    public void replaceItems (List<AllNewsItem> news ) {
        this.news.clear();
        this.news.addAll(news);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
    //    void OnItemClick(NewsItem news);
     void OnItemClick(int position); }

    @Override
    public int getItemCount() {
        return news.size();
    }

}

