package com.mek.haberler.base;

import com.mek.haberler.feednews.NewsFeedFragment;
import com.mek.haberler.gallerynews.GalleryFragment;
import com.mek.haberler.gallerynewsdetail.GalleryHeaderPageFragment;
import com.mek.haberler.gallerynewsdetail.GalleryNewsDetailFragment;
import com.mek.haberler.networking.NetworkModule;
import com.mek.haberler.newsdetail.NewsDetailFragment;
import com.mek.haberler.readlaterpage.ReadLaterFragment;
import com.mek.haberler.roomdb.RoomModule;
import com.mek.haberler.viewmodel.ViewModelModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        NetworkModule.class,
        ViewModelModule.class,
        RoomModule.class,
        ApplicationModule.class
})
public interface ApplicationComponent {

    void inject(NewsFeedFragment newsFeedFragment);

    void inject(NewsDetailFragment newsDetailFragment);

    void inject(GalleryFragment galleryFragment);

    void inject(GalleryHeaderPageFragment galleryHeaderPageFragment);

    void inject(GalleryNewsDetailFragment galleryNewsDetailFragment);

    void inject(ReadLaterFragment readLaterFragment);
}
