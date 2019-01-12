package com.mek.haberler.networking;

import com.mek.haberler.feednews.NewsFeedModel.FeedNewsModel;
import com.mek.haberler.feednews.NewsFeedModel.Files;
import com.mek.haberler.gallerynews.model.GalleryNewsModel;
import com.mek.haberler.newsdetail.model.NewsDetailModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface NewsService {



    /**
    * Tüm Haberlerin 12 tanesini title-files-id olarak al
    *
    * */
    @GET("articles?$select=Title,Files,Id&$expand=Files")
    Call<List<FeedNewsModel>> getAllNews(@Header("apikey") String apikey);

    /**
    * Haber Detayını, Id,CreatedDate,Description,Files,Text olarak getir.
    * Info: expand: eğer child sahibi ise kullanılır
    * */
    @GET("articles/{id}?$select=Id,CreatedDate,Description,Files,Editor,Title,Text&$expand=Files")
    Call<NewsDetailModel> getNewsDetail(@Header("apikey")  String apikey,
                                        @Path("id") String newsId);

    /**
     * Galerideki files dosyasını getir.
     *
     */
    @GET("newsphotogalleries?$select=Files,Id&$expand=Files")
    Call<List<GalleryNewsModel>> getNewsGallery(@Header("apikey") String apikey);




}
