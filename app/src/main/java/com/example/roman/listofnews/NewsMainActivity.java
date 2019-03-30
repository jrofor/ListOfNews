package com.example.roman.listofnews;

import android.content.Intent;

import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.example.roman.listofnews.data.NewsItemDTO;
import com.example.roman.listofnews.ui.adapter.NewsRecyclerAdapter;
import com.example.roman.listofnews.ui.State;
import com.example.roman.listofnews.ux.DefaultResponse;

import java.util.List;
import java.util.Objects;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import retrofit2.Call;

public class NewsMainActivity extends AppCompatActivity {

    private RecyclerView rvNews;
    private NewsRecyclerAdapter NewsAdapter;
    private View viewLoading;
    private View viewNoDate;
    private View viewError;
    private Button btnTryAgain;

    @Nullable
    private Call<DefaultResponse<List<NewsItemDTO>>> ResponseTopStoriesHome;

    private final NewsRecyclerAdapter.OnItemClickListener  clickListener = position -> {
        final String EXTRA_MESSAGE = "extra:news";
        Intent intent = new Intent(this, NewsDetailsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, position);
        startActivity(intent);
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_recycler_activity);
        setupUi();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupUx();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindUx();
    }

    private void unbindUx() {
        btnTryAgain.setOnClickListener(null);
    }

    private void setupUi() {
        findView();
        setupRecyclerViews();
    }

    private void setupUx() {
        loadTopStoriesHome();

    }


    private void loadTopStoriesHome() {
        showState(State.Loading);


    }

    public void showState(@NonNull State state) {
        switch (state) {
            case Loading:
                rvNews.setVisibility(View.GONE);
                viewError.setVisibility(View.GONE);
                viewNoDate.setVisibility(View.GONE);

                viewLoading.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void findView() {
        rvNews = findViewById(R.id.rv_news);
        viewLoading = findViewById(R.id.fl_loading);
        viewNoDate = findViewById(R.id.fl_no_data);
        viewError = findViewById(R.id.ll_error);
        btnTryAgain = findViewById(R.id.bnt_try_again);

    }

    private void setupRecyclerViews() {
        NewsAdapter = new NewsRecyclerAdapter(Glide.with(this), clickListener);
        rvNews.setAdapter(NewsAdapter);
        int orientation = getResources().getConfiguration().orientation;
        onChangeColumnsWithOrientation(orientation, rvNews);
    }

    public void onChangeColumnsWithOrientation(int orientation, RecyclerView recyclerView) {
        //This method helps change number of columns using Grid spanCount and helps add itemDecoration
        // Checks the orientation of the screen
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
            itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.item_ecoration_size_4)));
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.addItemDecoration(itemDecorator);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            DividerItemDecoration itemDecorator= new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.item_ecoration_size_4)));
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            recyclerView.addItemDecoration(itemDecorator);
        }
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item1:
                startActivity(new Intent(this, NewsAboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }






}
