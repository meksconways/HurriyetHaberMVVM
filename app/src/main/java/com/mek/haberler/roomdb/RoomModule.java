package com.mek.haberler.roomdb;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class RoomModule {


    @Provides
    @Singleton
    static AppDatabase provideAppDatabase(Context context){
        return AppDatabase.getAppDatabase(context);
    }

    @Provides
    @Singleton
    static NewsDao getNewsDao(AppDatabase appDatabase){
        return appDatabase.userDao();
    }

}
