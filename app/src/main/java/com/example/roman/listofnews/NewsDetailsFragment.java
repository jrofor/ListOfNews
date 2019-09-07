package com.example.roman.listofnews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.Glide;
import com.example.roman.listofnews.data.dataBase.NewsDatabaseRepository;
import com.example.roman.listofnews.data.dataBase.NewsEntity;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsDetailsFragment extends Fragment {

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
    private String titleActionBar;

    public static NewsDetailsFragment newInstance(String idNewsItem) {
        NewsDetailsFragment newsDetailsFragment = new NewsDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_NEWS_ITEM,idNewsItem);
        newsDetailsFragment.setArguments(bundle);

        return newsDetailsFragment;
    }


    @Nullable
    private NewsDatabaseRepository newsDatabaseRepository = new NewsDatabaseRepository(getActivity());
    @Nullable
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_details, container, false);

        if (getArguments() != null) idItem = getArguments().getString(ARGUMENT_NEWS_ITEM);
        setupUi(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupUx();
    }


    /*public static void start(@NonNull Context context, @NonNull String IdItem) {
        context.startActivity(new Intent(context, NewsDetailsFragment.class).putExtra(EXTRA_NEWS_ITEM, IdItem));

    }*/

    private void setupUi(View view) {
        findView(view);
    }

    private void setupUx() {
        setupOpeningNewsDetails(idItem);
    }

    private void setupOpeningNewsDetails (@NonNull String idItem) {
        getInEntityByIdFromDatabase(idItem);
    }

    private void getInEntityByIdFromDatabase(@NonNull String idItem) {
        Disposable disposable = newsDatabaseRepository.getEntitybyIdFromDatabase(idItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::bindViewNewsDetails);
        compositeDisposable.add(disposable);
    }

    public void bindViewNewsDetails(NewsEntity newsEntity) {
        titleView.setText(newsEntity.getTitle());
        fullTextView.setText(newsEntity.getPreviewText());
        Glide.with(this).load(newsEntity.getImageUrl()).into(imageView);
        textDateView.setText(newsEntity.getUpdatedDate());
        titleActionBar = (newsEntity.getTitle());
        // Set title bar
        ((MainActivity) Objects.requireNonNull(getActivity())).setupActionBar(titleActionBar,true);
    }


    private void findView(View view) {
        titleView = (TextView) view.findViewById(R.id.details_title);
        fullTextView = (TextView) view.findViewById(R.id.details_preview);
        imageView =  (ImageView) view.findViewById(R.id.details_image_avatar);
        textDateView = (TextView) view.findViewById(R.id.details_textDate);
        categoryView =  (TextView) view.findViewById(R.id.details_category);
    }


    @Override
    public void onDestroy(){
        compositeDisposable.dispose();
        Log.d(TAG, "--- NewsDetailsFragment onDestroy");
        super.onDestroy();
    }
}
