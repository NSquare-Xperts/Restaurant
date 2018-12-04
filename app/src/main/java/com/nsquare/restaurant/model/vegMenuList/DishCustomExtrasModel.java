package com.nsquare.restaurant.model.vegMenuList;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ritu Chavan 12-04-2018
 */

public class DishCustomExtrasModel {

    @SerializedName("Extra")
    private ArrayList<DishExtrasModel> extra= new ArrayList<>();

    @SerializedName("Quantity")
    private ArrayList<DishExtrasModel> quantity= new ArrayList<>();

    @SerializedName("Custom")
    private ArrayList<DishExtrasModel> custom= new ArrayList<>();

    public ArrayList<DishExtrasModel> getExtra() {
        return extra;
    }

    public void setExtra(ArrayList<DishExtrasModel> extra) {
        this.extra = extra;
    }

    public ArrayList<DishExtrasModel> getQuantity() {
        return quantity;
    }

    public void setQuantity(ArrayList<DishExtrasModel> quantity) {
        this.quantity = quantity;
    }

    public ArrayList<DishExtrasModel> getCustom() {
        return custom;
    }

    public void setCustom(ArrayList<DishExtrasModel> custom) {
        this.custom = custom;
    }
}

