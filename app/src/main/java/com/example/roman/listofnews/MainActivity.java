package com.example.roman.listofnews;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.bumptech.glide.util.Preconditions;
import com.example.roman.listofnews.data.Storage;
import com.example.roman.listofnews.ui.NewsDetailsFragmentListener;


public class MainActivity extends AppCompatActivity implements NewsDetailsFragmentListener {
    private static final String TAG = "myLogs";
    private static final String F_DETAILS_TAG = "details_fragment";
    private static final String F_LIST_TAG = "list_fragment";
    private boolean isTwoPanel;
    SelectionStateFragment stateFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActionBar();
        isTwoPanel = findViewById(R.id.frame_details) != null;
        int countBackStack = getSupportFragmentManager().getBackStackEntryCount();

        // to save idItem during reorientation
        stateFragment = (SelectionStateFragment) getSupportFragmentManager()
                .findFragmentByTag("headless");
        if (stateFragment == null) {
            stateFragment = new SelectionStateFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(stateFragment, "headless")
                    .commit();
        }

        if (savedInstanceState == null) {
            NewsListFragment newsListFragment = NewsListFragment.newInstance(isTwoPanel);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_list, newsListFragment)
                    .addToBackStack(null) //for convenient closure
                    .commit();
        } else {
            getSupportFragmentManager().executePendingTransactions();
            Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.frame_list);
            if (fragmentById != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragmentById)
                        .commit();
            }
        }

        if (isTwoPanel) {
            if (countBackStack == 2) {
                getSupportFragmentManager().popBackStack();
                if (stateFragment.lastSelection.length()>0) {
                    onNewsDetailsByIdClicked(stateFragment.lastSelection);
                }
            }
            NewsListFragment newsListFragment = NewsListFragment.newInstance(isTwoPanel);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_list, newsListFragment)
                    .commit();
        }

        if (!isTwoPanel) {
            NewsListFragment backNewsListFragment = NewsListFragment.newInstance(isTwoPanel);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_list, backNewsListFragment)
                    .commit();
            if (stateFragment.lastSelection.length()>0) {
                onNewsDetailsByIdClicked(stateFragment.lastSelection);
            }
        }
         /* //info for testing
        String outMessage = "TwoPanel: "+ String.valueOf(isTwoPanel)+" cntBackStack: "+(String.valueOf(countBackStack));
        Toast.makeText(this, outMessage,Toast.LENGTH_SHORT).show();
        */
    }


    @Override
    public void onNewsDetailsByIdClicked(@NonNull String idItem) {
        int countBackStack = getSupportFragmentManager().getBackStackEntryCount();
        NewsDetailsFragment newsDetailsFragment = NewsDetailsFragment.newInstance(idItem);
        stateFragment.lastSelection = idItem;
        //int frameId = isTwoPanel ? R.id.frame_details : R.id.frame_list;
        if (isTwoPanel) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_details, newsDetailsFragment)
                    .commit();
        } else  {

            if (countBackStack == 1) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_list, newsDetailsFragment)
                        .addToBackStack(null) //for return to NewsList
                        .commit();
            } else {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_list, newsDetailsFragment)
                        .commit();
            }

        }
        /* //info for testing
        String outMessage = "TwoPanel: "+ String.valueOf(isTwoPanel)+" cntBackStack: "+(String.valueOf(countBackStack));
        Toast.makeText(this, outMessage,Toast.LENGTH_SHORT).show();
        */
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


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        int  countBackStack = getSupportFragmentManager().getBackStackEntryCount();
        if (countBackStack == 2 ) {
            stateFragment.lastSelection = null;
        }

        if (countBackStack <= 1 || isTwoPanel) {
            /* //info for testing
            Toast.makeText(this, String.valueOf(countBackStack),Toast.LENGTH_SHORT).show();*/
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
