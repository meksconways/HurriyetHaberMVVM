package com.mek.haberler.base;

import com.mek.haberler.networking.NetworkModule;
import com.mek.haberler.viewmodel.ViewModelFactory;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        NetworkModule.class,
        ViewModelFactory.class
})
public interface ApplicationComponent {



}
