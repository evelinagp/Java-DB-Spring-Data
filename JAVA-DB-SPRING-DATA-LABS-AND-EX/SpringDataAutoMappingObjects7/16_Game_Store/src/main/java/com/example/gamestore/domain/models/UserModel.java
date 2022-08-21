package com.example.gamestore.domain.models;

import com.example.gamestore.domain.entities.enums.UserType;

public class UserModel {
    private Integer id;
    private String fullName;
    private String email;
    private UserType userType;

    public UserModel(String fullName, String email, UserType userType) {
        this.fullName = fullName;
        this.email = email;
        this.userType = userType;
    }

    public UserModel() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
