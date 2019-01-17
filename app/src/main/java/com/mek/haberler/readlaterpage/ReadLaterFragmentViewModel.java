package com.mek.haberler.readlaterpage;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.mek.haberler.roomdb.NewsDB;
import com.mek.haberler.roomdb.NewsDao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReadLaterFragmentViewModel extends ViewModel {

    private final MutableLiveData<List<NewsDB>> news = new MutableLiveData<>();
    private final MutableLiveData<Boolean> emptyData = new MutableLiveData<>();



    LiveData<List<NewsDB>> getNews(){
        return news;
    }
    LiveData<Boolean> getEmptyData(){
        return emptyData;
    }

    private final NewsDao newsDao;

    @Inject
    ReadLaterFragmentViewModel(NewsDao newsDao) {
        this.newsDao = newsDao;
        fetchData();
    }


    private final List<NewsDB> newsDBList = new ArrayList<>();

    void updateStates(List<NewsDB> newsDBList){
        news.setValue(newsDBList);
        if (news.getValue() != null) {
            if (news.getValue().size() > 0){
                emptyData.setValue(false);
            }else{
                emptyData.setValue(true);
            }
        }else{
            emptyData.setValue(true);
        }
    }

    public void fetchData() {
        Log.d( "fetchData: ","girdi ***");
        newsDBList.clear();
        new Thread(() -> {
          newsDBList.addAll(newsDao.getAllNewsfromRoom());
            new Handler(Looper.getMainLooper()).post(() -> updateStates(newsDBList));

        }).start();





    }
}
