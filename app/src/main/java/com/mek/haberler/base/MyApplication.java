package com.mek.haberler.base;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {


    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static ApplicationComponent getAppComponent(Context context){
        return ((MyApplication) context.getApplicationContext()).component;
    }
}
