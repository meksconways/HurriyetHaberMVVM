package com.mek.haberler.networking;

import com.mek.haberler.util.Util;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/*
* Dagger 2 ekledikten sonra kullanmaya gerek kalmadı
*
* */
public class NewsApi {

    private static Retrofit retrofit;
    private static NewsService newsService;

    /*
    * NewsService sınıfından singleton nesne üretimi
    *
    * */
    public static NewsService getInstance(){
        if (newsService != null) {
            return newsService;
        }
        if (retrofit == null){
            initializeRetrofit();
        }
        newsService = retrofit.create(NewsService.class);
        return newsService;

    }

    private static void initializeRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Util.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();


    }

    public NewsApi(){



    }


}
