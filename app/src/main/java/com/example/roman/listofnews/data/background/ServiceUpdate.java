package com.example.roman.listofnews.data.background;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.roman.listofnews.MainActivity;
import com.example.roman.listofnews.R;
import com.example.roman.listofnews.data.Storage;
import com.example.roman.listofnews.data.dataBase.NewsDatabaseConverter;
import com.example.roman.listofnews.data.dataBase.NewsDatabaseRepository;
import com.example.roman.listofnews.data.dataBase.NewsEntity;
import com.example.roman.listofnews.ui.adapter.AllNewsItem;
import com.example.roman.listofnews.ux.NewsCategory;
import com.example.roman.listofnews.ux.RestApi;
import com.example.roman.listofnews.ux.TopStoriesMapper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ServiceUpdate extends Service {
    public ServiceUpdate() {
    }

    private static final String TAG = "myLogs";
    @Nullable
    private NewsDatabaseRepository newsDatabaseRepository = new NewsDatabaseRepository(this);
    @Nullable
    private NewsDatabaseConverter databaseConverter = new NewsDatabaseConverter();
    @Nullable
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final String CHANNEL_ID = "LOAD_NEWS_CHANNEL";
    private static final int NOTIFY_ID = 0;
    private static final int FOREGROUND_ID = 3;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(FOREGROUND_ID, notificationStartForeground().build());

    }

    private Notification.Builder notificationStartForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        Notification.Builder notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
            notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID);
        } else {
            notification = new Notification.Builder(getApplicationContext());
        }
        notification
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentText(getString(R.string.startService))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        return notification;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        loadNews();
        return START_STICKY;
    }

    private void loadNews() {
        Disposable disposable = RestApi.getInstanse()
                .getTSEndpoint()
                .setSectionName(
                        NewsCategory.findCategoryByPosition(
                                Storage.getSelectedPositionCategory(this))
                                .serverValue())
                .map(response -> TopStoriesMapper.map(response.getNews()))
                .timeout(1, TimeUnit.MINUTES)
                .flatMap(aLong -> RestApi.getInstanse()
                        .getTSEndpoint()
                        .setSectionName(NewsCategory.findCategoryByPosition(
                                Storage.getSelectedPositionCategory(this))
                                .serverValue())
                        .map(response -> TopStoriesMapper.map(response.getNews())))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::deleteAllFromDatabaseWithRoom, //clear database before update
                        this::handleError);
        compositeDisposable.add(disposable);

    }

    private void deleteAllFromDatabaseWithRoom(List<AllNewsItem> newsItems) {
        Disposable disposable = newsDatabaseRepository.deleteAllFromDatabase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            Log.d(TAG, "//s// deleteAllFromDatabase");
                            //Convert to Entities and save List AllNewsItem to database
                            saveToDatabaseWithRoom(databaseConverter.toDatabase(newsItems));
                        },
                        throwable -> Log.e(TAG, throwable.toString()));
        compositeDisposable.add(disposable);
    }

    private void saveToDatabaseWithRoom(List<NewsEntity> NewsEntityList) {
        Disposable disposable = newsDatabaseRepository.saveToDatabase(NewsEntityList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            Log.d(TAG, "//s// save NewsEntityList To Database");
                            notifyMessage((getString(R.string.notiSuccessText_News_from_the)
                                    + NewsCategory.findCategoryByPosition(
                                    Storage.getSelectedPositionCategory(this))
                                    .serverValue())
                                    + getString(R.string.notiSuccessText_updated));
                        },
                        throwable -> Log.e(TAG, throwable.toString()));
        compositeDisposable.add(disposable);

    }

    private void handleError(Throwable throwable) {
        if (throwable instanceof IOException) {
            Log.d(TAG, "//s// handleError");
            notifyMessage(getString(R.string.notiErrorText));
        }
        stopForeground(true);
        //stop service after load and notify
        stopSelf();
    }

    private void notifyMessage(String message) {
        // Create an explicit intent for an Activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK); // or open existing  //open new activity

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // create the NotificationChannel
        createNotificationChannel();
        //create notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)//(R.mipmap.ic_launcher)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, builder.build());

        stopForeground(true);
        //stop service after load and notify
        stopSelf();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.enableLights(true);
            channel.getLockscreenVisibility();
            channel.setLightColor(Color.BLUE);
            channel.shouldShowLights();
            channel.enableVibration(false);
            NotificationManager notificationManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        stopSelf();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        Log.d(TAG, "--- //s// onDestroy");
        super.onDestroy();
    }
}
