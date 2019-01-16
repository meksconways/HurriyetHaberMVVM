package com.mek.haberler.newsdetail;

import android.os.Handler;
import android.os.Looper;

import com.mek.haberler.networking.NewsService;
import com.mek.haberler.newsdetail.model.NewsDetailModel;
import com.mek.haberler.roomdb.NewsDB;
import com.mek.haberler.roomdb.NewsDao;
import com.mek.haberler.util.Util;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDetailViewModel extends ViewModel {


    private final NewsService newsService;
    private final NewsDao newsDao;
    private final MutableLiveData<NewsDetailModel> detail = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> detailError = new MutableLiveData<>();
    private final MutableLiveData<String> newsId = new MutableLiveData<>();
    private final MutableLiveData<Integer> scrollYPos = new MutableLiveData<>(0);
    private Call<NewsDetailModel> detailCall;
    private final MutableLiveData<Boolean> newsFromDB = new MutableLiveData<>();




    private NewsDB newsDB;

    void isInDB(){

        new Thread(() -> {
            newsDB = newsDao.getNewsDetailfromRoom(newsId.getValue());
            new Handler(Looper.getMainLooper()).post(() -> updateState(newsDB));
        }).start();

    }

    void updateState(NewsDB newsDB){

        if (newsDB != null){
            newsFromDB.setValue(true);
        }else{
            newsFromDB.setValue(false);
        }
    }



    void saveToRoom(){
        NewsDB newsDB = new NewsDB();
        //noinspection ConstantConditions
        newsDB.setNewsId(detail.getValue().id);
        newsDB.setCreatedDate(detail.getValue().createdDate);
        newsDB.setDescription(detail.getValue().description);
        newsDB.setEditor(detail.getValue().editor);
        newsDB.setSubTitle(detail.getValue().subTitle);
        newsDB.setTitle(detail.getValue().title);
        if (detail.getValue().files.size() > 0){
            newsDB.setPhoto(detail.getValue().files.get(0).fileUrl);
        }
        new Thread(() -> {
            newsDao.insertNewsfromRoom(newsDB);
        }).start();

    }

    void deleteFromRoom(){
        NewsDB newsDB = new NewsDB();
        //noinspection ConstantConditions
        newsDB.setNewsId(detail.getValue().id);
        newsDB.setCreatedDate(detail.getValue().createdDate);
        newsDB.setDescription(detail.getValue().description);
        newsDB.setEditor(detail.getValue().editor);
        newsDB.setSubTitle(detail.getValue().subTitle);
        newsDB.setTitle(detail.getValue().title);
        if (detail.getValue().files.size() > 0){
            newsDB.setPhoto(detail.getValue().files.get(0).fileUrl);
        }
        new Thread(() -> newsDao.deleteNewsfromRoom(newsDB)).start();
    }

    void setScrollYPos(Integer pos){
        scrollYPos.setValue(pos);
    }

    @Inject
    NewsDetailViewModel(NewsService newsService, NewsDao newsDao){
        this.newsService = newsService;
        this.newsDao = newsDao;
    }



    void setSelectedNews(String newsID){
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


    private void fetchDetail(String news_id) {
        loading.setValue(true);

        detailCall = newsService.getNewsDetail(Util.API_KEY,news_id);
        detailCall.enqueue(new Callback<NewsDetailModel>() {
            @Override
            public void onResponse(Call<NewsDetailModel> call, Response<NewsDetailModel> response) {
                if (response.body() != null){
                    detailError.setValue(false);
                    detail.setValue(response.body());
                    loading.setValue(false);
                    detailCall = null;
                    isInDB();
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

    LiveData<Boolean> getIsInDB(){
        return newsFromDB;
    }
    LiveData<Integer> getScrollY(){
        return scrollYPos;
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







}
