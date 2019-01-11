package com.mek.haberler.feednews;

import android.util.Log;

import com.mek.haberler.feednews.NewsFeedModel.FeedNewsModel;
import com.mek.haberler.networking.NewsApi;
import com.mek.haberler.util.Util;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFeedViewModel extends ViewModel {


    private final MutableLiveData<List<FeedNewsModel>> news = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> newsLoadingError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> scrollToTop = new MutableLiveData<>();
    private Call<List<FeedNewsModel>> newsCall;


    public void setScroll(Boolean data){
        scrollToTop.setValue(data);
    }

    @Inject
    NewsFeedViewModel(){
        fetchNews();
    }

    LiveData<Boolean> getScrollTop(){
        return scrollToTop;
    }
    LiveData<List<FeedNewsModel>> getNews(){
        return news;
    }
    LiveData<Boolean> loading() {
        return loading;
    }
    LiveData<Boolean> getError() {
        return newsLoadingError;
    }

    private void fetchNews() {
        loading.setValue(true);
        newsCall = NewsApi.getInstance().getAllNews(Util.API_KEY);
        newsCall.enqueue(new Callback<List<FeedNewsModel>>() {
            @Override
            public void onResponse(Call<List<FeedNewsModel>> call, Response<List<FeedNewsModel>> response) {
                if (response.body() != null){
                    newsLoadingError.setValue(false);
                    news.setValue(response.body());
                    loading.setValue(false);
                    newsCall = null;
                }

            }

            @Override
            public void onFailure(Call<List<FeedNewsModel>> call, Throwable t) {
                loading.setValue(false);
                newsLoadingError.setValue(true);
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
