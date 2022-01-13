package com.ftg2021.effo2021V1.Authentication.DataModels;

public class UserDataModel {

    int userId;
    String name;
    String phone;
    String email,address;
    String firebaseNotificationToken;

    public UserDataModel(int userId, String name, String phone, String email, String address, String firebaseNotificationToken) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.firebaseNotificationToken = firebaseNotificationToken;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirebaseNotificationToken() {
        return firebaseNotificationToken;
    }

    public void setFirebaseNotificationToken(String firebaseNotificationToken) {
        this.firebaseNotificationToken = firebaseNotificationToken;
    }
}
