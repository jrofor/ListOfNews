package com.example.roman.listofnews.ui.adapter.pagedListAdapter;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.example.roman.listofnews.ui.adapter.AllNewsItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeeStorage {

    @Nullable
    private final List<AllNewsItem> news = new ArrayList<>();
    private String tit = "tit";
    private final Integer stopList = 33;
    //private MutableLiveData<AllNewsItem> liveData = new MutableLiveData<>();

    public void replaceItems (List<AllNewsItem> news ) {
        //this.news.clear();
        //this.news.addAll(news);
    }

    @Nullable
    public List<AllNewsItem> getData(int startPosition, int loadSize ) {
        List<AllNewsItem> outNews = new ArrayList<>();
        AllNewsItem TIT = AllNewsItem.create(tit,tit,tit,tit,tit,tit);
        //outNews.add(AllNewsItem.create(tit,tit,tit,tit,tit,tit));
        String cnt;

        //if (!news.isEmpty()) {
            /*for (int i=0; i<loadSize; i++) {
                cnt = String.valueOf(i);
                outNews.add(AllNewsItem.create(cnt,cnt,cnt,cnt,cnt,cnt));
                //outNews.add(news.get(startPosition+i)); }
                 }*/

            if (startPosition + loadSize < stopList) {
                    for (int i=startPosition; i<startPosition + loadSize ; i++) {
                        cnt = String.valueOf(i);
                        outNews.add(AllNewsItem.create(cnt,cnt,cnt,cnt,cnt,cnt)); }
            }  else if (startPosition < stopList) {
                for (int i=startPosition; i<stopList; i++) {
                    cnt = String.valueOf(i);
                    outNews.add(AllNewsItem.create(cnt,cnt,cnt,cnt,cnt,cnt)); }
            }
        return outNews;
    }

    public List<AllNewsItem> getInitialData(int startPosition, int loadSize) {
        List<AllNewsItem> outNews = new ArrayList<>();
        int beginningOffset = (startPosition + loadSize*3);
        String cnt;
        if (beginningOffset < stopList) {
            for (int i=startPosition; i<startPosition + loadSize ; i++) {
                cnt = String.valueOf(i);
                outNews.add(AllNewsItem.create(cnt,cnt,cnt,cnt,cnt,cnt)); }

        } else {
            for (int i=startPosition; i<stopList ; i++) {
                cnt = String.valueOf(i);
                outNews.add(AllNewsItem.create(cnt,cnt,cnt,cnt,cnt,cnt)); }
        }
        return outNews;
    }
}
