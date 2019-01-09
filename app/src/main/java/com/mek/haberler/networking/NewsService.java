package com.mek.haberler.networking;

import com.mek.haberler.feednews.NewsFeedModel.FeedNewsModel;
import com.mek.haberler.util.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

import static com.mek.haberler.util.Util.API_KEY;

public interface NewsService {



    /*
    * Tüm Haberlerin 12 tanesini title-files-id olarak al
    *
    * */
    @GET("articles?$select=Title,Files,Id&$expand=Files$top=12")
    Call<List<FeedNewsModel>> getAllNews(@Header("apikey") String apikey);




}
