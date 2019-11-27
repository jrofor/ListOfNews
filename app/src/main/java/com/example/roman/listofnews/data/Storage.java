package com.example.roman.listofnews.data;

import android.content.Context;
import android.content.SharedPreferences;

import static java.lang.String.valueOf;


public class Storage {

    private static final String APP_PREFERENCES = "mySettings";
    private static final String APP_PREFERENCES_FirstTime = "FirstTime";
    private static final String APP_PREFERENCES_switchIntro = "switchIntro";
    private static final String APP_PREFERENCES_switchUpdate = "switchUpdate";
    private static final String APP_PREFERENCES_tagUploadWorkManager = "tagUploadWorkManager";
    private static final String APP_PREFERENCES_selectedPositionCategory = "selectedPositionCategory";
    private static final String APP_PREFERENCES_currentState = "currentState";
    private static final String APP_PREFERENCES_currentListItem = "currentListItem";


//-----------------------------------------------------------

    public static boolean openFirstTime(Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean openFT = mSettings.getBoolean(APP_PREFERENCES_FirstTime, true);
        return openFT;
    }

    public static void setFirstTimeShown(Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_FirstTime, false);
        editor.apply();
    }
//-----------------------------------------------------------

    public static boolean checkSwitchIntro(Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean swIntro = mSettings.getBoolean(APP_PREFERENCES_switchIntro, false);
        return swIntro;
    }

    public static void setSwitchIntroOn(Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_switchIntro, true);
        editor.apply();
    }

    public static void setSwitchIntroOff(Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_switchIntro, false);
        editor.apply();
    }
//-----------------------------------------------------------

    public static boolean checkSwitchUpdate(Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean swUpdate = mSettings.getBoolean(APP_PREFERENCES_switchUpdate, false);
        return swUpdate;
    }

    public static void setSwitchUpdateOn(Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_switchUpdate, true);
        editor.apply();
    }

    public static void setSwitchUpdateOff(Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_switchUpdate, false);
        editor.apply();
    }
//-----------------------------------------------------------

    /*public static void setTagUploadWork (Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_tagUploadWorkManager, "WORK_MANAGER_UPLOAD_TAG");
        editor.apply();
    }*/

    public static String getTagUpdateWork(Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return mSettings.getString(APP_PREFERENCES_tagUploadWorkManager, "WORK_MANAGER_UPLOAD_TAG");
    }
//-----------------------------------------------------------

    public static void setSelectedPositionCategory(Context context, int position) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        //editor.putString(APP_PREFERENCES_selectedCategory , category) ;
        editor.putInt(APP_PREFERENCES_selectedPositionCategory, position);
        editor.apply();
    }

    public static int getSelectedPositionCategory(Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return mSettings.getInt(APP_PREFERENCES_selectedPositionCategory, 0);
    }
//-----------------------------------------------------------

    public static void setCurrentState(Context context, String nameState) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_currentState, nameState);
        editor.apply();
    }

    public static String getCurrentState(Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return mSettings.getString(APP_PREFERENCES_currentState, "HasNoData");
    }
//-----------------------------------------------------------

    public static void setCurrentListItem(Context context, Integer listItem) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_currentListItem, listItem);
        editor.apply();
    }

    public static int getCurrentListItem(Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return mSettings.getInt(APP_PREFERENCES_currentListItem, 0);
    }
//-----------------------------------------------------------
}