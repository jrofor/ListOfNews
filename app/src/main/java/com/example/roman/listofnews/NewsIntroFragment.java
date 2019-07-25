package com.example.roman.listofnews;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roman.listofnews.data.Storage;
import com.example.roman.listofnews.ui.adapter.introPager.IntroFragmentPagerAdapter;
import com.example.roman.listofnews.ui.NewsIntroFragmentClose;

import me.relex.circleindicator.CircleIndicator;

public class NewsIntroFragment extends Fragment {

    static final String TAG = "myLogs";
    private static final int pageCount = 5; //number of fragments in intro
    ViewPager pager;
    PagerAdapter pagerAdapter;
    private NewsIntroFragmentClose listener;

    //private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_intro, container, false);
        setupUi(view);
        Log.d(TAG, "NewsIntroFragment onCreate");
            if (!Storage.openFirstTime(getContext())) {
                Storage.setIntroHasShown(getContext());
                Log.d(TAG, "Storage IntroHasShown");
            }
        return view;
        }

    private void setupUi(View view){
        pager = (ViewPager) view.findViewById(R.id.vp_Pager);
        pagerAdapter = new IntroFragmentPagerAdapter(getChildFragmentManager(), pageCount, getContext());
        pager.setAdapter(pagerAdapter);

        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.ci_indicator);
        indicator.setViewPager(pager);

        pagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
        pager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position = " + position);
                if (position == 4) listener.onNewsIntroFragmentClose(); } //startMainActivity(); }
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

    private void startMainActivity() {
        startActivity(new Intent(getContext(), MainActivity.class));
        //finish();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        //compositeDisposable.dispose();
    }
}
