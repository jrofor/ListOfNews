package com.example.roman.listofnews.mvp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.example.roman.listofnews.data.dataBase.NewsDatabaseRepository;
import com.example.roman.listofnews.data.dataBase.NewsEntity;
import com.example.roman.listofnews.mvp.common.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class NewsDetailsPresenter extends BasePresenter<NewsDetailsView> {
    @Nullable
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void getDataInEntityByIdFromDatabase(@NonNull String idItem, @NonNull NewsDatabaseRepository newsDatabaseRepository) {
        Disposable getDataDisposable = newsDatabaseRepository.getEntitybyIdFromDatabase(idItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showNewsEntity);
        disposeOnDestroy(getDataDisposable);
    }

    private void showNewsEntity(NewsEntity newsEntity) {
        getViewState().bindViewNewsDetails(newsEntity);
    }

}
