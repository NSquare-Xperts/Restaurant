package com.nsquare.restaurant.model;

/**
 * Created by Admin on 25-04-2017.
 */

public class CustomeOrderItems {
    String id;
    String label;

    public CustomeOrderItems(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return label;
    }

    public void setRoleName(String roleName) {
        this.label = roleName;
    }
}
