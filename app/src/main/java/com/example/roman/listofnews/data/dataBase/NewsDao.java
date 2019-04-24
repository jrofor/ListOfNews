package com.example.roman.listofnews.data.dataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Observable;

import com.example.roman.listofnews.ui.adapter.AllNewsItem;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM newsEntity")
    Observable<List<NewsEntity>> getAll();

    @Query("SELECT * FROM newsEntity WHERE id = :id")
    NewsEntity getNewsById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NewsEntity newsEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(NewsEntity... newsEntities);

    @Delete
    void delete(NewsEntity newsEntity);

    @Query("DELETE FROM newsEntity")
    void deleteAll();

    void insertAll(AllNewsItem[] newsEntity);
}

