package com.example.roman.listofnews;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.roman.listofnews.data.Storage;
import com.example.roman.listofnews.data.dataBase.NewsDatabaseRepository;
import com.example.roman.listofnews.mvp.NewsListPresenter;
import com.example.roman.listofnews.mvp.NewsListView;
import com.example.roman.listofnews.ui.NewsDetailsFragmentListener;
import com.example.roman.listofnews.ui.SetTitleActionBarListener;
import com.example.roman.listofnews.ui.State;
import com.example.roman.listofnews.ui.adapter.AllNewsItem;
import com.example.roman.listofnews.ui.adapter.pagedListAdapter.EmployeeStorage;
import com.example.roman.listofnews.ui.adapter.pagedListAdapter.NewsItemDiffUtilItemCallback;
import com.example.roman.listofnews.ui.adapter.pagedListAdapter.NewsPagedListAdapter;
import com.example.roman.listofnews.ui.adapter.pagedListAdapter.NewsSourceFactory;
import com.example.roman.listofnews.ui.adapter.spinner.CategoriesSpinnerAdapter;
import com.example.roman.listofnews.ux.NewsCategory;
import com.example.roman.listofnews.ux.RestApi;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

import io.reactivex.disposables.CompositeDisposable;

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
    private CategoriesSpinnerAdapter categoriesAdapter;
    @Nullable
    NewsItemDiffUtilItemCallback newsItemDiffUtilItemCallback;
    @Nullable
    private NewsPagedListAdapter newsPagedAdapter;
    @Nullable
    private EmployeeStorage employeeStorage;
    @Nullable
    private LinearLayoutManager llm;
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
    @Nullable
    private NewsSourceFactory newsSourceFactory;
    private static final String TAG = "myLogs";
    private int mScrollOffsetFAB = 4;
    private NewsDetailsFragmentListener listener;
    private boolean isTwoPanel;
    static final String ARGUMENT_IS_TWO_PANEL = "arg_is_two_panel";
    private String currentState;
    private Integer currentListItem = 0;
    private int startLoadKey;
    private final int pageSizePagedList = 5;
    private SetTitleActionBarListener titleActionBarListener;

    private Context context;

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
        this.context = context;
        newsDatabaseRepository = new NewsDatabaseRepository(context);
        if (context instanceof NewsDetailsFragmentListener) {
            listener = (NewsDetailsFragmentListener) context;
        }
        if (context instanceof SetTitleActionBarListener) {
            titleActionBarListener = (SetTitleActionBarListener) context;
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
        if (Storage.getCurrentListItem(getActivity()) < 0) {
            Storage.setCurrentListItem(getActivity(), 0);
        }
        setupUi(view);
        Log.d(TAG, "--- ListFragment onCreateView");
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "--- ListFragment onStart");
        setupUx();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title bar
        titleActionBarListener.onSetTitleActionBar();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "--- ListFragment onPause");
        savingCurrentValuesView();
        unbindUx();
        super.onPause();
    }

    private void savingCurrentValuesView() {
        // for test
        Storage.setCurrentState(getActivity(), currentState);
        Log.d(TAG, "*** onPause " + currentState);
        if (llm != null) {
            if (employeeStorage != null) {
                currentListItem = llm.findFirstVisibleItemPosition() + employeeStorage.outMinStartPosition();
            }
        }
        Storage.setCurrentListItem(getActivity(), currentListItem);
        Log.d(TAG, "*** onPause #" + currentListItem.toString());
    }

    private void unbindUx() {
        btnTryAgain.setOnClickListener(null);
        fabUpdate.setOnClickListener(null);
    }

    private void setupUi(View view) {
        findView(view);
        setupSpinner();
        setupScroll();
        currentState = Storage.getCurrentState(getActivity());
        Log.d(TAG, "*** setupUi " + currentState);
        newsListPresenter.setCurrentScreenState(newsDatabaseRepository, currentState);

    }

    private void setupUx() {
        spinnerCategories.setOnTouchListener(spinnerOnTouch);
        fabUpdate.setOnClickListener(v -> onClickFabUpdate(categoriesAdapter.getSelectedCategory().serverValue()));
        btnTryAgain.setOnClickListener(v -> onClickTryAgain(categoriesAdapter.getSelectedCategory().serverValue()));
    }

    private View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                categoriesAdapter.setOnCategorySelectedListener(category -> {
                    Log.d(TAG, "--- ListFragment onSelected");
                    //cleaning saved Current List Item position
                    Storage.setCurrentListItem(getActivity(), 0);
                    newsListPresenter.loadItem(category.serverValue(), newsDatabaseRepository);
                }, spinnerCategories);
            }
            return false;
        }
    };

    private void onClickFabUpdate(@NonNull String category) {
        //cleaning saved Current List Item position
        Storage.setCurrentListItem(getActivity(), 0);
        newsListPresenter.loadItem(category, newsDatabaseRepository);
    }

    private void onClickTryAgain(@NonNull String category) {
        //cleaning saved Current List Item position
        Storage.setCurrentListItem(getActivity(), 0);
        newsListPresenter.loadItem(category, newsDatabaseRepository);
    }

    @Override
    public void updateItems(@Nullable List<AllNewsItem> news) {
        if (news.size() > 0) {
            setupPagedListAdapter(news);
        }
    }

    /**
     * ***************************************************************************************************
     **/

    @Override
    public void showState(@NonNull State state) {

        switch (state) {
            case Loading:
                currentState = State.Loading.name();
                rvNews.setVisibility(View.GONE);
                viewError.setVisibility(View.GONE);

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

    private void setupPagedListAdapter(List<AllNewsItem> news) {
        newsItemDiffUtilItemCallback = new NewsItemDiffUtilItemCallback();
        newsPagedAdapter = new NewsPagedListAdapter(newsItemDiffUtilItemCallback, context);
        startLoadKey = Storage.getCurrentListItem(getActivity());
        employeeStorage = new EmployeeStorage(news, startLoadKey);
        newsSourceFactory = new NewsSourceFactory(employeeStorage);

        setupPagedList();
        llm = new LinearLayoutManager(getActivity());
        rvNews.setLayoutManager(llm);
        rvNews.setAdapter(newsPagedAdapter);
        setupClickNewsItem();
    }

    private void setupPagedList() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(pageSizePagedList)
                .build();

        LiveData<PagedList<AllNewsItem>> pagedListLiveData = new LivePagedListBuilder<>(
                newsSourceFactory,
                config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .setInitialLoadKey(startLoadKey)
                .build();
        pagedListLiveData.observe(getActivity(), allNewsItems -> {
            Log.d(TAG, "### submit PagedList");
            newsPagedAdapter.submitList(allNewsItems);
        });
    }

    private void setupClickNewsItem() {
        newsPagedAdapter.setOnClickNewsListener(IdItem -> {
            if (listener != null) {
                listener.onNewsDetailsByIdClicked(IdItem);
            }
        });
    }

    private void setupSpinner() {
        final NewsCategory[] categories = NewsCategory.values();
        categoriesAdapter = CategoriesSpinnerAdapter.createDefault(getActivity(), categories);
        spinnerCategories.setAdapter(categoriesAdapter);
        spinnerCategories.setSelection(Storage.getSelectedPositionCategory(getActivity()));
    }

    private void setupScroll() {
        rvNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > mScrollOffsetFAB) {
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
    public void onDestroy() {
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
