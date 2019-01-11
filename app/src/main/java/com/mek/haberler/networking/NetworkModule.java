package com.mek.haberler.networking;

import com.mek.haberler.util.Util;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public abstract class NetworkModule {

    @Provides
    @Singleton
    static Retrofit provideRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(Util.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

    }

    @Provides
    @Singleton
    static NewsService provideNewsService(Retrofit retrofit){
        return retrofit.create(NewsService.class);
    }

}
