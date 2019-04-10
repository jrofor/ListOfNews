package com.example.roman.listofnews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.Glide;
import com.example.roman.listofnews.ui.adapter.AllNewsItem;

public class NewsDetailsActivity extends AppCompatActivity {


   /* TextView titleView;
    TextView fullTextView;
    String categoryView;
    ImageView imageView;
    TextView textDateView;*/

    private static final int LAYOUT = R.layout.activity_news_details;
    private static final String EXTRA_NEWS_ITEM = "extra:allNewsItem";

    //*DataUtils newss = (DataUtils) getIntent().getSerializableExtra("extra:news");
    //*List<NewsItem> newss = new ArrayList<>();
    //*newss.add(DataUtils.generateNews().get(newsPosition);)

    //private Context context;
    //private final List<AllNewsItem> news = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        final AllNewsItem allNewsItem = (AllNewsItem) getIntent().getSerializableExtra(EXTRA_NEWS_ITEM);
        final ActionBar ab = getSupportActionBar();
        final ActionBar actionBar = Preconditions.checkNotNull(ab);
        final WebView webView = findViewById(R.id.web_view);

        actionBar.setDisplayHomeAsUpEnabled(true);
        webView.loadUrl(allNewsItem.getUrl());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static void start(@NonNull Context context, @NonNull AllNewsItem allNewsItem) {
        context.startActivity(new Intent(context, NewsDetailsActivity.class).putExtra(EXTRA_NEWS_ITEM, allNewsItem));
    }
/*titleView = (TextView) findViewById(R.id.details_title);
        fullTextView = (TextView)  findViewById(R.id.details_preview);
        imageView =  (ImageView) findViewById(R.id.details_image_avatar);
        textDateView = (TextView)  findViewById(R.id.details_textDate);
        //categoryView =  findViewById(R.id.CategoryName);
        //categoryView =   R.string.NewsDetails_name;
        int newsPosition = getIntent().getIntExtra("extra:news",0);
        //bind(DataUtils.generateNews().get(newsPosition));//
        bind(news.get(newsPosition));*/
//}
/*
    public void bind(AllNewsItem newsDTO) {
        //setTitle(news.getCategory().getName());
        //titleView.setText(news.getTitle());
        titleView.setText(newsDTO.getTitle());
        //fullTextView.setText(news.getFullText());
        fullTextView.setText(newsDTO.getPreviewText());
        //Glide.with(this).load(news.getImageUrl()).into(imageView);
        Glide.with(this).load(newsDTO.getImageUrl()).into(imageView);
        //textDateView.setText(news.getPublishDate().toString());
        textDateView.setText(newsDTO.getUpdatedDate().toString());
    }*/
}
