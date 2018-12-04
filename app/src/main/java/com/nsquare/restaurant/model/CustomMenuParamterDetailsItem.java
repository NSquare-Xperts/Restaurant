package com.nsquare.restaurant.model;

/**
 * Created by Admin on 28-04-2017.
 */

public class CustomMenuParamterDetailsItem {
    String id;
    String name;
    String type;
    String selectedLabel_ids;
    String price;


    public CustomMenuParamterDetailsItem(String id, String name, String type,String selectedLabel,String price){

        this.id = id;
        this.name = name;
        this.type = type;
        this.selectedLabel_ids = selectedLabel;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSelectedLabel() {
        return selectedLabel_ids;
    }

    public void setSelectedLabel(String selectedLabel) {
        this.selectedLabel_ids = selectedLabel;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

