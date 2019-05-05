package com.example.roman.listofnews.data.dataBase;

import android.content.Context;
import java.util.List;
import io.reactivex.Observable;
import java.util.concurrent.Callable;
import io.reactivex.Completable;
import io.reactivex.Single;

public class NewsDatabaseRepository {

    private final Context mContext;

    public NewsDatabaseRepository(Context mContext) {
        this.mContext = mContext;
    }

    public Completable saveToDatabase(final List<NewsEntity> NewsEntityList) {
        return Completable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                NewsAppDatabase db = NewsAppDatabase.getNewsAppDatabase(mContext);

                NewsEntity[] newsEntities = NewsEntityList.toArray(new NewsEntity[NewsEntityList.size()]);

                db.newsDao().insertAll(newsEntities);

                return null;
            }
        });
    }

    public Single<List<NewsEntity>> getDataFromDatabase() {
        return Single.fromCallable(new Callable<List<NewsEntity>>() {
            @Override
            public List<NewsEntity> call() throws Exception {
                NewsAppDatabase db = NewsAppDatabase.getNewsAppDatabase(mContext);
                return db.newsDao().getAll();
            }
        });
    }

    public Completable deleteAllFromDatabase() {
        return Completable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                NewsAppDatabase db = NewsAppDatabase.getNewsAppDatabase(mContext);

                db.newsDao().deleteAll();

                return null;
            }
        });
    }

    public Single<Integer> checkDataInDatabase() {
        return Single.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                NewsAppDatabase db = NewsAppDatabase.getNewsAppDatabase(mContext);
                return db.newsDao().newsEntityCount();
            }
        });
    }

    /*public Observable<List<NewsEntity>> getDataObservable() {
        NewsAppDatabase db = NewsAppDatabase.getNewsAppDatabase(mContext);

        //return db.newsAsyncDao().getAll();
    }*/

}