package com.mek.haberler.feednews.NewsFeedModel;

import com.squareup.moshi.Json;

import java.util.List;

public class FeedNewsModel {

    @Json(name = "Id")
    public final String id;
    @Json(name = "Title")
    public final String title;
    @Json(name = "Files")
    public final List<Files> files;


    public FeedNewsModel(String id, String title, List<Files> files) {
        this.id = id;
        this.title = title;
        this.files = files;
    }
}
