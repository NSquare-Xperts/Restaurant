package com.nsquare.restaurant.model;

/**
 * Created by Pushkar on 07-09-2017.
 */

public class VegMenuListModel {

    String id, menuName, menuPrice, image, menuDescription, database_menu_status;

    public String getDatabase_menu_status() {
        return database_menu_status;
    }

    public void setDatabase_menu_status(String database_menu_status) {
        this.database_menu_status = database_menu_status;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        this.menuPrice = menuPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public VegMenuListModel(String id, String menuName, String menuPrice, String image, String menuDescription, String database_menu_status) {
        this.id = id;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.image = image;
        this.menuDescription = menuDescription;
        this.database_menu_status = database_menu_status;
    }
}
