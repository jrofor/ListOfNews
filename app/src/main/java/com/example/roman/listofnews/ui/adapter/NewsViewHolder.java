package com.example.roman.listofnews.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.roman.listofnews.R;

public class NewsViewHolder extends RecyclerView.ViewHolder {

    private RequestManager imageLoader;
    private ProgressBar progressBar;
    private TextView tvSubsection;
    private TextView tvTitle;
    private TextView tvPreview;
    private ImageView ivMultimedia;
    private TextView tvDate;

    private static final int LayuotItem = R.layout.item_news;

    public NewsViewHolder(@NonNull View itemView, RequestManager glideRequestManager) {
        super(itemView);
        this.imageLoader = glideRequestManager;
        findViews(itemView);
    }

    private void findViews (@NonNull View itemView) {
        ivMultimedia = itemView.findViewById(R.id.iv_multimedia);
        progressBar = itemView.findViewById(R.id.progress_bar);
        tvSubsection = itemView.findViewById(R.id.tv_subsection);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvDate = itemView.findViewById(R.id.tv_date);

    }

}
