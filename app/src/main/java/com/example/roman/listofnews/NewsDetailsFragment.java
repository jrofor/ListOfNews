package com.example.roman.listofnews;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.example.roman.listofnews.data.dataBase.NewsDatabaseRepository;
import com.example.roman.listofnews.data.dataBase.NewsEntity;
import com.example.roman.listofnews.mvp.NewsDetailsPresenter;
import com.example.roman.listofnews.mvp.NewsDetailsView;
import com.example.roman.listofnews.ui.SetTitleActionBarListener;

public class NewsDetailsFragment extends MvpAppCompatFragment implements NewsDetailsView {

    private static final int LAYOUT = R.layout.fragment_news_details;

    @InjectPresenter
    NewsDetailsPresenter newsDetailsPresenter;

    @Nullable
    TextView titleView;
    @Nullable
    TextView fullTextView;
    @Nullable
    TextView categoryView;
    @Nullable
    ImageView imageView;
    @Nullable
    TextView textDateView;

    private static final String TAG = "myLogs";
    private static final String ARGUMENT_NEWS_ITEM = "arg_idNewsItem"; //args:
    private String idItem;
    @NonNull
    private NewsDatabaseRepository newsDatabaseRepository = new NewsDatabaseRepository(getActivity());
    private MutableLiveData<String> liveTitActBar = new MutableLiveData<>();
    private SetTitleActionBarListener titleActionBarListener;

    public static NewsDetailsFragment newInstance(String idNewsItem) {
        NewsDetailsFragment newsDetailsFragment = new NewsDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_NEWS_ITEM, idNewsItem);
        newsDetailsFragment.setArguments(bundle);
        return newsDetailsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        newsDatabaseRepository = new NewsDatabaseRepository(getActivity());
        if (context instanceof SetTitleActionBarListener) {
            titleActionBarListener = (SetTitleActionBarListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        if (getArguments() != null) idItem = getArguments().getString(ARGUMENT_NEWS_ITEM);
        setupUi(view);
        return view;
    }

    private void setupUi(View view) {
        findView(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupUx();
    }

    private void setupUx() {
        newsDetailsPresenter.getDataInEntityByIdFromDatabase(idItem, newsDatabaseRepository);
    }

    @Override
    public void bindViewNewsDetails(@NonNull NewsEntity newsEntity) {
        titleView.setText(newsEntity.getTitle());
        fullTextView.setText(newsEntity.getPreviewText());
        Glide.with(this).load(newsEntity.getImageUrl()).into(imageView);
        textDateView.setText(newsEntity.getUpdatedDate());
        //title LiveData for actionBar
        liveTitActBar.setValue(newsEntity.getTitle());
    }

    public LiveData<String> getLiveTitActBar() {
        return liveTitActBar;
    }

    private void findView(View view) {
        titleView = view.findViewById(R.id.details_title);
        fullTextView = view.findViewById(R.id.details_preview);
        imageView = view.findViewById(R.id.details_image_avatar);
        textDateView = view.findViewById(R.id.details_textDate);
        categoryView = view.findViewById(R.id.details_category);
    }

    @Override
    public void onResume() {
        // Set title bar
        titleActionBarListener.onSetTitleActionBar();
        Log.d(TAG, "--- NewsDetailsFragment onResume");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "--- NewsDetailsFragment onDestroy");
        super.onDestroy();
    }
}
