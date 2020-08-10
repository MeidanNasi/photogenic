package com.colman.photogenic.viewmodel;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.colman.photogenic.model.Photo;
import com.colman.photogenic.model.PhotoModel;

public class EditPhotoViewModel extends ViewModel {

    public void updatePhoto(Photo r, PhotoModel.Listener<Boolean> listener) {
        PhotoModel.instance.updatePhoto(r,listener);
    }

    public void deletePhoto(Photo r, PhotoModel.Listener<Boolean> listener) {
        PhotoModel.instance.delete(r, listener);
    }




    public void uploadImage(Uri uri, PhotoModel.Listener<Uri> listener) {
        PhotoModel.instance.uploadImage(uri,listener);
    }

}
