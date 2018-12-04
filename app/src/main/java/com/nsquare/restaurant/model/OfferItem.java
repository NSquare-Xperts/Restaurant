package com.nsquare.restaurant.model;

/**
 * Created by mobintia on 29/7/16.
 */
public class OfferItem {

    String id;
    String offerImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOfferImage() {
        return offerImage;
    }

    public void setOfferImage(String offerImage) {
        this.offerImage = offerImage;
    }

    public OfferItem(String id, String offerImage) {
        this.id = id;
        this.offerImage = offerImage;
    }
}
