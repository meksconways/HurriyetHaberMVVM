package com.mek.haberler.roomdb;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM news_db")
    //LiveData<List<NewsDB>> getAllNewsFromRoom();
    List<NewsDB> getAllNewsfromRoom();

    @Query("SELECT * FROM news_db WHERE newsId LIKE :newsID")
    NewsDB getNewsDetailfromRoom(String newsID);

    @Insert
    void insertNewsfromRoom(NewsDB news);

    @Delete
    void deleteNewsfromRoom(NewsDB news);





}
