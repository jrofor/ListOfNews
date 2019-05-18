package com.example.roman.listofnews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.Glide;
import com.example.roman.listofnews.data.Storage;
import com.example.roman.listofnews.data.dataBase.NewsDatabaseRepository;
import com.example.roman.listofnews.data.dataBase.NewsEntity;
import com.example.roman.listofnews.ui.adapter.AllNewsItem;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsDetailsActivity extends AppCompatActivity {

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
    private static final String TAGroom = "RoomActivity";
    private static final int LAYOUT = R.layout.activity_news_details;

    private static final String EXTRA_NEWS_ITEM = "extra:idNewsItem";
    private String Id;

    //final String Id = (String) getIntent().getSerializableExtra(EXTRA_NEWS_ITEM);

    @Nullable
    private NewsDatabaseRepository newsDatabaseRepository = new NewsDatabaseRepository(this);
    @Nullable
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    //*DataUtils newss = (DataUtils) getIntent().getSerializableExtra("extra:news");
    //*List<NewsItem> newss = new ArrayList<>();
    //*newss.add(DataUtils.generateNews().get(newsPosition);)

    //private Context context;
    //private final List<AllNewsItem> news = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        Id = getIntent().getStringExtra(EXTRA_NEWS_ITEM);
        setupUi();

         /*final WebView webView = findViewById(R.id.web_view);
        webView.loadUrl(allNewsItem.getUrl());*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupUx();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static void start(@NonNull Context context, @NonNull String IdItem) {
        context.startActivity(new Intent(context, NewsDetailsActivity.class).putExtra(EXTRA_NEWS_ITEM, IdItem));

    }

    private void setupUi() {
        findView();
        setupActionBar();

    }

    private void setupUx() {
        setupOpeningNewsDetails(Id);
    }

    private void setupOpeningNewsDetails (@NonNull String Id) {
        getinEntitybyIdFromDatabase(Id);
    }

    private void getinEntitybyIdFromDatabase(@NonNull String IdItem) {
        Disposable disposable = newsDatabaseRepository.getEntitybyIdFromDatabase(IdItem)
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
    }

    private void setupActionBar(){
        final ActionBar ab = getSupportActionBar();
        final ActionBar actionBar = Preconditions.checkNotNull(ab);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void findView() {
        titleView = (TextView) findViewById(R.id.details_title);
        fullTextView = (TextView)  findViewById(R.id.details_preview);
        imageView =  (ImageView) findViewById(R.id.details_image_avatar);
        textDateView = (TextView)  findViewById(R.id.details_textDate);
        categoryView =  (TextView) findViewById(R.id.details_category);

        //int newsPosition = getIntent().getIntExtra("extra:news",0);
        //bind(news.get(newsPosition));
    }


    @Override
    protected void onDestroy(){
        compositeDisposable.dispose();
        Log.d(TAGroom, "NewsDetailsActivity onDestroy()");
        //Log.d(TAG2, "ActivityTwo: onDistroy");
        super.onDestroy();
    }
}
