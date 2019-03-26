package com.example.roman.listofnews;

import android.content.Intent;

import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.roman.listofnews.data.DataUtils;
import com.example.roman.listofnews.ui.adapter.NewsRecyclerAdapter;

import java.util.Objects;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class NewsMainActivity extends AppCompatActivity {

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

    private void setupUi() {
        findView();

    }

    private void findView() {
        RecyclerView recyclerView = findViewById(R.id.rv_news);
        recyclerView.setAdapter(new NewsRecyclerAdapter(this, DataUtils.generateNews(), clickListener));
        int orientation = getResources().getConfiguration().orientation;
        onChangeColumnsWithOrientation(orientation, recyclerView);

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
