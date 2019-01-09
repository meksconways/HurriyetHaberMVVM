package com.mek.haberler.feednews.NewsFeedModel;

import com.squareup.moshi.Json;

public class Files {


    @Json(name = "FileUrl")
    public final String fileUrl;


    public Files(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
