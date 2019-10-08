package com.example.roman.listofnews.ui.adapter.pagedListAdapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.roman.listofnews.R;
import com.example.roman.listofnews.ui.adapter.AllNewsItem;
import com.example.roman.listofnews.ui.adapter.NewsRecyclerAdapter;
import com.example.roman.listofnews.ui.adapter.NewsViewHolder;

public class NewsPagedListAdapter extends PagedListAdapter <AllNewsItem, NewsViewHolder> {

    @Nullable
    private OnItemClickListener newsListener;
    private RequestManager glideRequestManager;
    private final LayoutInflater inflater;
    private Context context;

    public interface OnItemClickListener {
        //void OnItemClick(@NonNull AllNewsItem allnewsItem); }
        void OnItemClick(@NonNull String IdItem); }

    public NewsPagedListAdapter(@NonNull DiffUtil.ItemCallback<AllNewsItem> diffCallback, Context context) {
        super(diffCallback);

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        final RequestOptions imageOption = new RequestOptions()
                .placeholder(R.drawable.image_placeholder)
                .fallback(R.drawable.image_placeholder)
                .centerCrop();
        this.glideRequestManager = Glide.with(context).applyDefaultRequestOptions(imageOption);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        /*View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee, parent, false);
        EmployeeViewHolder holder = new EmployeeViewHolder(view);
        return holder;*/
        return NewsViewHolder.create (inflater, parent, glideRequestManager);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int position) {
        final AllNewsItem NIDTO = getItem(position);
        newsViewHolder.bindItem(NIDTO, newsListener, context);
    }

    public void setOnClickNewsListener(@NonNull NewsPagedListAdapter.OnItemClickListener newsListener) {
        this.newsListener = newsListener;
    }


}
