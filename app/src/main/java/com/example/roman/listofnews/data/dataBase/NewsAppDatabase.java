package com.example.roman.listofnews.data.dataBase;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {NewsEntity.class}, version = 1)
public abstract class NewsAppDatabase extends RoomDatabase {
    private static NewsAppDatabase singletone;
    private static final String DATABASE_NAME = "NewsRoomDb.db";
    public NewsDao newsDao;
    //public abstract  NewsAsyncDao NewsAsyncDao;

    public static NewsAppDatabase getNewsAppDatabase(Context context) {
        if (singletone == null) {
            synchronized (NewsAppDatabase.class) {
                if (singletone == null) {
                    singletone = Room.databaseBuilder(context.getApplicationContext(),
                            NewsAppDatabase.class,
                            DATABASE_NAME)
                            .allowMainThreadQueries()
                            .build();


                }
            }
        }
        return singletone;
    }

}
