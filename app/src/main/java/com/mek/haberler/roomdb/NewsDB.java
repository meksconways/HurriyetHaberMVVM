package com.mek.haberler.roomdb;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news_db")
public class NewsDB {

    @NonNull
    @PrimaryKey
    private String newsId;

    @ColumnInfo(name = "CreatedDate")
    private String createdDate;
    @ColumnInfo(name = "Description")
    private String subTitle;
    @ColumnInfo(name = "Title")
    private String title;
    @ColumnInfo(name = "Editor")
    private String editor;
    @ColumnInfo(name = "Text")
    private String description;


    @ColumnInfo(name = "photo")
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getNewsId() {
        return newsId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getEditor() {
        return editor;
    }

    public String getDescription() {
        return description;
    }
}
