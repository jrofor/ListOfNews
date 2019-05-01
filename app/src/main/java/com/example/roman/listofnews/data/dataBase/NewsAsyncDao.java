package com.example.roman.listofnews.data.dataBase;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

public interface NewsAsyncDao {

    @Query("SELECT * FROM NewsEntity")
    Observable<List<NewsEntity>> getAll();

    @Insert(onConflict = REPLACE)
    void insertAll(NewsEntity... newsEntities);

    @Delete
    void deleta (NewsEntity newsEntity);

    @Query("DELETE FROM NewsEntity")
    void deleteAll();

    @Query("SELECT * FROM NewsEntity WHERE title LIKE :title LIMIT 1")
    Single<NewsEntity> findByName(String title);


    @Query("SELECT * FROM NewsEntity WHERE title IN (:titles)")
    Observable<List<NewsEntity>> loadAllByTitles(String[] titles);
}
