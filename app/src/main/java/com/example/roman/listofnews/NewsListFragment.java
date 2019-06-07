package com.example.roman.listofnews;

import android.content.Context;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.roman.listofnews.data.Storage;
import com.example.roman.listofnews.data.dataBase.NewsDatabaseConverter;
import com.example.roman.listofnews.data.dataBase.NewsDatabaseRepository;
import com.example.roman.listofnews.data.dataBase.NewsEntity;
import com.example.roman.listofnews.ui.NewsDetailsFragmentListener;
import com.example.roman.listofnews.ui.adapter.AllNewsItem;
import com.example.roman.listofnews.ui.adapter.spinner.CategoriesSpinnerAdapter;
import com.example.roman.listofnews.ux.NewsCategory;
import com.example.roman.listofnews.ui.adapter.NewsRecyclerAdapter;
import com.example.roman.listofnews.ui.State;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewsListFragment extends Fragment {

    @Nullable
    private RecyclerView rvNews;
    @Nullable
    private Spinner spinnerCategories;
    @Nullable
    private NewsRecyclerAdapter NewsAdapter;
    @Nullable
    private CategoriesSpinnerAdapter categoriesAdapter;

    @Nullable
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
    private FloatingActionButton fabUpdate;

    /*@Nullable
    private Button errorAction;*/


    @Nullable
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Nullable
    private NewsDatabaseRepository newsDatabaseRepository;
    @Nullable
    private NewsDatabaseConverter databaseConverter = new NewsDatabaseConverter();
    private static final String TAG = "myLogs";
    private int mScrollOffset = 4;
    private NewsDetailsFragmentListener listener;


    /*public interface OnItemClickListener {
        void OnItemClick(int position); }*/
    //@Nullable
    //private Call<DefaultResponse<List<NewsItemDTO>>> ResponseTSHome;

    /*private final NewsRecyclerAdapter.OnItemClickListener  newsListener = position -> {
        final String EXTRA_MESSAGE = "extra:news";
        Intent intent = new Intent(this, NewsDetailsFragment.class);
        intent.putExtra(EXTRA_MESSAGE, position);
        startActivity(intent);
    };*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        newsDatabaseRepository = new NewsDatabaseRepository(getActivity());
        //checking where context
        if (getActivity() instanceof NewsDetailsFragmentListener) {
            listener = (NewsDetailsFragmentListener) getActivity();
        }
        Log.d(TAG, "--- ListFragment onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "--- ListFragment onCreate");
        //setContentView(R.layout.fragment_news_recycler);
        //setupUi();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_recycler, container, false);
        Log.d(TAG, "--- ListFragment onCreateView");
        setupUi(view);
        Log.d(TAG, "ListFragment setupUi");
        return view; //super.onCreateView(inflater, container, savedInstanceState);

    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "--- ListFragment onStart");
        setupUx();
        Log.d(TAG, "ListFragment setupUx");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "--- ListFragment onPause");
        unbindUx();
        super.onPause();
    }

    private void unbindUx() {
        btnTryAgain.setOnClickListener(null);
    }

    private void setupUi(View view) {
        findView(view);
        setupRecyclerViews();
        setupSpinner();
        setupFabScroll();

    }


    private void setupUx() {
        //errorAction.setOnClickListener(view -> loadItem(categoriesAdapter.getSelectedCategory().serverValue()));
        //categoriesAdapter.setOnCategorySelectedListener(category -> loadItem(category.serverValue()), spinnerCategories);
        fabUpdate.setOnClickListener(v -> onClickFabUpdate());
        btnTryAgain.setOnClickListener(v -> onClickTryAgain(categoriesAdapter.getSelectedCategory().serverValue()));
        NewsAdapter.setOnClickNewsListener(IdItem ->
                //NewsDetailsFragment.start(getActivity(), IdItem));
            { if (listener != null){
                listener.onNewsDetailsByIdClicked(IdItem);
            }
        });
        checkingDatabaseForEmptiness();
    }

    private void checkingDatabase(int cnt) {
        //if DB not emptiness - declare categoriesAdapter and view items from DB on screen
         if (cnt > 0) {

             showState(State.HasData);
             Log.d(TAG, "Database not emptiness");
             categoriesAdapter.setOnCategorySelectedListener(category -> loadItem(category.serverValue()),spinnerCategories);

             initViews();
             Log.d(TAG, "categoriesAdapter.setOnCategorySelectedListener");
         }
    }

    private void onClickFabUpdate() {
        loadItem(categoriesAdapter.getSelectedCategory().serverValue());
    }

    private void onClickTryAgain(@NonNull String category ) {
        loadItem(category);
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

    private void setupNews(List<AllNewsItem> newsItems) {
        showState(State.HasData);
        updateItems(newsItems);
        //clear database before update
        deleteAllFromDatabaseWithRoom();
        //Convert to Entities and save List AllNewsItem to database
        saveToDatabaseWithRoom(databaseConverter.toDatabase(newsItems));
    }

    private void updateItems(@Nullable List<AllNewsItem> news) {
        if (NewsAdapter != null) NewsAdapter.replaceItems(news);
    }

    private void handleError (Throwable throwable) {
        if (throwable instanceof IOException) {
            showState(State.NetworkError);
            return;
        }
        showState(State.NetworkError);
    }

/**
********************************************Database methods****************************************
**/

    private void saveToDatabaseWithRoom(List<NewsEntity> NewsEntityList) {
        Disposable disposable = newsDatabaseRepository.saveToDatabase(NewsEntityList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () ->
                                //Log.d(TAGroom, NewsEntityList.toString()),
                                Log.d(TAG, "save NewsEntityList To Database"),
                        throwable ->
                                Log.e(TAG, throwable.toString()));
        compositeDisposable.add(disposable);
    }

    private void initViews() {
        Disposable disposable = newsDatabaseRepository.getDataFromDatabase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new Consumer<List<NewsEntity>>() {
                    @Override
                    public void accept(List<NewsEntity> newsEntities) throws Exception {
                        //Log.d(TAGroom, newsEntities.toString());

                        // updating Items in RecyclerView from Database with converting Entities to AllNewsItem
                        updateItems(databaseConverter.fromDatabase(newsEntities)) ;
                        Log.d(TAG, "updating Items in RecyclerView from Database");
                    }
                } ,
                            new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void deleteAllFromDatabaseWithRoom() {
        Disposable disposable = newsDatabaseRepository.deleteAllFromDatabase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () ->
                                Log.d(TAG, "deleteAllFromDatabase"),
                        throwable ->
                                Log.e(TAG, throwable.toString()));
        compositeDisposable.add(disposable);
    }

    private void checkingDatabaseForEmptiness() {
        Disposable disposable = newsDatabaseRepository.checkDataInDatabase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::checkingDatabase);
        compositeDisposable.add(disposable);
    }

    //getinEntitybyIdFromDatabase  getEntitybyIdFromDatabase



    /*private void subscribeToData() {
        Disposable disposable = newsDatabaseRepository.getDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new Consumer<List<NewsEntity>>() {
                    @Override
                    public void accept(List<NewsEntity> newsEntities) throws Exception {
                        Log.d(TAGroom, newsEntities.toString());
                        //

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAGroom, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }*/

/**
****************************************************************************************************
**/
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

    private void findView(View view) {
        rvNews = view.findViewById(R.id.rv_news);
        viewLoading = view.findViewById(R.id.fl_loading);
        viewNoDate = view.findViewById(R.id.fl_no_data);
        viewError = view.findViewById(R.id.ll_error);
        tvError = view.findViewById(R.id.tv_error);
        btnTryAgain = view.findViewById(R.id.bnt_try_again);
        spinnerCategories = view.findViewById(R.id.spinner_categories);
        fabUpdate = view.findViewById(R.id.fab_update);
    }
    private void setupRecyclerViews() {
        NewsAdapter = new NewsRecyclerAdapter(getActivity());
        rvNews.setAdapter(NewsAdapter);
        int orientation = getResources().getConfiguration().orientation;
        onChangeColumnsWithOrientation(orientation, rvNews);
    }

    public void onChangeColumnsWithOrientation(int orientation, RecyclerView recyclerView) {
        //This method helps change number of columns using Grid spanCount and helps add itemDecoration
        // Checks the orientation of the screen
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        /*if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL);
            itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getActivity(), R.drawable.item_ecoration_size_4)));
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerView.addItemDecoration(itemDecorator);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            DividerItemDecoration itemDecorator= new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getActivity(), R.drawable.item_ecoration_size_4)));
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            recyclerView.addItemDecoration(itemDecorator);
        }*/
    }

    private void setupSpinner() {
        final NewsCategory[] categories = NewsCategory.values();
        categoriesAdapter = CategoriesSpinnerAdapter.createDefault(getActivity(), categories);
        spinnerCategories.setAdapter(categoriesAdapter);
    }

    private void setupFabScroll() {
        rvNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > mScrollOffset) {
                    if (dy > 0) {
                        fabUpdate.hide();
                    } else {
                        fabUpdate.show();
                    }

                }
            }
        });

    }








    @Override
    public void onDestroy(){

        compositeDisposable.dispose();
        super.onDestroy();
        Log.d(TAG, "--- ListFragment onDestroy");
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
        Log.d(TAG, "--- ListFragment onDetach");
    }
}
