package com.example.roman.listofnews.ui.adapter.pagedListAdapter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.roman.listofnews.ui.adapter.AllNewsItem;

import java.util.ArrayList;
import java.util.List;

public class EmployeeStorage {

    @Nullable
    private List<AllNewsItem> news = new ArrayList<>();
    private int stopList;
    private String cnt;
    private int minStartPosition;
    private static final String TAG = "myLogs";

    public EmployeeStorage(List<AllNewsItem> news, int startLoadKey) {
        getDataFromDB(news);
        minStartPosition = startLoadKey;
    }

    private void getDataFromDB(List<AllNewsItem> news) {
        this.news.clear();
        this.news.addAll(news);
        stopList = news.size();
        Log.d(TAG, "EmployeeStorage updating from Database");
    }

    @Nullable
    public List<AllNewsItem> getData(int startPosition, int loadSize) {
        List<AllNewsItem> outNews = new ArrayList<>();
        outNews.clear();
        if (startPosition + loadSize < stopList) {
            if (!news.isEmpty()) {
                for (int i = startPosition; i < startPosition + loadSize; i++) {
                    outNews.add(news.get(i));
                }
            } else {
                for (int i = startPosition; i < startPosition + loadSize; i++) {
                    cnt = String.valueOf(i);
                    outNews.add(AllNewsItem.create(cnt, cnt, cnt, cnt, cnt, cnt));
                }
            }
        } else if (startPosition < stopList) {
            if (!news.isEmpty()) {
                for (int i = startPosition; i < stopList; i++) {
                    outNews.add(news.get(i));
                }
            } else {
                for (int i = startPosition; i < stopList; i++) {
                    cnt = String.valueOf(i);
                    outNews.add(AllNewsItem.create(cnt, cnt, cnt, cnt, cnt, cnt));
                }
            }
        }

        if (startPosition < minStartPosition) {
            if (startPosition != stopList) {
                minStartPosition = startPosition;
            }
            Log.d(TAG, "minStartPosition" + minStartPosition);
        }
        return outNews;
    }

    public int outCount() {
        return stopList;
    }

    public int outMinStartPosition() {
        return minStartPosition;
    }

}
