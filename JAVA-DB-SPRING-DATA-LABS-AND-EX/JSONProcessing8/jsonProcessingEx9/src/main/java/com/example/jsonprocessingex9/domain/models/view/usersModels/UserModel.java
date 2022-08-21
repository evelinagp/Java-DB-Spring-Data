package com.example.jsonprocessingex9.domain.models.view.usersModels;

import com.example.jsonprocessingex9.domain.models.view.productsModels.BoughtProductModel;
import com.example.jsonprocessingex9.domain.models.view.productsModels.SellerProductModel;
import com.example.jsonprocessingex9.domain.models.view.productsModels.BoughtProductModel;
import com.example.jsonprocessingex9.domain.models.view.productsModels.SellerProductModel;

import java.util.HashSet;
import java.util.Set;

public class UserModel {
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer age;
    private Set<SellerProductModel> sellingProducts = new HashSet<>();
    private Set<BoughtProductModel> boughtProducts = new HashSet<>();

    public UserModel() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Set<SellerProductModel> getSellingProducts() {
        return sellingProducts;
    }

    public void setSellingProducts(Set<SellerProductModel> sellingProducts) {
        this.sellingProducts = sellingProducts;
    }

    public Set<BoughtProductModel> getBoughtProducts() {
        return boughtProducts;
    }

    public void setBoughtProducts(Set<BoughtProductModel> boughtProducts) {
        this.boughtProducts = boughtProducts;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
