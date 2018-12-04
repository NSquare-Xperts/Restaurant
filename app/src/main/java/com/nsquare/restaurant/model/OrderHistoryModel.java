package com.nsquare.restaurant.model;

/**
 * Created by Pushkar on 07-09-2017.
 */

public class OrderHistoryModel {

    String id, orderId, amount, status, time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public OrderHistoryModel(String id, String orderId, String amount, String status, String time) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.time = time;
    }
}
