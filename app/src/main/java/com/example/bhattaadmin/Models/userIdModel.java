package com.example.bhattaadmin.Models;

public class userIdModel {
    String imageUrl ;
    String name;
    String phone;
    String userId;

    public userIdModel() {
    }

    public userIdModel(String imageUrl, String name, String phone, String userId) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.phone = phone;
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
