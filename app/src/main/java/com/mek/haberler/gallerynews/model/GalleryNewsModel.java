package com.mek.haberler.gallerynews.model;

import com.mek.haberler.feednews.NewsFeedModel.Files;
import com.squareup.moshi.Json;

import java.util.List;

public class GalleryNewsModel {

    @Json(name = "Files")
    public final List<Files> files;
    @Json(name = "Id")
    public final String id;


    public GalleryNewsModel(List<Files> files, String id) {
        this.files = files;
        this.id = id;
    }
}
