package com.example.roman.listofnews.mvp;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.example.roman.listofnews.data.Storage;
import com.example.roman.listofnews.data.background.UploadWork;
import com.example.roman.listofnews.mvp.common.BasePresenter;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

@InjectViewState
public class NewsAboutPresenter extends BasePresenter<NewsAboutView> {

    public void setCheckedValueSwIntro() {
        getViewState().setValueSwIntro();
    }

    public void onCheckedChangedSwIntro(@NonNull Boolean on) {
        getViewState().saveValueSwIntro(on);
    }

    public void setCheckedValueSwUpdate() {
        getViewState().setValueSwUpdate();
    }

    public void onCheckedChangedSwUpdate(@NonNull Boolean on) {
        getViewState().saveValueAndWorkSwUpdate(on);
    }

    public void startPeriodicUpdateService(@NonNull String TAG) {
        Constraints workConstraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(UploadWork.class,
                185, TimeUnit.MINUTES, 5, TimeUnit.MINUTES)
                .setConstraints(workConstraints)
                .addTag(TAG)
                .build();

        WorkManager.getInstance().enqueue(workRequest);
        getViewState().showWorkPeriodicUpdateService();
    }

    public void stopPeriodicUpdateService(@NonNull String TAG) {
        WorkManager.getInstance().cancelAllWorkByTag(TAG);
    }

    public void ErrorEmailMessage() {
        getViewState().showErrorEmailMessage();
    }

    public void onClickSendMessage(@NonNull String message) {
        getViewState().sendingMessage(message);
    }

    public void ErrorEmailClient() {
        getViewState().showErrorEmailClient();
    }


    public void onClickTelegramIcon() {
        getViewState().openUrlTelegramIcon();
    }

    public void onClickGithubIcon() {
        getViewState().openUrlGithubIcon();
    }

    public void onClickNyTimesLogo() {
        getViewState().openUrlNyTimesLogo();
    }

    public void ErrorWebClient() {
        getViewState().showErrorWebClient();
    }

}
