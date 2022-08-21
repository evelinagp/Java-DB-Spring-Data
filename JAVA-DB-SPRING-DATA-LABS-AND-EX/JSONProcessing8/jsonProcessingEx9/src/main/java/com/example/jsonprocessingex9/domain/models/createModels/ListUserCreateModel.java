package com.example.jsonprocessingex9.domain.models.createModels;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUserCreateModel {
    @XmlElement(name = "user")
    private List<UserCreateModel> users;

    public ListUserCreateModel() {
    }

    public List<UserCreateModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserCreateModel> users) {
        this.users = users;
    }
}