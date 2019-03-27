package com.example.roman.listofnews.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.RequestManager;
import com.example.roman.listofnews.R;

public class NewsViewHolder extends RecyclerView.ViewHolder {

    private RequestManager imageLoader;
    private ImageView multimedia;
    private ProgressBar progressBar;
    private static final int LayuotItem = R.layout.item_news;

    public NewsViewHolder(@NonNull View itemView, RequestManager glideRequestManager) {
        super(itemView);
        this.imageLoader = glideRequestManager;
        findViews(itemView);
    }

    private void findViews (@NonNull View itemView) {

    }

}
