package com.example.roman.listofnews.mvp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface NewsAboutView extends MvpView {

    void setValueSwIntro();

    void saveValueSwIntro(@NonNull Boolean on);

    void setValueSwUpdate();

    void saveValueAndWorkSwUpdate(@NonNull Boolean on);

    void showWorkPeriodicUpdateService();


    void showErrorEmailMessage();

    void sendingMessage(@NonNull String message);

    void showErrorEmailClient();


    void openUrlTelegramIcon();

    void openUrlGithubIcon();

    void openUrlNyTimesLogo();

    void showErrorWebClient();
}
