package com.nsquare.restaurant.model;

/**
 * Created by mobintia on 29/7/16.
 */
public class TablesItem {

    String id;
    String title;
    String type;
    String order_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public TablesItem(String id, String title, String type, String order_id) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.order_id = order_id;
    }
}
