package com.nsquare.restaurant.model;

/**
 * Created by Pushkar on 07-09-2017.
 */

public class PhotosModel {

    String id, photoUrl;

    public PhotosModel(String id, String photoUrl) {
        this.id = id;
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
