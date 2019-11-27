package com.example.roman.listofnews;

import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.util.Preconditions;
import com.example.roman.listofnews.data.Storage;
import com.example.roman.listofnews.ui.NewsDetailsFragmentListener;
import com.example.roman.listofnews.ui.NewsIntroFragmentListener;
import com.example.roman.listofnews.ui.SetTitleActionBarListener;

public class MainActivity extends AppCompatActivity implements NewsIntroFragmentListener, NewsDetailsFragmentListener, SetTitleActionBarListener {
    private static final String TAG = "myLogs";
    private static final String F_INTRO_TAG = "intro_fragment";
    private static final String F_LIST_TAG = "list_fragment";
    private static final String F_DETAILS_TAG = "details_fragment";
    private static final String F_ABOUT_TAG = "about_fragment";
    private boolean isTwoPanel;
    SelectionStateFragment stateFragment;
    private LiveData<String> liveDataTitActBar;
    private String resultLiveDataTitActBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "--- mainActivity onCreate");
        setContentView(R.layout.activity_main);
        isTwoPanel = findViewById(R.id.frame_details) != null;
        int countBackStack = getSupportFragmentManager().getBackStackEntryCount();

        if (savedInstanceState == null) {
            // open in first time
            if (Storage.openFirstTime(this)) {
                startIntroFragment();
                Storage.setFirstTimeShown(this);
                return;
            } else {
                // check Switch Intro
                if (Storage.checkSwitchIntro(this)) {
                    startIntroFragment();
                    return;
                }
                //create first fragment
                NewsListFragment newsListFragment = NewsListFragment.newInstance(isTwoPanel);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_list, newsListFragment, F_LIST_TAG)
                        .addToBackStack(null) //for convenient closure
                        .commit();
            }

        } else {
            // else when orientation change - clean first fragment
            getSupportFragmentManager().executePendingTransactions();
            Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.frame_list);
            if (fragmentById != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragmentById)
                        .commit();
            }
        }

        saveStateFragment();

        if (isTwoPanel) {
            Fragment introByTag = getSupportFragmentManager().findFragmentByTag(F_INTRO_TAG);
            if (introByTag != null) {
                //cleaning addBackStack added in portrait
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().popBackStack();
                startIntroFragment();
            }
            Fragment aboutByTag = getSupportFragmentManager().findFragmentByTag(F_ABOUT_TAG);
            //check if open newsAboutFragment after newsDetailsFragment
            if (countBackStack == 3 && aboutByTag != null) {
                //cleaning addBackStack added in portrait
                getSupportFragmentManager().popBackStack();
                findViewById(R.id.frame_full_screen).setVisibility(View.VISIBLE);

                //create news fragments under newsAboutFragment
                if (stateFragment.lastSelection.length() > 0) {
                    onNewsDetailsByIdClicked(stateFragment.lastSelection);
                }

                NewsAboutFragment newsAboutFragment = new NewsAboutFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_full_screen, newsAboutFragment, F_ABOUT_TAG)
                        .commit();
                return;
            }
            if (countBackStack == 2 && introByTag == null) {
                if (aboutByTag != null) {
                    //don't use popBackStack, NewsList from portrait set on right
                    findViewById(R.id.frame_full_screen).setVisibility(View.VISIBLE);
                    NewsAboutFragment newsAboutFragment = new NewsAboutFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_full_screen, newsAboutFragment, F_ABOUT_TAG)
                            .commit();

                    /*if (stateFragment.lastSelection.length()>0) {
                        onNewsDetailsByIdClicked(stateFragment.lastSelection);
                    }*/
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
            Fragment introByTag = getSupportFragmentManager().findFragmentByTag(F_INTRO_TAG);
            if (introByTag != null) {
                //cleaning addBackStack added in landscape
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().popBackStack();
                startIntroFragment();
                return;
            }
            Fragment aboutByTag = getSupportFragmentManager().findFragmentByTag(F_ABOUT_TAG);
            Fragment detailsByTag = getSupportFragmentManager().findFragmentByTag(F_DETAILS_TAG);
            if (countBackStack >= 2) {
                for (int i = countBackStack; i > 1; i--) {
                    getSupportFragmentManager().popBackStack();
                }
                NewsListFragment newsListFragment = NewsListFragment.newInstance(isTwoPanel);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_list, newsListFragment, F_LIST_TAG)
                        .commit();

                if (detailsByTag != null) {
                    if (stateFragment.lastSelection.length() > 0) {
                        onNewsDetailsByIdClicked(stateFragment.lastSelection);
                    }
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
       /* //info for testing
        String outMessage = "TwoPanel: "+ String.valueOf(isTwoPanel)+" cntBackStack: "+(String.valueOf(countBackStack));
        Toast.makeText(this, outMessage,Toast.LENGTH_SHORT).show();*/

    }

    private void startIntroFragment() {
        //create intro fragment
        if (isTwoPanel) {
            findViewById(R.id.frame_full_screen).setVisibility(View.VISIBLE);

            NewsIntroFragment newsIntroFragment = new NewsIntroFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_full_screen, newsIntroFragment, F_INTRO_TAG)
                    .addToBackStack(null)
                    .commit();

        } else {
            NewsIntroFragment newsIntroFragment = new NewsIntroFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_list, newsIntroFragment, F_INTRO_TAG)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onNewsIntroFragmentClose() {
        if (isTwoPanel) findViewById(R.id.frame_full_screen).setVisibility(View.GONE);
        getSupportFragmentManager().executePendingTransactions();
        Fragment introByTag = getSupportFragmentManager().findFragmentByTag(F_INTRO_TAG);
        if (introByTag != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(introByTag)
                    .commit();
            //cleaning addBackStack added in startIntroFragment
            getSupportFragmentManager().popBackStack();
        }

        //create first fragment
        NewsListFragment newsListFragment = NewsListFragment.newInstance(isTwoPanel);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_list, newsListFragment, F_LIST_TAG)
                .addToBackStack(null) //for convenient closure
                .commit();
        saveStateFragment();
    }

    private void saveStateFragment() {
        // to save idItem during reorientation
        stateFragment = (SelectionStateFragment) getSupportFragmentManager()
                .findFragmentByTag("headless");
        if (stateFragment == null) {
            stateFragment = new SelectionStateFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(stateFragment, "headless")
                    .commit();
        }
    }

    @Override
    public void onNewsDetailsByIdClicked(@NonNull String idItem) {
        int countBackStack = getSupportFragmentManager().getBackStackEntryCount();
        NewsDetailsFragment newsDetailsFragment = NewsDetailsFragment.newInstance(idItem);
        stateFragment.lastSelection = idItem;
        if (isTwoPanel) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_details, newsDetailsFragment, F_DETAILS_TAG)
                    .commit();
        } else {
            if (countBackStack >= 1) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_list, newsDetailsFragment, F_DETAILS_TAG)
                        .addToBackStack(null) //for return to NewsList
                        .commit();
            }
        }

        liveDataTitActBar = newsDetailsFragment.getLiveTitActBar();
        /* //info for testing
        String outMessage = "TwoPanel: "+ String.valueOf(isTwoPanel)+" cntBackStack: "+(String.valueOf(countBackStack));
        Toast.makeText(this, outMessage,Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                int idFrame = R.id.frame_list;
                if (isTwoPanel) {
                    idFrame = R.id.frame_full_screen;
                    findViewById(R.id.frame_full_screen).setVisibility(View.VISIBLE);
                }
                NewsAboutFragment newsAboutFragment = new NewsAboutFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(idFrame, newsAboutFragment, F_ABOUT_TAG)
                        .addToBackStack(null) //for return
                        .commit();
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
        Fragment listByTag = getSupportFragmentManager().findFragmentByTag(F_LIST_TAG);
        Fragment detailsByTag = getSupportFragmentManager().findFragmentByTag(F_DETAILS_TAG);
        Fragment aboutByTag = getSupportFragmentManager().findFragmentByTag(F_ABOUT_TAG);
        Fragment introByTag = getSupportFragmentManager().findFragmentByTag(F_INTRO_TAG);

        int countBackStack = getSupportFragmentManager().getBackStackEntryCount();
        //clean idItem in stateFragment when returning to NewsList
        if ((!isTwoPanel) && (detailsByTag != null) && (aboutByTag == null)) {
            //cleaning details be in portrait for landscape
            stateFragment.lastSelection = "";
            Fragment detailsById = getSupportFragmentManager().findFragmentById(R.id.frame_details);
            if (detailsById != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(detailsById)
                        .commit();
            }
        }

        //clean newsAboutFragment or newsIntroFragment
        if (isTwoPanel) {
            findViewById(R.id.frame_full_screen).setVisibility(View.GONE);
            if (aboutByTag != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(aboutByTag)
                        .commit();
                //set previous details Title on Action Bar
                if (listByTag != null) setupActionBar(getString(R.string.app_name), false);
                if (resultLiveDataTitActBar != null) setupActionBar(resultLiveDataTitActBar, false);
            }
            if (introByTag != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(introByTag)
                        .commit();
            }
        }

        if (countBackStack == 1) {
            if (!isTwoPanel || aboutByTag == null) {
                 /*//info for testing
                 Toast.makeText(this, String.valueOf(countBackStack), Toast.LENGTH_SHORT).show();
                 Log.d(TAG, "--- mainActivity finish");*/
                finish();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public void onSetTitleActionBar() {
        Fragment listByTag = getSupportFragmentManager().findFragmentByTag(F_LIST_TAG);
        Fragment detailsByTag = getSupportFragmentManager().findFragmentByTag(F_DETAILS_TAG);
        Fragment aboutByTag = getSupportFragmentManager().findFragmentByTag(F_ABOUT_TAG);

        if (aboutByTag != null) {
            setupActionBar(getString(R.string.about_label), true);
            return;
        }
        if (detailsByTag != null) {
            liveDataTitActBar.observe(this, s -> {
                resultLiveDataTitActBar = s;
                if (!isTwoPanel) {
                    setupActionBar(s, true);
                } else {
                    setupActionBar(s, false);
                }
            });
            return;
        }
        if (listByTag != null) {
            setupActionBar(getString(R.string.app_name), false);
        }
    }

    public void setupActionBar(String title, Boolean addBackButton) {
        final ActionBar ab = getSupportActionBar();
        final ActionBar actionBar = Preconditions.checkNotNull(ab);
        actionBar.setTitle(title);
        if (addBackButton) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }


    @Override
    protected void onPause() {
        Log.d(TAG, "--- mainActivity onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "--- mainActivity onDestroy");
        super.onDestroy();
    }
}
