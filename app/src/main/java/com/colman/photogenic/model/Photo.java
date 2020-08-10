package com.colman.photogenic.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.colman.photogenic.utils.Converters;
import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Entity(tableName = "photo_table")
public class Photo implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "imgURL")
    public String imgURL;
    @ColumnInfo(name = "createdBy")
    public String createdBy;
    @ColumnInfo(name = "category")
    public String category;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "timestamp")
    @TypeConverters({Converters.class})
    public Date timestamp;
    @ColumnInfo(name = "isDeleted")
    public Boolean isDeleted;


    public Photo(@NonNull String id, String name, String imgURL, String createdBy, String category, String description, Date timestamp) {
        this.id = id;
        this.name = name;
        this.imgURL = imgURL;
        this.createdBy = createdBy;
        this.category = category;
        this.description = description;
        this.timestamp = timestamp;
        this.isDeleted = false;
    }

    public Photo() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Photo(Map<String, Object> data) {
        this.id = ((String) data.get("id"));
        this.name = ((String) data.get("name"));
        this.imgURL = ((String) data.get("imgURL"));
        this.createdBy = ((String) data.get("createdBy"));
        this.category = ((String) data.get("category"));
        this.description = ((String) data.get("description"));
        this.timestamp = timestamp_to_date((Timestamp) data.get("timestamp"));
        this.isDeleted = ((Boolean) data.get("isDeleted"));

    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date timestamp_to_date(Timestamp ts){
        return ts.toDate();
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean isDeleted() {
        return this.isDeleted;
    }

    public void delete() {
        this.isDeleted = true;
    }


}