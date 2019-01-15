package com.mek.haberler.base;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class ApplicationModule {

    private final Application application;


    ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Context provideAppContext(){
        return application;
    }

}
