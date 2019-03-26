package com.example.roman.listofnews;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.roman.listofnews.data.DataUtils;
import com.example.roman.listofnews.data.NewsItem;

import java.util.ArrayList;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class NewsDetailsActivity extends AppCompatActivity {


    TextView titleView;
    TextView fullTextView;
    String categoryView;
    ImageView imageView;
    TextView textDateView;


    //*DataUtils newss = (DataUtils) getIntent().getSerializableExtra("extra:news");
    //*List<NewsItem> newss = new ArrayList<>();
    //*newss.add(DataUtils.generateNews().get(newsPosition);)

    //private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);


        titleView = (TextView) findViewById(R.id.details_title);
        fullTextView = (TextView)  findViewById(R.id.details_preview);
        imageView =  (ImageView) findViewById(R.id.details_image_avatar);
        textDateView = (TextView)  findViewById(R.id.details_textDate);
        //categoryView =  findViewById(R.id.CategoryName);
        //categoryView =   R.string.NewsDetails_name;
        int newsPosition = getIntent().getIntExtra("extra:news",0);
        bind(DataUtils.generateNews().get(newsPosition));//
}

    public void bind(NewsItem news) {
        setTitle(news.getCategory().getName());
        titleView.setText(news.getTitle());
        fullTextView.setText(news.getFullText());
        Glide.with(this).load(news.getImageUrl()).into(imageView);
        textDateView.setText(news.getPublishDate().toString());
    }
}
