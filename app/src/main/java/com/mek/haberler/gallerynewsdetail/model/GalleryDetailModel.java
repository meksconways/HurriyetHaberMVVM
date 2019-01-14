package com.mek.haberler.gallerynewsdetail.model;

import com.squareup.moshi.Json;

import java.util.List;


public class GalleryDetailModel {

    @Json(name = "Id")
    public final String id;
    @Json(name = "ContentType")
    public final String contentType;
    @Json(name = "CreatedDate")
    public final String createdDate;
    @Json(name = "Description")
    public final String description;
    @Json(name = "Editor")
    public final String editor;
    @Json(name = "Files")
    public final List<Files> files;
    @Json(name = "Path")
    public final String path;
    @Json(name = "StartDate")
    public final String startDate;
    @Json(name = "Tags")
    public final List<String> tags;
    @Json(name = "Text")
    public final String text;
    @Json(name = "Title")
    public final String title;
    @Json(name = "Url")
    public final String url;


    public GalleryDetailModel(String id, String contentType, String createdDate, String description,
                              String editor, List<Files> files, String path,
                              String startDate, List<String> tags,
                              String text, String title, String url) {
        this.id = id;
        this.contentType = contentType;
        this.createdDate = createdDate;
        this.description = description;
        this.editor = editor;
        this.files = files;
        this.path = path;
        this.startDate = startDate;
        this.tags = tags;
        this.text = text;
        this.title = title;
        this.url = url;
    }
}
