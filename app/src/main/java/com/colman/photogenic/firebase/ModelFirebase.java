package com.colman.photogenic.firebase;

import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.colman.photogenic.model.Photo;
import com.colman.photogenic.sql.PhotoRepository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModelFirebase {

    private FirebaseFirestore fb = FirebaseFirestore.getInstance();
    private MutableLiveData<List<Photo>> photos;
    private FirestoreManager firestoreManager;
    private PhotoRepository mRepository;
    private CollectionReference photosCollectionReference;


    private ModelFirebase()
    {
        photosCollectionReference = FirebaseFirestore.getInstance().collection("photos");

    }


    public interface GetAllPhotosListener{
        void onComplete(LiveData<List<Photo>> data);
    }


    public void getAllPhotos(GetAllPhotosListener listener)
    {
        //PhotosCollectionReference.orderBy("name", Query.Direction.DESCENDING).addSnapshotListener(listener);

    }




    private void loadPhotos() {
        Date d = PhotoRepository.lud;


        firestoreManager.getAllPhotosFirebase(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null)
                {
                    Log.w("Meidan", "Listen failed.", e);
                    return;
                }
                List<Photo> list = new ArrayList<>();
                for (DocumentChange dc : value.getDocumentChanges())
                {
                    Photo r = new Photo(dc.getDocument().getData());
                    switch (dc.getType())
                    {
                        case ADDED:
                            mRepository.insert(r);
                            list.add(r);
                            break;
                        case MODIFIED:
                            mRepository.update(r);
                            list.forEach(photo -> {
                                if(photo.getId() == r.getId())
                                {
                                    photo.setName(r.getName());
                                    photo.setDescription(r.getDescription());
                                    photo.setImgURL(r.getImgURL());
                                }
                            });
                            break;
                        case REMOVED:
                            mRepository.delete(r);
                            break;
                    }
                }


                photos.setValue(list);
            }
        });
    }
}
