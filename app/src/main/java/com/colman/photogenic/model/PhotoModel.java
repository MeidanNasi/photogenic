package com.colman.photogenic.model;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.colman.photogenic.MainActivity;
import com.colman.photogenic.sql.PhotoRoomDatabase;

import static android.content.Context.MODE_PRIVATE;

import java.util.Date;
import java.util.List;

public class PhotoModel {
    public static final PhotoModel instance = new PhotoModel();
    SharedPreferences sharedPreferences;
    Context ctx;
    Long last;


    public interface Listener<T>{
        void onComplete(T data);
    }
    public interface CompListener{
        void onComplete();
    }
    private PhotoModel(){
        ctx = MainActivity.context;
        sharedPreferences = ctx.getSharedPreferences("TAG", MODE_PRIVATE);
    }

    public void newPhotoId(Listener<String> listener) {
        PhotoFirebase.newPhotoId(listener);
    }

    public void uploadImage(Uri uri, Listener<Uri> listener) {
        PhotoFirebase.uploadImage(uri,listener);
    }


    public void addPhoto(Photo photo, Listener<Boolean> listener) {
        PhotoFirebase.addPhoto(photo,listener);
    }

    public void updatePhoto(Photo photo, Listener<Boolean> listener) {
        PhotoFirebase.updatePhoto(photo,listener);
    }

    public LiveData<List<Photo>> getAllPhotos(){
        LiveData<List<Photo>> liveData = PhotoRoomDatabase.getDatabase(MainActivity.context).photoDao().getAllPhotos();
        refreshPhotosList(null);
        return liveData;
    }

    public LiveData<List<Photo>> getAllPhotosByUser(String email){
        LiveData<List<Photo>> liveData = PhotoRoomDatabase.getDatabase(MainActivity.context).photoDao().getAllPhotosByUser(email);
        refreshPhotosList(null);
        return liveData;
    }

    public void delete(Photo photo, Listener<Boolean> listener){
        PhotoFirebase.deletePhoto(photo, new Listener<Boolean>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(Boolean data) {
                new AsyncTask<String,String,String>(){

                    @Override
                    protected String doInBackground(String... strings) {

                        // If to delete from local db or not
                        PhotoRoomDatabase.getDatabase(ctx).photoDao().update(photo);
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener!=null)  listener.onComplete(true);
                    }
                }.execute("");
            }
        });
    }

    public void refreshPhotosList(final CompListener listener){

        last = sharedPreferences.getLong("PhotosLastUpdateDate",0);
        PhotoFirebase.getAllPhotosSince(last,new Listener<List<Photo>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Photo> data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        Date d = new Date(0);
                        for(Photo photo : data)
                        {
                            if(!photo.isDeleted())
                            {
                                PhotoRoomDatabase.getDatabase(ctx).photoDao().insert(photo);
                            }
                            else
                            {
                                PhotoRoomDatabase.getDatabase(ctx).photoDao().delete(photo.getId());
                            }
                            if(photo.timestamp.after(d) && !photo.isDeleted())
                            {
                                last = photo.timestamp.getTime();
                            }
                        }
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.commit();
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener!=null)  listener.onComplete();
                    }
                }.execute("");
            }
        });
    }
    
}
