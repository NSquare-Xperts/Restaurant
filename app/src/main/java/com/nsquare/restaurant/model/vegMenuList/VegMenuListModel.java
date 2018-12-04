package com.nsquare.restaurant.model.vegMenuList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pushkar on 07-09-2017.
 * updated by Ritu Chavan 4-12-2018
 */

public class VegMenuListModel {

    private String dish_id;
    private String dish_name;
    private String dish_desc;
    private String dish_price;
    private String dish_image;
    private String dish_position;
    private boolean dish_customizable;
    private ArrayList<DishExtrasModel> dish_extras= new ArrayList<>();

    public VegMenuListModel(String dish_id, String dish_name, String dish_desc, String dish_price, String dish_image, String dish_position,boolean dish_customizable, ArrayList dishExtrasModels) {
        this.dish_id = dish_id;
        this.dish_name = dish_name;
        this.dish_desc = dish_desc;
        this.dish_price = dish_price;
        this.dish_image = dish_image;
        this.dish_position = dish_position;
        this.dish_customizable = dish_customizable;
        this.dish_extras = dishExtrasModels;
    }

    public String getDish_id() {
        return dish_id;
    }

    public void setDish_id(String dish_id) {
        this.dish_id = dish_id;
    }

    public String getDish_name() {
        return dish_name;
    }

    public void setDish_name(String dish_name) {
        this.dish_name = dish_name;
    }

    public String getDish_desc() {
        return dish_desc;
    }

    public void setDish_desc(String dish_desc) {
        this.dish_desc = dish_desc;
    }

    public String getDish_price() {
        return dish_price;
    }

    public void setDish_price(String dish_price) {
        this.dish_price = dish_price;
    }

    public String getDish_image() {
        return dish_image;
    }

    public void setDish_image(String dish_image) {
        this.dish_image = dish_image;
    }

    public String getDish_position() {
        return dish_position;
    }

    public void setDish_position(String dish_position) {
        this.dish_position = dish_position;
    }

    public boolean isDish_customizable() {
        return dish_customizable;
    }

    public void setDish_customizable(boolean dish_customizable) {
        this.dish_customizable = dish_customizable;
    }

    public ArrayList<DishExtrasModel> getDish_extras() {
        return dish_extras;
    }

    public void setDish_extras(ArrayList<DishExtrasModel> dish_extras) {
        this.dish_extras = dish_extras;
    }
}

