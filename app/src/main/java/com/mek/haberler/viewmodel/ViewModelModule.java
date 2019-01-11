package com.mek.haberler.viewmodel;

import com.mek.haberler.feednews.NewsFeedViewModel;
import com.mek.haberler.newsdetail.NewsDetailViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.multibindings.IntoMap;

public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NewsFeedViewModel.class)
    abstract ViewModel bindNewsFeedViewModel(NewsFeedViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NewsDetailViewModel.class)
    abstract ViewModel bindNewsDetailViewModel(NewsDetailViewModel viewModel);

}
