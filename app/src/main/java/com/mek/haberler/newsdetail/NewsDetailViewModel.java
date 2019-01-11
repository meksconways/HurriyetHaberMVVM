package com.mek.haberler.newsdetail;

import android.os.Bundle;
import android.util.Log;

import com.mek.haberler.networking.NewsApi;
import com.mek.haberler.newsdetail.model.NewsDetailModel;
import com.mek.haberler.util.Util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDetailViewModel extends ViewModel {


    private MutableLiveData<NewsDetailModel> detail = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private MutableLiveData<Boolean> detailError = new MutableLiveData<>();
    private MutableLiveData<String> newsId = new MutableLiveData<>();
    private Call<NewsDetailModel> detailCall;


    NewsDetailViewModel(){
    }

    public void setSelectedNews(String newsID){
        if (newsId.getValue() == null){
            newsId.setValue(newsID);
            fetchDetail(newsID);
        }else{
            if (!newsId.getValue().equals(newsID)){
                newsId.setValue(newsID);
                fetchDetail(newsID);
            }
        }


    }


    public void fetchDetail(String news_id) {
        Log.d( "----fetchDetail: ","oluştu");
        loading.setValue(true);
        detailCall = NewsApi.getInstance().getNewsDetail(Util.API_KEY,news_id);
        detailCall.enqueue(new Callback<NewsDetailModel>() {
            @Override
            public void onResponse(Call<NewsDetailModel> call, Response<NewsDetailModel> response) {
                if (response.body() != null){
                    detailError.setValue(false);
                    detail.setValue(response.body());
                    loading.setValue(false);
                    detailCall = null;
                }
            }

            @Override
            public void onFailure(Call<NewsDetailModel> call, Throwable t) {
                loading.setValue(false);
                detailError.setValue(true);
                detailCall = null;
            }
        });
    }

    @Override
    protected void onCleared() {
        if (detailCall != null) {
            detailCall.cancel();
            detailCall = null;
        }
    }

    LiveData<NewsDetailModel> getDetail(){
        return detail;
    }
    LiveData<Boolean> isLoading(){
        return loading;
    }
    LiveData<Boolean> isError(){
        return detailError;
    }
    LiveData<String> getNewsId(){
        return newsId;
    }






}