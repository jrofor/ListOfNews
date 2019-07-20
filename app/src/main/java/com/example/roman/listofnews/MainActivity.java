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
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.util.Preconditions;
import com.example.roman.listofnews.data.Storage;
import com.example.roman.listofnews.ui.NewsDetailsFragmentListener;


public class MainActivity extends AppCompatActivity implements NewsDetailsFragmentListener {
    private static final String TAG = "myLogs";
    private static final String F_LIST_TAG = "list_fragment";
    private static final String F_DETAILS_TAG = "details_fragment";
    private static final String F_ABOUT_TAG = "about_fragment";
    private boolean isTwoPanel;
    SelectionStateFragment stateFragment;
    private boolean introNextTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "--- mainActivity onCreate");
        setContentView(R.layout.activity_main);
        setupActionBar();
        isTwoPanel = findViewById(R.id.frame_details) != null;
        int countBackStack = getSupportFragmentManager().getBackStackEntryCount();

        if (savedInstanceState == null) {
            //checking whether the program needs to opens Intro or not
            if (!Storage.checkIntro(this)) {

                startActivity(new Intent(this, NewsIntroActivity.class));
                introNextTime = false;
                Log.d(TAG, "--- mainActivity finish for intro");
                finishAffinity();
                return;
            } else {
                introNextTime = true;
                //else create first fragment
                NewsListFragment newsListFragment = NewsListFragment.newInstance(isTwoPanel);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_list, newsListFragment, F_LIST_TAG)
                        .addToBackStack(null) //for convenient closure
                        .commit();
            }

        } else {
            //clean first fragment
            getSupportFragmentManager().executePendingTransactions();
            Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.frame_list);
            if (fragmentById != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragmentById)
                        .commit();
            }
        }

        // to save idItem during reorientation
        stateFragment = (SelectionStateFragment) getSupportFragmentManager()
                .findFragmentByTag("headless");
        if (stateFragment == null) {
            stateFragment = new SelectionStateFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(stateFragment, "headless")
                    .commit();
        }

        if (isTwoPanel) {
            Fragment aboutByTag = getSupportFragmentManager().findFragmentByTag(F_ABOUT_TAG);
            //check if open newsAboutFragment after newsDetailsFragment
            if (countBackStack == 3 && aboutByTag != null) {
                //cleaning addBackStack added in portrait
                getSupportFragmentManager().popBackStack();
                findViewById(R.id.frame_full_screen).setVisibility(View.VISIBLE);

                NewsAboutFragment newsAboutFragment = new NewsAboutFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_full_screen, newsAboutFragment, F_ABOUT_TAG)
                        .commit();

                //create news fragments under newsAboutFragment
                if (stateFragment.lastSelection.length()>0) {
                    onNewsDetailsByIdClicked(stateFragment.lastSelection);
                }
                NewsListFragment backNewsListFragment = NewsListFragment.newInstance(isTwoPanel);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_list, backNewsListFragment, F_LIST_TAG)
                        .commit();
            }
            if (countBackStack == 2 ) {
                if (aboutByTag != null){
                    //don't use popBackStack, NewsList from portrait set on right
                    findViewById(R.id.frame_full_screen).setVisibility(View.VISIBLE);
                    NewsAboutFragment newsAboutFragment = new NewsAboutFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_full_screen, newsAboutFragment, F_ABOUT_TAG)
                            .commit();

                    if (stateFragment.lastSelection.length()>0) {
                        onNewsDetailsByIdClicked(stateFragment.lastSelection);
                    }
                } else {
                    findViewById(R.id.frame_full_screen).setVisibility(View.GONE);
                    //if don't use newsAboutFragment
                    //simulating the user pressing the onBackPressed
                    getSupportFragmentManager().popBackStack();
                    // if user open details on portrait - returning NewsDetails on right
                    if (stateFragment.lastSelection.length() > 0) {
                        onNewsDetailsByIdClicked(stateFragment.lastSelection);
                    }
                    //returning NewsList on left
                    NewsListFragment backNewsListFragment = NewsListFragment.newInstance(isTwoPanel);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_list, backNewsListFragment, F_LIST_TAG)
                            .commit();
                }
            } else {
                //returning NewsList on left
                NewsListFragment backNewsListFragment = NewsListFragment.newInstance(isTwoPanel);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_list, backNewsListFragment, F_LIST_TAG)
                        .commit();
            }

        }

        if (!isTwoPanel) {
            Fragment aboutByTag = getSupportFragmentManager().findFragmentByTag(F_ABOUT_TAG);
            Fragment detailsByTag = getSupportFragmentManager().findFragmentByTag(F_DETAILS_TAG);
            if (countBackStack >= 2) {
                getSupportFragmentManager().popBackStack();
                NewsListFragment newsListFragment = NewsListFragment.newInstance(isTwoPanel);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_list, newsListFragment, F_LIST_TAG)
                        .commit();

                if (detailsByTag != null) {
                    NewsDetailsFragment newsDetailsFragment = NewsDetailsFragment.newInstance(stateFragment.lastSelection);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_list, newsDetailsFragment, F_DETAILS_TAG)
                            .addToBackStack(null) //for return to NewsList
                            .commit();
                }

                if (aboutByTag != null) {
                    NewsAboutFragment newsAboutFragment = new NewsAboutFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_list, newsAboutFragment, F_ABOUT_TAG)
                            .addToBackStack(null) //for return
                            .commit();
                }
            } else {
                NewsListFragment backNewsListFragment = NewsListFragment.newInstance(isTwoPanel);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_list, backNewsListFragment, F_LIST_TAG)
                        .commit();
                if (stateFragment.lastSelection.length() > 0) {
                    onNewsDetailsByIdClicked(stateFragment.lastSelection);
                }
            }
        }
        //info for testing
        String outMessage = "TwoPanel: "+ String.valueOf(isTwoPanel)+" cntBackStack: "+(String.valueOf(countBackStack));
        Toast.makeText(this, outMessage,Toast.LENGTH_SHORT).show();

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
                    .replace(R.id.frame_details, newsDetailsFragment, F_DETAILS_TAG)
                    .commit();
        } else  {
            if (countBackStack == 1) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_list, newsDetailsFragment, F_DETAILS_TAG)
                        .addToBackStack(null) //for return to NewsList
                        .commit();
            }
        }
         //info for testing
        String outMessage = "TwoPanel: "+ String.valueOf(isTwoPanel)+" cntBackStack: "+(String.valueOf(countBackStack));
        Toast.makeText(this, outMessage,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId() ){
            case R.id.item1:
                int idFrame = R.id.frame_list;
                if (isTwoPanel) {
                    idFrame = R.id.frame_full_screen;
                    findViewById(R.id.frame_full_screen).setVisibility(View.VISIBLE);
                }
                NewsAboutFragment newsAboutFragment = new NewsAboutFragment() ;
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(idFrame, newsAboutFragment, F_ABOUT_TAG)
                        .addToBackStack(null) //for return
                        .commit();

                /*
                startActivity(new Intent(this, NewsAboutFragment.class));
                return true;*/
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
        Fragment aboutByTag = getSupportFragmentManager().findFragmentByTag(F_ABOUT_TAG);
        Fragment detailsByTag = getSupportFragmentManager().findFragmentByTag(F_DETAILS_TAG);

        int  countBackStack = getSupportFragmentManager().getBackStackEntryCount();
        //clean idItem in stateFragment when returning to NewsList
        if ((!isTwoPanel) && (detailsByTag !=null) && (aboutByTag ==null) ) {
            //cleaning details be in portrait for landscape
            stateFragment.lastSelection = "";
            Fragment detailsById = getSupportFragmentManager().findFragmentById(R.id.frame_details);
            if (detailsById != null){
                getSupportFragmentManager().beginTransaction()
                        .remove(detailsById)
                        .commit();
            }
        }

        //clean newsAboutFragment
        if (isTwoPanel && aboutByTag != null ) {
            findViewById(R.id.frame_full_screen).setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction()
                    .remove(aboutByTag)
                    .commit();
        }

        if (countBackStack == 1) {
             if ((!isTwoPanel) || (isTwoPanel && aboutByTag == null)) {
                 //info for testing
                 Toast.makeText(this, String.valueOf(countBackStack), Toast.LENGTH_SHORT).show();
                 Log.d(TAG, "--- mainActivity finish");
                 finish();
                 return;
             }
        }

        super.onBackPressed();
    }

    private void setupActionBar(){
        final ActionBar ab = getSupportActionBar();
        final ActionBar actionBar = Preconditions.checkNotNull(ab);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }



    @Override
    protected void onPause() {
        Storage.setIntroShowAgain(this);
        Log.d(TAG, "Storage FALSE");
        Log.d(TAG, "--- mainActivity onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (introNextTime) {
            Storage.setIntroShowAgain(this);
            Log.d(TAG, "Storage FALSE");
            Log.d(TAG, "--- mainActivity onDestroy");

        }
        super.onDestroy();
    }
}
