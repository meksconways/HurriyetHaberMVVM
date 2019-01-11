package com.mek.haberler.newsdetail.model;

import com.mek.haberler.feednews.NewsFeedModel.Files;
import com.squareup.moshi.Json;

import java.util.List;

public class NewsDetailModel {

    @Json(name = "Id")
    public final String id;
    @Json(name = "CreatedDate")
    public final String createdDate;
    @Json(name = "Description")
    public final String subTitle;
    @Json(name = "Title")
    public final String title;
    @Json(name = "Editor")
    public final String editor;
    @Json(name = "Files")
    public final List<Files> files;
    @Json(name = "Text")
    public final String description;


    public NewsDetailModel(String id, String createdDate, String subTitle, String title, String editor, List<Files> files, String description) {
        this.id = id;
        this.createdDate = createdDate;
        this.subTitle = subTitle;
        this.title = title;
        this.editor = editor;
        this.files = files;
        this.description = description;
    }
}
