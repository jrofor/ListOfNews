package com.example.roman.listofnews.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.roman.listofnews.NewsListFragment;
import com.example.roman.listofnews.R;

public class PageFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    int pageNumber;

    public static PageFragment newInstance(int page) {
        PageFragment pageFragment  = new PageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(bundle);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pagefragment_news_intro, null);
        ImageView ivPage = (ImageView) view.findViewById(R.id.iv_intro_Page);
        //tvPage.setText("Page" + pageNumber);

        switch (pageNumber) {
            case 0:
                ivPage.setImageResource(R.drawable.device_list_of_news);
                break;
            case 1:
                ivPage.setImageResource(R.drawable.device_details_of_the_news);
                break;
            case 2:
                ivPage.setImageResource(R.drawable.device_for_tablet);
                break;
            case 3:
                ivPage.setImageResource(R.drawable.device_about_app);
                break;
        }
        return view;
    }

}
