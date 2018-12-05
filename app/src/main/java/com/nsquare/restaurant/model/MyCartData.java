package com.nsquare.restaurant.model;

/**
 * Created by Pushkar on 07-09-2017.
 */

public class MyCartData {

    public String id;
    public String dish_quantity;
    public String dish_price;
    public String dish_total_price;

    public MyCartData(String id, String dish_quantity, String dish_price, String dish_total_price) {
        this.id = id;
        this.dish_quantity = dish_quantity;
        this.dish_price = dish_price;
        this.dish_total_price = dish_total_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDish_quantity() {
        return dish_quantity;
    }

    public void setDish_quantity(String dish_quantity) {
        this.dish_quantity = dish_quantity;
    }

    public String getDish_price() {
        return dish_price;
    }

    public void setDish_price(String dish_price) {
        this.dish_price = dish_price;
    }

    public String getDish_total_price() {
        return dish_total_price;
    }

    public void setDish_total_price(String dish_total_price) {
        this.dish_total_price = dish_total_price;
    }
}
