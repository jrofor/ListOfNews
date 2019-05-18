package com.example.roman.listofnews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;

import com.example.roman.listofnews.data.Storage;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class NewsIntroActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
//СДЕЛАТЬ ЗАПУСК ПРИ КАЖДОМ 2 м ОТКРЫВАНИИ!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        // проверяем, первый ли раз открывается программа (запускалась интро или нет)
        if (!Storage.checkIntro(this)) {//
            Storage.setIntroHasShown(this);
            setContentView(R.layout.activity_news_intro);
            Disposable disposable = Completable.complete()
                    .delay(2, TimeUnit.SECONDS)
                    .subscribe(this::startSecondActivity);
            compositeDisposable.add(disposable);
        } else {
            startSecondActivity();
        }
    }

    private void startSecondActivity() {
        startActivity(new Intent(this, NewsMainActivity.class));
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.dispose();
    }
}
