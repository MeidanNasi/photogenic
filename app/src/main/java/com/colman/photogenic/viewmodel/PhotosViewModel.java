package com.colman.photogenic.viewmodel;
import android.app.Application;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.colman.photogenic.firebase.FirestoreManager;
import com.colman.photogenic.model.Photo;
import com.colman.photogenic.model.PhotoModel;
import com.colman.photogenic.sql.PhotoRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

class Constants {

    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "uploads";
}

public class PhotosViewModel extends AndroidViewModel {

    private final String REFERENCE_URL = "gs://photogenic-4400f.appspot.com";
    private StorageReference storageRef;
    private UploadTask uploadTask;
    LiveData<List<Photo>> liveData;

    private FirebaseFirestore fb;
    private FirebaseStorage fbStorage;
    private PhotoRepository mRepository;
    private LiveData<List<Photo>> mAllPhotos;
    private MutableLiveData<List<Photo>> photos;
    private FirestoreManager firestoreManager;


    public PhotosViewModel (Application application)
    {
        super(application);
        fb = FirebaseFirestore.getInstance();
        mRepository = new PhotoRepository(application);
        mAllPhotos = mRepository.getAllPhotos();
        firestoreManager = FirestoreManager.newInstance();
        fbStorage = FirebaseStorage.getInstance();
        storageRef = fbStorage.getReferenceFromUrl(REFERENCE_URL);
    }


    ///////////////////// Eliav ////////////////////////

    public LiveData<List<Photo>> getData() {
        if (liveData == null) {
            liveData = PhotoModel.instance.getAllPhotos();
        }
        return liveData;
    }

    public void refresh(PhotoModel.CompListener listener) {
        PhotoModel.instance.refreshPhotosList(listener);
    }


    ///////////////////////////////////////////////////






    public PhotosViewModel (){
        super(null);
    }

    public void update(Map<String, Object> data,MyCallback callback)
    {
        String photoId = data.get("id").toString();
        mRepository.delete(photoId);
        PhotoRepository.lud = new Date();
        fb.collection("photos").document(photoId).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                callback.onDataGot("Success");
            }
        });
    }


    public String getFirebaseId(){
        return fb.collection("photos").document().getId();
    }


    public void insert(Photo photo, MyCallback callback)
    {
        fb.collection("photos").document(photo.getId()).set(photo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Meidan", "XXXXXXXXXXXXXX -----  Success ----------- XXXXXXXXX");
                PhotoRepository.lud = new Date();
                callback.onDataGot("Inserted");
            }
        });
    }

    public void delete(String id,MyCallback callback)
    {

        fb.collection("photos").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mRepository.deleteAllPhotos();
                callback.onDataGot("Deleted");
            }
        });
    }


    public void uploadImage(String id, Uri imagePath , MyCallback callback)
    {
        storageRef = fbStorage.getReference();
        StorageReference photoImgRef = storageRef.child("images/"+imagePath.getLastPathSegment());
        uploadTask = photoImgRef.putFile(imagePath);


        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                photoImgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        callback.onDataGot(uri.toString());
                    }
                });
            }
        });
    }


    public LiveData<List<Photo>> getPhotos() {
        if (photos == null) {
            photos = new MutableLiveData<List<Photo>>();
            loadPhotos();
        }
        return mAllPhotos;
    }


    private void loadPhotos() {

        // 1. Get from local DB the last update DateTime of specific (each Category is row from local DB)
        // 2. Get all photos By Category from Firebase newer than 'lud' timestamp
        // 3. If data != null
        // 3.1 push all photos to SQL
        // 3.2 lud = newestPhotos.timestamp
        // 3.3 update SQL with new timestamp
        // 4. getAllPhotos() from SQL
        // 5. return getAllPhotos()

        // Do an asynchronous operation to fetch users.

        Date d = PhotoRepository.lud;


        firestoreManager.getAllPhotosFirebase(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null)
                {
                    Log.w("meidan", "Listen failed.", e);
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

    public interface MyCallback {

        void onDataGot(String string);
    }


}
