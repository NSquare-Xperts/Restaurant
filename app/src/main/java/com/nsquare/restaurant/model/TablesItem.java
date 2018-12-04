package com.nsquare.restaurant.model;

/**
 * Created by mobintia on 29/7/16.
 */
public class TablesItem {

    String id;
    String tableName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public TablesItem(String id, String tableName) {
        this.id = id;
        this.tableName = tableName;
    }
}
