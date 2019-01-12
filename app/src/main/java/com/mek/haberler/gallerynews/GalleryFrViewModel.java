package com.mek.haberler.gallerynews;

import com.mek.haberler.gallerynews.model.GalleryNewsModel;
import com.mek.haberler.networking.NewsService;
import com.mek.haberler.util.Util;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryFrViewModel extends ViewModel {


    private final MutableLiveData<List<GalleryNewsModel>> galleryNews = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> refresh = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> scrollToTop = new MutableLiveData<>();
    private final NewsService newsService;
    private Call<List<GalleryNewsModel>> newsCall;

    @Inject
    GalleryFrViewModel(NewsService newsService) {
        this.newsService = newsService;
        fetchGalleryNews();
    }

    public void setScroll(Boolean data){
        scrollToTop.setValue(data);
    }

    void setRefresh(Boolean data){
        refresh.setValue(data);
        if (data){
            fetchGalleryNews();
        }
    }
    LiveData<Boolean> getScrollTop(){
        return scrollToTop;
    }
    LiveData<List<GalleryNewsModel>> getNews(){
        return galleryNews;
    }
    LiveData<Boolean> isError(){
        return isError;
    }
    LiveData<Boolean> isLoading(){
        return loading;
    }
    LiveData<Boolean> isRefreshing(){
        return refresh;
    }

    private void fetchGalleryNews() {

        //noinspection ConstantConditions
        if (!refresh.getValue()){
            loading.setValue(true);

        }else{
            loading.setValue(false);
        }
        newsCall = newsService.getNewsGallery(Util.API_KEY);
        newsCall.enqueue(new Callback<List<GalleryNewsModel>>() {
            @Override
            public void onResponse(Call<List<GalleryNewsModel>> call, Response<List<GalleryNewsModel>> response) {
                if (response.body() != null){
                    isError.setValue(false);
                    galleryNews.setValue(response.body());
                    loading.setValue(false);
                    refresh.setValue(false);
                    newsCall = null;
                }
            }

            @Override
            public void onFailure(Call<List<GalleryNewsModel>> call, Throwable t) {
                loading.setValue(false);
                isError.setValue(true);
                refresh.setValue(false);
                newsCall = null;
            }
        });

    }
    @Override
    protected void onCleared() {
        if (newsCall != null) {
            newsCall.cancel();
            newsCall = null;
        }
    }


}
