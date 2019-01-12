package com.mek.haberler.viewmodel;

import com.mek.haberler.feednews.NewsFeedViewModel;
import com.mek.haberler.gallerynews.GalleryFrViewModel;
import com.mek.haberler.newsdetail.NewsDetailViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NewsFeedViewModel.class)
    abstract ViewModel bindNewsFeedViewModel(NewsFeedViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NewsDetailViewModel.class)
    abstract ViewModel bindNewsDetailViewModel(NewsDetailViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GalleryFrViewModel.class)
    abstract ViewModel bindGalleryNewsViewModel(GalleryFrViewModel viewModel);

}
