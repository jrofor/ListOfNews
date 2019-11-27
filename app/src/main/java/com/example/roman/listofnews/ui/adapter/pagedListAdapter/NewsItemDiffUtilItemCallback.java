package com.example.roman.listofnews.ui.adapter.pagedListAdapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.example.roman.listofnews.ui.adapter.AllNewsItem;

public class NewsItemDiffUtilItemCallback extends DiffUtil.ItemCallback<AllNewsItem> {

    @Override
    public boolean areItemsTheSame(@NonNull AllNewsItem oldList, @NonNull AllNewsItem newList) {
        return oldList.getTitle().equals(newList.getTitle());
    }

    @Override
    public boolean areContentsTheSame(@NonNull AllNewsItem allNewsItem, @NonNull AllNewsItem t1) {
        return allNewsItem.getTitle().equals(t1.getTitle())
                && allNewsItem.getUrl().equals(t1.getUrl());
    }
}