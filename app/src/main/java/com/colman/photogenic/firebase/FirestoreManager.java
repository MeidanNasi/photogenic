package com.colman.photogenic.firebase;

import com.colman.photogenic.sql.PhotoRepository;
import com.colman.photogenic.viewmodel.PhotosViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreManager {

    private static FirestoreManager photosFirestoreManager;
    private CollectionReference photosCollectionReference;
    private CollectionReference lastupdateCollectionReference;
    private PhotoRepository mRepository;


    public static FirestoreManager newInstance() {
        if (photosFirestoreManager == null) {
            photosFirestoreManager = new FirestoreManager();
        }
        return photosFirestoreManager;
    }

    private FirestoreManager() {
        photosCollectionReference = FirebaseFirestore.getInstance().collection("photos");
        lastupdateCollectionReference = FirebaseFirestore.getInstance().collection("lastupdate");
    }


    public void getAllPhotosFirebase(OnCompleteListener<QuerySnapshot> onCompleteListener)
    {
        photosCollectionReference.get().addOnCompleteListener(onCompleteListener);


    }

    public void getAllPhotosFirebase(EventListener<QuerySnapshot> eventListener)
    {
        photosCollectionReference.orderBy("name", Query.Direction.DESCENDING).addSnapshotListener(eventListener);
    }

    public void getLastUpdateFirebase(OnCompleteListener<DocumentSnapshot> onCompleteListener)
    {
        lastupdateCollectionReference.document("lud").get().addOnCompleteListener(onCompleteListener);
    }

    public void delete(String id, PhotosViewModel.MyCallback callback)
    {

        photosCollectionReference.document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mRepository.getAllPhotos();
                callback.onDataGot("Deleted");
            }
        });
    }

}
