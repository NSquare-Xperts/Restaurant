package com.nsquare.restaurant.model.vegMenuList;

/**
 * Created by Pushkar on 07-09-2017.
 * updated by Ritu Chavan 05-12-2018
 */

public class DishCustomDataModel {

    private String dish_extra_id;
    private String dish_extra_name;
    private String dish_extra_price;
    private String dish_extra_is_chargeable;
    private String dish_extra_selection_type;
    private String dish_extra_selected;

    public String getDish_extra_id() {
        return dish_extra_id;
    }

    public void setDish_extra_id(String dish_extra_id) {
        this.dish_extra_id = dish_extra_id;
    }

    public String getDish_extra_name() {
        return dish_extra_name;
    }

    public void setDish_extra_name(String dish_extra_name) {
        this.dish_extra_name = dish_extra_name;
    }

    public String getDish_extra_price() {
        return dish_extra_price;
    }

    public void setDish_extra_price(String dish_extra_price) {
        this.dish_extra_price = dish_extra_price;
    }

    public String getDish_extra_is_chargeable() {
        return dish_extra_is_chargeable;
    }

    public void setDish_extra_is_chargeable(String dish_extra_is_chargeable) {
        this.dish_extra_is_chargeable = dish_extra_is_chargeable;
    }

    public String getDish_extra_selection_type() {
        return dish_extra_selection_type;
    }

    public void setDish_extra_selection_type(String dish_extra_selection_type) {
        this.dish_extra_selection_type = dish_extra_selection_type;
    }

    public String getDish_extra_selected() {
        return dish_extra_selected;
    }

    public void setDish_extra_selected(String dish_extra_selected) {
        this.dish_extra_selected = dish_extra_selected;
    }

    public DishCustomDataModel(String dish_extra_id, String dish_extra_name, String dish_extra_price, String dish_extra_is_chargeable, String dish_extra_selection_type, String dish_extra_selected) {
        this.dish_extra_id = dish_extra_id;
        this.dish_extra_name = dish_extra_name;
        this.dish_extra_price = dish_extra_price;
        this.dish_extra_is_chargeable = dish_extra_is_chargeable;
        this.dish_extra_selection_type = dish_extra_selection_type;
        this.dish_extra_selected = dish_extra_selected;
    }
}

