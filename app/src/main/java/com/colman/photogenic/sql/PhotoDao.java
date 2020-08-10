package com.colman.photogenic.sql;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.colman.photogenic.model.Photo;

import java.util.List;

@Dao
public interface PhotoDao {
    @Query("SELECT * FROM photo_table")
    List<Photo> getAll();

    @Query("SELECT * FROM photo_table WHERE id IN (:userIds)")
    List<Photo> loadAllByIds(int[] userIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Photo photo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Photo... photos);

    @Update
    void update(Photo photo);

    @Query("UPDATE photo_table SET isDeleted=:setIsDeleted WHERE id = :id")
    void updateIsDeleted(Boolean setIsDeleted, String id);


    @Delete
    void delete(Photo photo);

    @Query("DELETE FROM photo_table WHERE id = (:id)")
    void delete(String id);

    @Query("DELETE FROM photo_table")
    void deleteAll();

    @Query("SELECT * FROM photo_table")
    LiveData<List<Photo>> getAllPhotos();

    @Query("SELECT * FROM photo_table WHERE createdBy = (:email)")
    LiveData<List<Photo>> getAllPhotosByUser(String email);


}