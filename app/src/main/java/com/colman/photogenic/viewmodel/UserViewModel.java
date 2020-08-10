package com.colman.photogenic.viewmodel;

import com.colman.photogenic.model.Photo;
import com.colman.photogenic.model.PhotoModel;

public class UserViewModel {

    public void updatePhoto(Photo r, PhotoModel.Listener<Boolean> listener) {
        PhotoModel.instance.updatePhoto(r,listener);
    }
}
