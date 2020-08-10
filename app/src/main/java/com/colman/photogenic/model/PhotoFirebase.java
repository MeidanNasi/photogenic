package com.colman.photogenic.model;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.colman.photogenic.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PhotoFirebase {
    final static String PHOTOS_COLLECTION = "photos";
    public static Context ctx = MainActivity.context;


    public static void uploadImage(Uri imagePath, final PhotoModel.Listener<Uri> listener) {
        StorageReference storageRef;
        UploadTask uploadTask;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        StorageReference photoImgRef = storageRef.child("images/"+imagePath.getLastPathSegment());
        uploadTask = photoImgRef.putFile(imagePath);


        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                photoImgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (listener!=null){
                            listener.onComplete(task.getResult());
                        }
                    }
                });
            }
        });
    }

    public static void deletePhoto(Photo photo, final PhotoModel.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // set isDeleted to true
        photo.delete();
        db.collection(PHOTOS_COLLECTION).document(photo.getId()).set(toJson(photo)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener!=null){
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }


    public static void addPhoto(Photo photo, final PhotoModel.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(PHOTOS_COLLECTION).document(photo.getId()).set(toJson(photo)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener!=null){
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }

    public static void updatePhoto(Photo photo, final PhotoModel.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(PHOTOS_COLLECTION).document(photo.getId()).set(toJson(photo)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener!=null){
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }

    public static void newPhotoId(final PhotoModel.Listener<String> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        listener.onComplete(db.collection(PHOTOS_COLLECTION).document().getId());
    }



    // With Long parameter
    public static void getAllPhotosSince(Long last, final PhotoModel.Listener<List<Photo>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Date d = new Date(last);
        //Timestamp ts = new Timestamp(last, 0);
        db.collection(PHOTOS_COLLECTION)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Photo> PhotosData = null;
                if (task.isSuccessful()){
                    PhotosData = new LinkedList<>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Map<String,Object> json = doc.getData();
                        Photo photo = factory(json);
                        if(photo.getTimestamp().after(d))
                        {
                            PhotosData.add(photo);
                        }
                    }
                }
                listener.onComplete(PhotosData);
                Log.d("TAG","refresh " + PhotosData.size());
            }
        });
    }


    public static void getAllPhotos(final PhotoModel.Listener<List<Photo>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(PHOTOS_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Photo> photosData = null;
                if (task.isSuccessful()){
                    photosData = new LinkedList<Photo>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Photo photo = doc.toObject(Photo.class);
                        photosData.add(photo);
                    }
                }
                listener.onComplete(photosData);
            }
        });
    }
    


    private static Map<String, Object> toJson(Photo photo){
        HashMap<String, Object> result = new HashMap<>();


        result.put("id", photo.getId());
        result.put("name", photo.getName());
        result.put("category", photo.getCategory());
        result.put("createdBy", photo.getCreatedBy());
        result.put("description", photo.getDescription());
        result.put("imgURL", photo.getImgURL());
        result.put("timestamp",new Timestamp(new Date()));
        result.put("isDeleted", photo.isDeleted());


        return result;
    }


    private static Photo factory(Map<String, Object> json){
        Photo photo = new Photo();
        photo.id = (String)json.get("id");
        photo.name = (String)json.get("name");
        photo.imgURL = (String)json.get("imgURL");
        photo.category = (String)json.get("category");
        photo.description = (String)json.get("description");
        photo.createdBy = (String)json.get("createdBy");
        Timestamp ts = (Timestamp)json.get("timestamp");
        photo.isDeleted = (Boolean) json.get("isDeleted");


        if (ts != null) photo.timestamp = new Date();
        return photo;
    }
}
