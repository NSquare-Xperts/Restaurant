package com.nsquare.restaurant.model;

/**
 * Created by Pushkar on 07-09-2017.
 */

public class UpcomingYourBookingModel {

    String id, restroName, address, image, dateTime, status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestroName() {
        return restroName;
    }

    public void setRestroName(String restroName) {
        this.restroName = restroName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UpcomingYourBookingModel(String id, String restroName, String address, String image, String dateTime, String status) {
        this.id = id;
        this.restroName = restroName;
        this.address = address;
        this.image = image;
        this.dateTime = dateTime;
        this.status = status;
    }
}
