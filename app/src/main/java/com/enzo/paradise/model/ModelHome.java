package com.enzo.paradise.model;

public class ModelHome {
    private String homeImage;
    private String homeName;

    public String getHomeImage() {
        return homeImage;
    }

    public void setHomeImage(String homeImage) {
        this.homeImage = homeImage;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public ModelHome(String homeImage, String homeName) {
        this.homeImage = homeImage;
        this.homeName = homeName;
    }
}
