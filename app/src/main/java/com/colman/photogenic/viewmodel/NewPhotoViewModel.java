package com.colman.photogenic.viewmodel;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.colman.photogenic.model.Photo;
import com.colman.photogenic.model.PhotoModel;

public class NewPhotoViewModel extends ViewModel {

    public void addPhoto(Photo r, PhotoModel.Listener<Boolean> listener) {
        PhotoModel.instance.addPhoto(r,listener);
    }

    public void newPhotoId(PhotoModel.Listener<String> listener) {
        PhotoModel.instance.newPhotoId(listener);
    }

    public void uploadImage(Uri uri, PhotoModel.Listener<Uri> listener) {
        PhotoModel.instance.uploadImage(uri,listener);
    }

}
