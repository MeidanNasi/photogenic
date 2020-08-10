package com.colman.photogenic.model;

import java.io.Serializable;

public class Category implements Serializable {


    public String name;
    public String image;


    public Category() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Category(String name) {
        this.name = name;
    }
    public Category(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}