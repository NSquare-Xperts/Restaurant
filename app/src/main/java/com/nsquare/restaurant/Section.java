package com.nsquare.restaurant;

import java.util.List;

public class Section {
    private String iconURL;
    private String primaryColor;
    private List<String> productionIds;
    private String secondaryColor;
    private String title;
    private int image;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPrimaryColor() {
        return this.primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return this.secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getIconURL() {
        return this.iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getProductionIds() {
        return this.productionIds;
    }

    public void setProductionIds(List<String> productionIds) {
        this.productionIds = productionIds;
    }
}
