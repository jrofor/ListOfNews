package com.example.roman.listofnews.data.dataBase;

import android.content.Context;
import android.util.Log;

import com.example.roman.listofnews.ui.adapter.AllNewsItem;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewsDatabaseWork {


    private Context wContext;
    public NewsDatabaseWork(Context wContext) {this.wContext = wContext;}

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private NewsDatabaseRepository newsDatabaseRepository = new NewsDatabaseRepository(wContext);
    private static final String TAG = "RoomActivity";


    private void initViews() {
        Disposable disposable = newsDatabaseRepository.getDataFromDatabase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NewsEntity>>() {
                               @Override
                               public void accept(List<NewsEntity> newsEntities) throws Exception {
                                   Log.e(TAG, newsEntities.toString());
                                   //

                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e(TAG, throwable.toString());
                               }
                           });
        compositeDisposable.add(disposable);
    }

    private void subscribeToDate() {
        Disposable disposable = newsDatabaseRepository.getDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new Consumer<List<NewsEntity>>() {
                    @Override
                    public void accept(List<NewsEntity> newsEntities) throws Exception {
                        Log.e(TAG, newsEntities.toString());
                        //

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void saveToDatabaseWhisRoom(List<NewsEntity> NewsEntityList) {
        Disposable disposable = newsDatabaseRepository.saveToDatabase(NewsEntityList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, NewsEntityList.toString());
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

}



