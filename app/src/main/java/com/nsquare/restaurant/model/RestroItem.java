package com.nsquare.restaurant.model;

/**
 * Created by mobintia on 29/7/16.
 */
public class RestroItem {

    String id;
    String personName;
    String companyName;
    String day;
    String description;
    String profilePic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public RestroItem(String id, String personName, String companyName, String day, String description, String profilePic) {
        this.id = id;
        this.personName = personName;
        this.companyName = companyName;
        this.day = day;
        this.description = description;
        this.profilePic = profilePic;
    }
}
