package com.example.roman.listofnews.data.dataBase;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM newsEntity")
     List<NewsEntity> getAll();
/*
    @Query("SELECT * FROM newsEntity")
    DataSource.Factory<Integer, NewsEntity> getAllForDataSource();*/

    @Query("SELECT COUNT(*) from newsEntity")
    Integer newsEntityCount();

    @Query("SELECT * FROM newsEntity WHERE id = :id")
    NewsEntity getNewsById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NewsEntity newsEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //void insertAll(NewsEntity[] newsEntities);
    void insertAll(NewsEntity... newsEntities);

    @Delete
    void delete(NewsEntity newsEntity);

    @Query("DELETE FROM newsEntity")
    void deleteAll();


}

