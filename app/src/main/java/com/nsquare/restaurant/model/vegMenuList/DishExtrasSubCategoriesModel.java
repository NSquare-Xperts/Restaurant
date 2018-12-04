package com.nsquare.restaurant.model.vegMenuList;

/**
 * Created by Pushkar on 07-09-2017.
 */

public class DishExtrasSubCategoriesModel {

    private String dish_extra_id;
    private String dish_extra_name;
    private String dish_extra_price;

    public DishExtrasSubCategoriesModel(String dish_extra_id, String dish_extra_name, String dish_extra_price) {
        this.dish_extra_id = dish_extra_id;
        this.dish_extra_name = dish_extra_name;
        this.dish_extra_price = dish_extra_price;
    }

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
}

