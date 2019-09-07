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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roman.listofnews.ui.adapter.introPager.IntroFragmentPagerAdapter;
import com.example.roman.listofnews.ui.NewsIntroFragmentListener;

import me.relex.circleindicator.CircleIndicator;

public class NewsIntroFragment extends Fragment {

    static final String TAG = "myLogs";
    private static final int pageCount = 5; //number of fragments in intro
    ViewPager pager;
    PagerAdapter pagerAdapter;
    private NewsIntroFragmentListener listener;

    //private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof NewsIntroFragmentListener) {
            listener = (NewsIntroFragmentListener) getActivity();
            Log.d(TAG, "--- IntroFragment onAttach");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_intro, container, false);
        setupUi(view);
        Log.d(TAG, "--- IntroFragment onCreateView");
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
                if (position == 4) {
                    if (listener != null) listener.onNewsIntroFragmentClose();
                } //startMainActivity(); }
            }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
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

    @Override
    public void onDetach() {
        listener = null;
        Log.d(TAG, "--- IntroFragment onDetach");
        super.onDetach();
    }
}
