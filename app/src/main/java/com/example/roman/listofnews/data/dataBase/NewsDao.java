package com.example.roman.listofnews.data.dataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM newsEntity")
    List<NewsEntity> getAll();

    @Query("SELECT COUNT(*) from newsEntity")
    int newsEntityCount();

    @Query("SELECT * FROM newsEntity WHERE id = :id")
    NewsEntity getNewsById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(NewsEntity... newsEntities);

    @Query("DELETE FROM newsEntity")
    void deleteAll();


}

