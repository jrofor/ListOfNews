package com.example.roman.listofnews.mvp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.example.roman.listofnews.data.dataBase.NewsDatabaseConverter;
import com.example.roman.listofnews.data.dataBase.NewsDatabaseRepository;
import com.example.roman.listofnews.data.dataBase.NewsEntity;
import com.example.roman.listofnews.mvp.common.BasePresenter;
import com.example.roman.listofnews.ui.State;
import com.example.roman.listofnews.ui.adapter.AllNewsItem;
import com.example.roman.listofnews.ux.DefaultResponse;
import com.example.roman.listofnews.ux.RestApi;
import com.example.roman.listofnews.ux.TopStoriesMapper;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@InjectViewState
public class NewsListPresenter extends BasePresenter<NewsListView> {


    private static final String TAG = "myLogs";
    private RestApi restApi;
    @Nullable
    private NewsDatabaseConverter databaseConverter = new NewsDatabaseConverter();

    public NewsListPresenter(@NonNull RestApi instance) {
        this.restApi = instance;
    }

    public void loadItem(@NonNull String category,@NonNull NewsDatabaseRepository newsDatabaseRepository) {
        getViewState().showState(State.Loading);

        final Disposable disposable = (Disposable) RestApi.getInstanse()
                .getTSEndpoint()
                .setSectionName(category)
                .map(response ->
                        TopStoriesMapper
                                .map(response
                                        .getNews()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newsItems -> setupNews(newsItems, newsDatabaseRepository),
                        throwable -> handleError(throwable));
        disposeOnDestroy(disposable);
    }

    private void setupNews(@NonNull List<AllNewsItem> newsItems,@NonNull NewsDatabaseRepository newsDatabaseRepository) {
        if (newsItems.isEmpty()) {
            getViewState().showState(State.HasNoData);
            return;
        }
        Log.d(TAG, "--- setupNews");
        getViewState().showState(State.HasData);
        getViewState().updateItems(newsItems);
        //clear database before update
        deleteAllFromDatabaseWithRoom(newsDatabaseRepository);
        //Convert to Entities and save List AllNewsItem to database
        saveToDatabaseWithRoom(databaseConverter.toDatabase(newsItems), newsDatabaseRepository);
    }

    private void handleError (Throwable throwable) {
        Log.d(TAG, "/// handleError");
        if (throwable instanceof IOException) {
            getViewState().showState(State.NetworkError);
            return;
        }
        getViewState().showState(State.NetworkError);
    }

    /**
     ********************************************Database methods****************************************
     **/

    private void saveToDatabaseWithRoom(List<NewsEntity> NewsEntityList, @NonNull NewsDatabaseRepository newsDatabaseRepository) {
        Disposable disposable = newsDatabaseRepository.saveToDatabase(NewsEntityList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () ->
                                //Log.d(TAGroom, NewsEntityList.toString()),
                                Log.d(TAG, "save NewsEntityList To Database"),
                        throwable ->
                                Log.e(TAG, throwable.toString()));
        disposeOnDestroy(disposable);
    }

    public void initViews(@NonNull NewsDatabaseRepository newsDatabaseRepository) {
        Disposable disposable = newsDatabaseRepository.getDataFromDatabase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new Consumer<List<NewsEntity>>() {
                                @Override
                                public void accept(List<NewsEntity> newsEntities) throws Exception {
                                    //Log.d(TAG, newsEntities.toString());
                                    // updating Items in RecyclerView from Database with converting Entities to AllNewsItem
                                    getViewState().showState(State.HasData);
                                    getViewState().updateItems(databaseConverter.fromDatabase(newsEntities)) ;
                                    Log.d(TAG, "updating Items in RecyclerView from Database");

                                }
                            } ,
                        throwable -> Log.e(TAG, throwable.toString()));
        disposeOnDestroy(disposable);
    }

    private void deleteAllFromDatabaseWithRoom(@NonNull NewsDatabaseRepository newsDatabaseRepository) {
        Disposable disposable = newsDatabaseRepository.deleteAllFromDatabase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () ->
                                Log.d(TAG, "deleteAllFromDatabase"),
                        throwable ->
                                Log.e(TAG, throwable.toString()));
        disposeOnDestroy(disposable);
    }

    public void checkingDatabase(@NonNull NewsDatabaseRepository newsDatabaseRepository) {
        Disposable disposable = newsDatabaseRepository.checkDataInDatabase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        cnt ->
                                showResultCheckingDB(cnt));
        disposeOnDestroy(disposable);
    }

    private void showResultCheckingDB(@NonNull int cnt) {
        getViewState().showResultCheckingDatabase(cnt);
    }

}
