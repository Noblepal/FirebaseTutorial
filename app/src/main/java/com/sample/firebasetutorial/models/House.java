package com.sample.firebasetutorial.models;

import java.io.Serializable;

public class House implements Serializable {
    private String name, id, estate, landlord, image;

    public House() {
    }

    public House(String name, String id, String estate, String landlord, String image) {
        this.name = name;
        this.id = id;
        this.estate = estate;
        this.landlord = landlord;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstate() {
        return estate;
    }

    public void setEstate(String estate) {
        this.estate = estate;
    }

    public String getLandlord() {
        return landlord;
    }

    public void setLandlord(String landlord) {
        this.landlord = landlord;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
