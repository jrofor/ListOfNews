package com.example.roman.listofnews.ui.adapter.introPager;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.roman.listofnews.R;
import com.example.roman.listofnews.ui.PageFragment;

public class IntroFragmentPagerAdapter extends FragmentPagerAdapter {
    private static int PAGE_COUNT;
    private String PageTitle_0;
    private String PageTitle_1;
    private String PageTitle_2;
    private String PageTitle_3;
    private String PageTitle_4;

    public IntroFragmentPagerAdapter(FragmentManager fm, int pageCount, Context context) {
        super(fm);
        PAGE_COUNT = pageCount;
        Resources res = context.getResources();
        PageTitle_0 = res.getString(R.string.list_of_news);
        PageTitle_1 = res.getString(R.string.details_of_the_news);
        PageTitle_2 = res.getString(R.string.version_for_tablets);
        PageTitle_3 = res.getString(R.string.about_app);
        PageTitle_4 = res.getString(R.string.good_news);
    }

 /*     @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        return super.instantiateItem(container, position);
        /*Object ret = super.instantiateItem(container, position);
        PageFragment  = (PageFragment) ret;
        return PageFragment.newInstance(position);
    }

      @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Object ret = super.instantiateItem(container, position);
            mFragments[position] = (Fragment) ret;
            return ret;

*/
    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String PageTitle = null;

        switch (position) {
            case 0:
                PageTitle = PageTitle_0;
                break;
            case 1:
                PageTitle = PageTitle_1;
                break;
            case 2:
                PageTitle = PageTitle_2;
                break;
            case 3:
                PageTitle = PageTitle_3;
                break;
            case 4:
                PageTitle = PageTitle_4;
                break;
        }
        return PageTitle;
    }


}