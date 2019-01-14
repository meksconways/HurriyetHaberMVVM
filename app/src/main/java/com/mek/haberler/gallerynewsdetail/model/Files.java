package com.mek.haberler.gallerynewsdetail.model;

import com.squareup.moshi.Json;

public class Files {

    @Json(name = "FileUrl")
    public final String fileUrl;
    @Json(name = "Metadata")
    public final Metadata metadata;

    public Files(String fileUrl, Metadata metadata) {
        this.fileUrl = fileUrl;
        this.metadata = metadata;
    }
}
