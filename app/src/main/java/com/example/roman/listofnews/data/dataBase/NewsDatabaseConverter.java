package com.example.roman.listofnews.data.dataBase;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.roman.listofnews.ui.adapter.AllNewsItem;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Single;

public class NewsDatabaseConverter {

    private final Context mContext;

    public NewsDatabaseConverter(Context mContext) {
        this.mContext = mContext;
    }

    Completable saveToDatabase (final List<AllNewsItem> AllNewsItemList) {
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

    Single <List<NewsEntity>> getDataFromDatabase() {
        return Single.fromCallable(new Callable<List<NewsEntity>>() {
            @Override
            public List<NewsEntity> call() throws Exception {
                NewsAppDatabase db = NewsAppDatabase.getNewsAppDatabase(mContext);
                return db.newsDao.getAll();
            }
        });
    }

    //Observable<List<NewsEntity>> getDataObservable() {
    //        AppDatabase db = AppDatabase.getAppDatabase(mContext);
    //
    //        return db.newsAsyncDao().getAll();
}
