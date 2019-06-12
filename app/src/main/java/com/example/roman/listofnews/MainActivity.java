package com.example.roman.listofnews;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.util.Preconditions;
import com.example.roman.listofnews.data.Storage;
import com.example.roman.listofnews.ui.NewsDetailsFragmentListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NewsDetailsFragmentListener {
    private static final String TAG = "myLogs";
    private static final String F_DETAILS_TAG = "details_fragment";
    private static final String F_LIST_TAG = "list_fragment";
    private boolean isTwoPanel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActionBar();
        isTwoPanel = findViewById(R.id.frame_details) != null;

        if (savedInstanceState == null) {
            NewsListFragment newsListFragment = NewsListFragment.newInstance(isTwoPanel);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_list, newsListFragment, F_LIST_TAG )
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void onNewsDetailsByIdClicked(@NonNull String idItem) {
        NewsDetailsFragment newsDetailsFragment = NewsDetailsFragment.newInstance(idItem);
        //if (isTwoPanel || !newsDetailsFragment.isVisible())  {
        int frameId = isTwoPanel ? R.id.frame_details : R.id.frame_list;
        //onCheckChangeOrientation(isTwoPanel);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameId, newsDetailsFragment, F_DETAILS_TAG)
                .addToBackStack(null)
                .commit();
        //}
    }

   /*public void onCheckChangeOrientation(boolean isTwoPanel){
        if (isTwoPanel  && findViewById(R.id.frame_details).()) {}
    }
*/
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


    /*@Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }*/

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
            Log.d(TAG, "--- mainActivity finish");
            return;
        }
        super.onBackPressed();
    }

    private void setupActionBar(){
        final ActionBar ab = getSupportActionBar();
        final ActionBar actionBar = Preconditions.checkNotNull(ab);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        Storage.setIntroShowAgain(this);
        Log.d(TAG, "Storage FALSE");
        super.onDestroy();
    }

}
