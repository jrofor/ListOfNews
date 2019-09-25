package com.example.roman.listofnews;

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
import java.util.Objects;

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

    public static NewsDetailsFragment newInstance(String idNewsItem) {
        NewsDetailsFragment newsDetailsFragment = new NewsDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_NEWS_ITEM,idNewsItem);
        newsDetailsFragment.setArguments(bundle);
        return newsDetailsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        newsDatabaseRepository = new NewsDatabaseRepository(getActivity());
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
        String titleActionBar = (newsEntity.getTitle());
        // Set title bar
        ((MainActivity) Objects.requireNonNull(getActivity())).setupActionBar(titleActionBar,true);
    }

    private void findView(View view) {
        titleView = (TextView) view.findViewById(R.id.details_title);
        fullTextView = (TextView) view.findViewById(R.id.details_preview);
        imageView = (ImageView) view.findViewById(R.id.details_image_avatar);
        textDateView = (TextView) view.findViewById(R.id.details_textDate);
        categoryView = (TextView) view.findViewById(R.id.details_category);
    }


    @Override
    public void onDestroy(){
        Log.d(TAG, "--- NewsDetailsFragment onDestroy");
        super.onDestroy();
    }
}
