package com.nsquare.restaurant.model.vegMenuList;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ritu Chavan 05-12-2018
 */

public class DishCustomModel {

    private String group_id;
    private String group_label;

    @SerializedName("data")
    private ArrayList<DishCustomDataModel> dishCustomDataModelArrayList;

    public String getGroup_id() {
        return group_id;
    }
    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }
    public String getGroup_label() {
        return group_label;
    }
    public void setGroup_label(String group_label) {
        this.group_label = group_label;
    }
    public DishCustomModel(String group_id, String group_label, ArrayList<DishCustomDataModel> dishCustomDataModelArrayList) {
        this.group_id = group_id;
        this.group_label = group_label;
        this.dishCustomDataModelArrayList = dishCustomDataModelArrayList;
    }
}

