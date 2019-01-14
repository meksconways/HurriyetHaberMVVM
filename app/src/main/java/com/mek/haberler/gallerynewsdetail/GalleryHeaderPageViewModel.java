package com.mek.haberler.gallerynewsdetail;

import com.mek.haberler.gallerynewsdetail.model.GalleryDetailModel;
import com.mek.haberler.networking.NewsService;
import com.mek.haberler.util.Util;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryHeaderPageViewModel extends ViewModel {

    private final MutableLiveData<GalleryDetailModel> news = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> error = new MutableLiveData<>();
    private MutableLiveData<String> newsId = new MutableLiveData<>();
    private final NewsService newsService;
    private Call<GalleryDetailModel> newsCall;

    void setSelectedNews(String newsID){
        if (newsId.getValue() == null){
            newsId.setValue(newsID);
            fetchData(newsID);
        }else{
            if (!newsId.getValue().equals(newsID)){
                newsId.setValue(newsID);
                fetchData(newsID);
            }
        }
    }

    @Inject
    GalleryHeaderPageViewModel(NewsService newsService) {
        this.newsService = newsService;
    }

    private void fetchData(String newsID) {
        loading.setValue(true);
        newsCall = newsService.getGalleryNewsDetail(Util.API_KEY,newsID);
        newsCall.enqueue(new Callback<GalleryDetailModel>() {
            @Override
            public void onResponse(Call<GalleryDetailModel> call, Response<GalleryDetailModel> response) {
                if (response.body() != null){
                    error.setValue(false);
                    news.setValue(response.body());
                    loading.setValue(false);
                    newsCall = null;
                }
            }

            @Override
            public void onFailure(Call<GalleryDetailModel> call, Throwable t) {
                loading.setValue(false);
                error.setValue(true);
                newsCall = null;
            }
        });

    }

    LiveData<GalleryDetailModel> getNews(){
        return news;
    }
    LiveData<Boolean> getError(){
        return error;
    }
    LiveData<Boolean> getLoading(){
        return loading;
    }

    @Override
    protected void onCleared() {
        if (newsCall != null) {
            newsCall.cancel();
            newsCall = null;
        }
    }


}
