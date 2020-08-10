package com.colman.photogenic.sql;
import android.app.Application;

import androidx.lifecycle.LiveData;

import com.colman.photogenic.model.Photo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PhotoRepository {

    private PhotoDao mPhotoDao;
    private LiveData<List<Photo>> mAllPhotos;
    public static Date lud;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();



    public PhotoRepository(Application application) {
        lud = new Date();
        lud = new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime();
        PhotoRoomDatabase db = PhotoRoomDatabase.getDatabase(application);
        mPhotoDao = db.photoDao();
        mAllPhotos = mPhotoDao.getAllPhotos();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Photo>> getAllPhotos()
    {
        return mAllPhotos;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(final Photo photo) {
        PhotoRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPhotoDao.insert(photo);
        });
    }

    public void delete(final Photo photo) {
        PhotoRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPhotoDao.delete(photo);
        });
    }

    public void update(final Photo photo) {
        PhotoRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPhotoDao.update(photo);
        });
    }

    public void delete(final String id) {
        PhotoRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPhotoDao.delete(id);
        });
    }

    public void deleteAllPhotos() {
        PhotoRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPhotoDao.deleteAll();
        });
    }

}
