package com.example.roman.listofnews;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.example.roman.listofnews.data.Storage;
import com.example.roman.listofnews.ui.adapter.introPager.IntroFragmentPagerAdapter;

import me.relex.circleindicator.CircleIndicator;

public class NewsIntroActivity extends FragmentActivity {

    static final String TAG = "myLogs";
    private static final int pageCount = 5; //number of fragments in intro
    ViewPager pager;
    PagerAdapter pagerAdapter;

    //private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_news_intro);
        Log.d(TAG, "NewsIntroActivity onCreate");
        if (!Storage.openFirstTime(this)) {
            Storage.setIntroHasShown(this);
            Log.d(TAG, "Storage IntroHasShown");
        }
        pager = (ViewPager) findViewById(R.id.vp_Pager);
        pagerAdapter = new IntroFragmentPagerAdapter(getSupportFragmentManager(), pageCount, this);
        pager.setAdapter(pagerAdapter);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.ci_indicator);
        indicator.setViewPager(pager);

        pagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
        pager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position = " + position);
                if (position == 4) startSecondActivity(); }
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) { }
                @Override
                public void onPageScrollStateChanged(int state) { }});
        /*Disposable disposable = Completable.complete()
                   .delay(10, TimeUnit.SECONDS)
                   .subscribe(this::startSecondActivity);
           compositeDisposable.add(disposable);*/

        }

    private void startSecondActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //compositeDisposable.dispose();
    }
}
