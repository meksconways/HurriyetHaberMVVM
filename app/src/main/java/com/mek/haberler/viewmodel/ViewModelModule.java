package com.mek.haberler.viewmodel;

import com.mek.haberler.feednews.NewsFeedViewModel;
import com.mek.haberler.gallerynews.GalleryFrViewModel;
import com.mek.haberler.gallerynewsdetail.GalleryHeaderPageViewModel;
import com.mek.haberler.gallerynewsdetail.GalleryNewsDetailViewModel;
import com.mek.haberler.newsdetail.NewsDetailViewModel;
import com.mek.haberler.readlaterpage.ReadLaterFragmentViewModel;

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

    @Binds
    @IntoMap
    @ViewModelKey(GalleryHeaderPageViewModel.class)
    abstract ViewModel bindgalleryHeaderPageViewModel(GalleryHeaderPageViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GalleryNewsDetailViewModel.class)
    abstract ViewModel bindGallerynewsDetailViewModel(GalleryNewsDetailViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ReadLaterFragmentViewModel.class)
    abstract ViewModel bindReadLaterFragmentViewModel(ReadLaterFragmentViewModel viewModel);

}
