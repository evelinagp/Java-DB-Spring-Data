package com.example.jsonprocessingex9.domain.models.view.usersModels;

import com.example.jsonprocessingex9.domain.models.view.productsModels.BoughtProductModel;

import javax.xml.bind.annotation.*;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
public class UserWithSoldProductsModel {
    @XmlAttribute(name = "first-name")
    private String firstName;

    @XmlAttribute(name = "last-name")
    private String lastName;

    @XmlElementWrapper(name = "sold-products")
    @XmlElement(name = "product")
    private Set<BoughtProductModel> soldProducts;

    public UserWithSoldProductsModel() {
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

    public Set<BoughtProductModel> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(Set<BoughtProductModel> soldProducts) {
        this.soldProducts = soldProducts;
    }
}