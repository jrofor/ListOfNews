package com.example.roman.listofnews.data;

import android.content.Context;
import android.content.SharedPreferences;

import static java.lang.String.valueOf;


public class Storage {

    private static final String APP_PREFERENCES = "mySettings";
    private static final String APP_PREFERENCES_Intro = "Intro";
    private static final String APP_PREFERENCES_FirstTime = "FirstTime";
    private static final String APP_PREFERENCES_switchIntro = "switchIntro";


    //private static final String APP_PREFERENCES_cnt = "cnt";

    public static boolean openFirstTime  (Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean openFT = mSettings.getBoolean(APP_PREFERENCES_FirstTime, true);
        return openFT;
    }

    public static void setFirstTimeShown  (Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_FirstTime , false) ;
        editor.apply();
    }

    public static boolean checkIntro  (Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean hasShown = mSettings.getBoolean(APP_PREFERENCES_Intro, false);
        return hasShown;
    }

    public static void setIntroHasShown  (Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();  //2 //3
        editor.putBoolean(APP_PREFERENCES_Intro , true) ;
        editor.apply();
    }

    public static void setIntroShowAgain  (Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();  //2 //3
        editor.putBoolean(APP_PREFERENCES_Intro , false) ;
        editor.apply();
    }

    public static boolean checkSwitchIntro(Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean swIntro = mSettings.getBoolean(APP_PREFERENCES_switchIntro, false);
        return swIntro;
    }

    public static void setSwitchIntroOn  (Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_switchIntro ,true ) ;
        editor.apply();
    }

    public static void setSwitchIntroOff  (Context context) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_switchIntro , false) ;
        editor.apply();
    }

    /*public static void setCntIntro(Context context, int cntIntro) {
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE); //1
        SharedPreferences.Editor editor = mSettings.edit();  //2 //3
        editor.putString(APP_PREFERENCES_cnt, valueOf(cntIntro)) ;
        editor.apply();

    }

    public static int getCntIntro (Context context) {
        int cntIntro;
        SharedPreferences mSettings;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        cntIntro = (mSettings.getInt(APP_PREFERENCES_cnt, 0)) ;

        return cntIntro;

    }*/

}