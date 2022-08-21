package com.example.jsonprocessingex9.domain.models.view.usersModels;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUserWithSoldProductsModel {
    @XmlElement(name = "user")
    private List<UserWithSoldProductsModel> sellers;

    public ListUserWithSoldProductsModel() {
    }

    public List<UserWithSoldProductsModel> getSellers() {
        return sellers;
    }

    public void setSellers(List<UserWithSoldProductsModel> sellers) {
        this.sellers = sellers;
    }
}
