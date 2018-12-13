package com.nsquare.restaurant.model;

/**
 * Created by Ritu Chavan on 12-12-2018.
 */

public class OrderMenuDetails {

    public String dish_id;
    public String dish_name;
    public String dish_price;
    public String dish_quantity;
    public String dish_total_amount;
    public String is_processed;


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

    public String getDish_price() {
        return dish_price;
    }

    public void setDish_price(String dish_price) {
        this.dish_price = dish_price;
    }

    public String getDish_quantity() {
        return dish_quantity;
    }

    public void setDish_quantity(String dish_quantity) {
        this.dish_quantity = dish_quantity;
    }

    public String getDish_total_amount() {
        return dish_total_amount;
    }

    public void setDish_total_amount(String dish_total_amount) {
        this.dish_total_amount = dish_total_amount;
    }

    public String getIs_processed() {
        return is_processed;
    }

    public void setIs_processed(String is_processed) {
        this.is_processed = is_processed;
    }
}
