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
import com.example.roman.listofnews.ui.adapter.AllNewsItem;
import com.example.roman.listofnews.ux.NewsDTO.NewsItemDTO;
import com.example.roman.listofnews.ui.adapter.NewsRecyclerAdapter;
import com.example.roman.listofnews.ui.State;
import com.example.roman.listofnews.ux.DefaultResponse;
import com.example.roman.listofnews.ux.RestApi;
import com.example.roman.listofnews.ux.TopStoriesMapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsMainActivity extends AppCompatActivity {

    @Nullable
    private RecyclerView rvNews;
    private NewsRecyclerAdapter NewsAdapter;
    private View viewLoading;
    @Nullable
    private View viewNoDate;
    @Nullable
    private View viewError;
    @Nullable
    private TextView tvError;
    @Nullable
    private Button btnTryAgain;

    @Nullable
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    private Call<DefaultResponse<List<NewsItemDTO>>> ResponseTSHome;

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
        loadItem("home");
        btnTryAgain.setOnClickListener(v -> onClickTryAgain());

    }

    private void onClickTryAgain() {
        loadItem("home");
    }

    private void loadItem(@NonNull String category) {
        showState(State.Loading);
        final Disposable disposable = (Disposable) RestApi.getInstanse()
                .getTSEndpoint()
                .setSectionName(category)
                .map(response ->
                        TopStoriesMapper
                                .map(response
                                        .getNews()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setupNews,
                        this::handleError);
        compositeDisposable.add(disposable);




        /*ResponseTSHome.enqueue(new Callback<DefaultResponse<List<NewsItemDTO>>>() {
            @Override
            public void onResponse(@NonNull Call<DefaultResponse<List<NewsItemDTO>>> call,
                                   @NonNull Response<DefaultResponse<List<NewsItemDTO>>> response) {
                checkResponseAndShowState(response);
            }

            @Override
            public void onFailure(@NonNull  Call<DefaultResponse<List<NewsItemDTO>>> call,
                                  @NonNull Throwable t) {
                handleError(t);
            }
        });*/
    }

    //private void setupNews(List<NewsItemDTO> newsItems) {
    private void setupNews(List<AllNewsItem> newsItems) {
        showState(State.HasData);
        updateItems(newsItems);
    }

    //private void updateItems(@Nullable List<NewsItemDTO> news) {
    private void updateItems(@Nullable List<AllNewsItem> news) {
        if (NewsAdapter != null) NewsAdapter.replaceItems(news);
    }

    //private void checkResponseAndShowState(@NonNull Response<DefaultResponse<List<NewsItemDTO>>> response) {
    /*private void checkResponseAndShowState(@NonNull Response<List<AllNewsItem>> response) {

        if (!response.isSuccessful()) {
            showState(State.ServerError);
            return;
        }

        //final DefaultResponse<List<NewsItemDTO>> body = response.body();
        final List<AllNewsItem> body = response.body();

        if (body == null) {
            showState(State.HasData);
            return;
        }

        final List<NewsItemDTO> data = body.getData();
        final List<NewsItemDTO> data = body.getData();

        if (data == null) {
            showState(State.HasNoData);
            return;
        }

        if (data.isEmpty()) {
            showState(State.HasNoData);
            return;
        }

        //NewsAdapter.replaceItems(data);
         NewsAdapter.replaceItems(body);
        showState(State.HasData);
    } */

    private void handleError (Throwable throwable) {
        if (throwable instanceof IOException) {
            showState(State.NetworkError);
            return;
        }
        showState(State.NetworkError);
    }

    public void showState(@NonNull State state) {

        switch (state) {
            case Loading:

                rvNews.setVisibility(View.GONE);
                viewError.setVisibility(View.GONE);
                viewNoDate.setVisibility(View.GONE);

                viewLoading.setVisibility(View.VISIBLE);
                break;

            case ServerError:
                rvNews.setVisibility(View.GONE);
                viewNoDate.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);

                tvError.setText(getText(R.string.error_server));
                viewError.setVisibility(View.VISIBLE);
                break;

            case HasData:
                viewError.setVisibility(View.GONE);
                viewNoDate.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);

                rvNews.setVisibility(View.VISIBLE);
                break;

            case HasNoData:
                viewError.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                rvNews.setVisibility(View.GONE);

                viewNoDate.setVisibility(View.VISIBLE);
                break;

            case NetworkError:
                rvNews.setVisibility(View.GONE);
                viewNoDate.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);

                tvError.setText(getText(R.string.error_network));
                viewError.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void findView() {
        rvNews = findViewById(R.id.rv_news);
        viewLoading = findViewById(R.id.fl_loading);
        viewNoDate = findViewById(R.id.fl_no_data);
        viewError = findViewById(R.id.ll_error);
        tvError = findViewById(R.id.tv_error);
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
