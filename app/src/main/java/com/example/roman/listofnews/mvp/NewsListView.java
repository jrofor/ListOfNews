package com.example.roman.listofnews.mvp;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.roman.listofnews.ui.State;
import com.example.roman.listofnews.ui.adapter.AllNewsItem;

import java.util.List;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface NewsListView extends MvpView {

    void showState(@NonNull State state);

    void updateItems(@NonNull List<AllNewsItem> newsItems);

    void showResultCheckingDatabase(@NonNull int cnt);
}