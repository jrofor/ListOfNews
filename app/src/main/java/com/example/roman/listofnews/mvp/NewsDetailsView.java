package com.example.roman.listofnews.mvp;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.roman.listofnews.data.dataBase.NewsEntity;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface NewsDetailsView extends MvpView {

    void bindViewNewsDetails(@NonNull NewsEntity newsEntity);
}
