package com.nsquare.restaurant.model;

/**
 * Created by Pushkar on 09-04-2018.
 */

public class SubCategoryModel {

    public String subcategoryName, categryId, subcategoryId, subcategoryImage;

    public String getSubcategoryImage() {
        return subcategoryImage;
    }

    public void setSubcategoryImage(String subcategoryImage) {
        this.subcategoryImage = subcategoryImage;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getCategryId() {
        return categryId;
    }

    public void setCategryId(String categryId) {
        this.categryId = categryId;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public SubCategoryModel(String subcategoryName, String categryId, String subcategoryId, String subcategoryImage) {
        this.subcategoryName = subcategoryName;
        this.categryId = categryId;
        this.subcategoryId = subcategoryId;
        this.subcategoryImage = subcategoryImage;
    }
}
