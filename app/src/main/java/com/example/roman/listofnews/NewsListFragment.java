package com.example.roman.listofnews;

import android.content.Context;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.roman.listofnews.data.Storage;
import com.example.roman.listofnews.data.dataBase.NewsDatabaseConverter;
import com.example.roman.listofnews.data.dataBase.NewsDatabaseRepository;
import com.example.roman.listofnews.data.dataBase.NewsEntity;
import com.example.roman.listofnews.mvp.NewsListPresenter;
import com.example.roman.listofnews.mvp.NewsListView;
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
import android.view.MotionEvent;
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

public class NewsListFragment extends MvpAppCompatFragment implements NewsListView {

    private static final int LAYOUT = R.layout.fragment_news_list;

    @InjectPresenter
    NewsListPresenter newsListPresenter;

    @ProvidePresenter
    NewsListPresenter provideNewsListPresenter() {
        return new NewsListPresenter(RestApi.getInstanse());
    }

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
    private View viewError;
    @Nullable
    private TextView tvError;
    @Nullable
    private Button btnTryAgain;
    @Nullable
    private FloatingActionButton fabUpdate;
    @Nullable
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Nullable
    private NewsDatabaseRepository newsDatabaseRepository;
    private static final String TAG = "myLogs";
    private int mScrollOffset = 4;
    private NewsDetailsFragmentListener listener;
    private boolean isTwoPanel;
    static final String ARGUMENT_IS_TWO_PANEL = "arg_is_two_panel";
    private String currentState;

    public static NewsListFragment newInstance(boolean isTwoPanel) {
        NewsListFragment newsListFragment = new NewsListFragment();
        Bundle arguments = new Bundle();
        arguments.putBoolean(ARGUMENT_IS_TWO_PANEL, isTwoPanel);
        newsListFragment.setArguments(arguments);
        return newsListFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        newsDatabaseRepository = new NewsDatabaseRepository(getActivity());
        //checking where context
        if (getActivity() instanceof NewsDetailsFragmentListener) {
            listener = (NewsDetailsFragmentListener) getActivity();
        }

        isTwoPanel = getArguments().getBoolean(ARGUMENT_IS_TWO_PANEL);
        Log.d(TAG, "--- ListFragment onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "--- ListFragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        Log.d(TAG, "--- ListFragment onCreateView");
        setupUi(view);
        //Log.d(TAG, "ListFragment setupUi");
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "--- ListFragment onStart");
        setupUx();
        //Log.d(TAG, "ListFragment setupUx");
        //checkingDatabaseForEmptiness();
    }

    @Override
    public void onResume(){
        super.onResume();
        // Set title bar
        ((MainActivity) Objects.requireNonNull(getActivity())).setupActionBar(getString(R.string.app_name), false);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "--- ListFragment onPause");
        unbindUx();
        Storage.setCurrentState(getActivity(), currentState);
        Log.d(TAG, "*** onPause " + currentState);
        //newsListPresenter.saveCurrentState(currentState, newsDatabaseRepository);
        super.onPause();
    }

    private void unbindUx() {
        btnTryAgain.setOnClickListener(null);
        fabUpdate.setOnClickListener(null);
    }

    private void setupUi(View view) {
        findView(view);
        setupRecyclerViews(view);
        setupSpinner();
        setupFabScroll();
        currentState = Storage.getCurrentState(getActivity());
        Log.d(TAG, "*** setupUi " + currentState);
        newsListPresenter.setCurrentScreenState(newsDatabaseRepository, currentState);
    }

    private void setupUx() {
        spinnerCategories.setOnTouchListener(spinnerOnTouch);
        fabUpdate.setOnClickListener(v -> onClickFabUpdate(categoriesAdapter.getSelectedCategory().serverValue()));
        btnTryAgain.setOnClickListener(v -> onClickTryAgain(categoriesAdapter.getSelectedCategory().serverValue()));
        NewsAdapter.setOnClickNewsListener(IdItem -> {
            if (listener != null) {
                listener.onNewsDetailsByIdClicked(IdItem);
            }
        });
    }

    private View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                categoriesAdapter.setOnCategorySelectedListener(new CategoriesSpinnerAdapter.OnCategorySelectedListener() {
                    @Override
                    public void onSelected(@NonNull NewsCategory category) {
                        Log.d(TAG, "--- ListFragment onSelected");
                        newsListPresenter.loadItem(category.serverValue(), newsDatabaseRepository);
                    }
                }, spinnerCategories);
            }
            return false;
        }
    };


    private void onClickFabUpdate(@NonNull String category) {
        newsListPresenter.loadItem(category, newsDatabaseRepository);
    }

    private void onClickTryAgain(@NonNull String category) {
        newsListPresenter.loadItem(category, newsDatabaseRepository);
    }

    @Override
    public void updateItems(@Nullable List<AllNewsItem> news) {
        if (NewsAdapter != null) NewsAdapter.replaceItems(news);

    }

/**
****************************************************************************************************
**/

    @Override
    public void showState(@NonNull State state) {

        switch (state) {
            case Loading:
                currentState = State.Loading.name();
                rvNews.setVisibility(View.GONE);
                viewError.setVisibility(View.GONE);
                //viewNoDate.setVisibility(View.GONE);

                viewLoading.setVisibility(View.VISIBLE);
                break;

            case HasData:
                currentState = State.HasData.name();
                viewError.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);

                rvNews.setVisibility(View.VISIBLE);
                break;

            case ServerError:
                currentState = State.ServerError.name();
                rvNews.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);

                tvError.setText(getText(R.string.error_server));
                viewError.setVisibility(View.VISIBLE);
                break;

            case HasNoData:
                currentState = State.HasNoData.name();
                viewLoading.setVisibility(View.GONE);
                rvNews.setVisibility(View.GONE);

                tvError.setText(getText(R.string.err_data_is_empty));
                viewError.setVisibility(View.VISIBLE);
                break;

            case NetworkError:
                currentState = State.NetworkError.name();
                rvNews.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);

                tvError.setText(getText(R.string.error_network));
                viewError.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void findView(View view) {
        rvNews = view.findViewById(R.id.rv_news);
        viewLoading = view.findViewById(R.id.fl_loading);
        viewError = view.findViewById(R.id.ll_error);
        tvError = view.findViewById(R.id.tv_error);
        btnTryAgain = view.findViewById(R.id.bnt_try_again);
        spinnerCategories = view.findViewById(R.id.spinner_categories);
        fabUpdate = view.findViewById(R.id.fab_update);
    }
    private void setupRecyclerViews(View view) {
        NewsAdapter = new NewsRecyclerAdapter(getActivity());
        rvNews.setAdapter(NewsAdapter);
        int orientation = getResources().getConfiguration().orientation;
        onChangeColumnsWithOrientation(orientation, rvNews, view);
    }

    public boolean onCheckIsTwoPanel(boolean isTwoPanel){
        return isTwoPanel;
    }

    public void onChangeColumnsWithOrientation(int orientation, RecyclerView recyclerView, View view) {
        //This method helps change number of columns using Grid spanCount and helps add itemDecoration
        // Checks the orientation of the screen and TwoPanel fragments for Tablets (if landscape on Tablets 1 column news)
        /*if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }*/
            /*if (orientation == Configuration.ORIENTATION_PORTRAIT && !isTwoPanel)*/
            DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getActivity(), R.drawable.item_ecoration_size_4)));
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            recyclerView.addItemDecoration(itemDecorator);

        /*if ((view.findViewById(R.id.tablet_ll)) == null && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DividerItemDecoration itemDecorator1 = new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL);
            itemDecorator1.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getActivity(), R.drawable.item_ecoration_size_4)));
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerView.addItemDecoration(itemDecorator1);
        }*/

    }

    private void setupSpinner() {
        final NewsCategory[] categories = NewsCategory.values();
        categoriesAdapter = CategoriesSpinnerAdapter.createDefault(getActivity(), categories);
        spinnerCategories.setAdapter(categoriesAdapter);
        spinnerCategories.setSelection(Storage.getSelectedPositionCategory(getActivity()));
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
