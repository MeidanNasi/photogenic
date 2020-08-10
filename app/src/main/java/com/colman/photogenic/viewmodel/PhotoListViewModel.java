package com.colman.photogenic.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.colman.photogenic.model.Photo;
import com.colman.photogenic.model.PhotoModel;

import java.util.List;

public class PhotoListViewModel extends ViewModel {
    LiveData<List<Photo>> liveData;

    public LiveData<List<Photo>> getData() {
        if (liveData == null) {
            liveData = PhotoModel.instance.getAllPhotos();
        }
        return liveData;
    }

    public LiveData<List<Photo>> getUserData(String email) {
        if (liveData == null) {
            liveData = PhotoModel.instance.getAllPhotosByUser(email);
        }
        return liveData;
    }


    public void refresh(PhotoModel.CompListener listener) {
        PhotoModel.instance.refreshPhotosList(listener);
    }
}
