package com.mek.haberler.gallerynewsdetail.model;

import com.squareup.moshi.Json;

public class Metadata {

    @Json(name = "Title")
    public final String title;
    @Json(name = "Description")
    public final String description;

    public Metadata(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
