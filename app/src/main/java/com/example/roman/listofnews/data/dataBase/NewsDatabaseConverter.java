package com.example.roman.listofnews.data.dataBase;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.roman.listofnews.ui.adapter.AllNewsItem;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;

public class NewsDatabaseConverter {

    private final Context mContext;

    public NewsDatabaseConverter(Context mContext) {
        this.mContext = mContext;
    }

    Completable saveDate (final List<AllNewsItem> AllNewsItemList) {
        return Completable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                NewsAppDatabase db = NewsAppDatabase.getNewsAppDatabase(mContext);

                AllNewsItem[] newsEntity = AllNewsItemList.toArray(new AllNewsItem[AllNewsItemList.size()]);

                db.newsDao.insertAll(newsEntity);


                return null;
            }


        });
    }
}
