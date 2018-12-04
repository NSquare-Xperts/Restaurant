package com.nsquare.restaurant.model;

/**
 * Created by Pushkar on 07-09-2017.
 */

public class CartModel {

    public String database_unique_id = "database_unique_id";
    public String database_order_menus_id = "database_order_menus_id";
    public String database_user_id = "database_user_id";
    public String database_menu_id = "database_menu_id";
    public String database_menu_name = "database_menu_name";
    public String database_menu_price = "database_menu_price";
    public String database_menu_image = "database_menu_image";
    public String database_menu_quantity = "database_menu_quantity";
    public String database_menu_status = "database_menu_status";

    public String getDatabase_order_menus_id() {
        return database_order_menus_id;
    }

    public void setDatabase_order_menus_id(String database_order_menus_id) {
        this.database_order_menus_id = database_order_menus_id;
    }

    public String getDatabase_menu_status() {
        return database_menu_status;
    }

    public void setDatabase_menu_status(String database_menu_status) {
        this.database_menu_status = database_menu_status;
    }

    public CartModel() {
    }

    public CartModel(String database_unique_id, String database_order_menus_id, String database_user_id, String database_menu_id, String database_menu_name, String database_menu_price,
                     String database_menu_image, String database_menu_quantity, String database_menu_status) {
        this.database_unique_id = database_unique_id;
        this.database_order_menus_id = database_order_menus_id;
        this.database_user_id = database_user_id;
        this.database_menu_id = database_menu_id;
        this.database_menu_name = database_menu_name;
        this.database_menu_price = database_menu_price;
        this.database_menu_image = database_menu_image;
        this.database_menu_quantity = database_menu_quantity;
        this.database_menu_status = database_menu_status;
    }

    public String getDatabase_unique_id() {
        return database_unique_id;
    }

    public void setDatabase_unique_id(String database_unique_id) {
        this.database_unique_id = database_unique_id;
    }

    public String getDatabase_user_id() {
        return database_user_id;
    }

    public void setDatabase_user_id(String database_user_id) {
        this.database_user_id = database_user_id;
    }

    public String getDatabase_menu_id() {
        return database_menu_id;
    }

    public void setDatabase_menu_id(String database_menu_id) {
        this.database_menu_id = database_menu_id;
    }

    public String getDatabase_menu_name() {
        return database_menu_name;
    }

    public void setDatabase_menu_name(String database_menu_name) {
        this.database_menu_name = database_menu_name;
    }

    public String getDatabase_menu_price() {
        return database_menu_price;
    }

    public void setDatabase_menu_price(String database_menu_price) {
        this.database_menu_price = database_menu_price;
    }

    public String getDatabase_menu_image() {
        return database_menu_image;
    }

    public void setDatabase_menu_image(String database_menu_image) {
        this.database_menu_image = database_menu_image;
    }

    public String getDatabase_menu_quantity() {
        return database_menu_quantity;
    }

    public void setDatabase_menu_quantity(String database_menu_quantity) {
        this.database_menu_quantity = database_menu_quantity;
    }
}
