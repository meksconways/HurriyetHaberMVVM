package com.mek.haberler.gallerynewsdetail;

import com.mek.haberler.gallerynewsdetail.model.Files;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryNewsDetailViewModel extends ViewModel {

    private final MutableLiveData<List<Files>> files = new MutableLiveData<>();


    @Inject
    public GalleryNewsDetailViewModel() {

    }

    void setFiles(List<Files> file){
        files.setValue(file);
    }

    public LiveData<List<Files>> getFiles() {
        return files;
    }
}
