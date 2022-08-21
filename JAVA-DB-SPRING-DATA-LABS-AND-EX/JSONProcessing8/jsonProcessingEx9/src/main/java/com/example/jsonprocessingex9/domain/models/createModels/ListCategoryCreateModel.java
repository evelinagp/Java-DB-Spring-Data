package com.example.jsonprocessingex9.domain.models.createModels;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListCategoryCreateModel {
    @XmlElement(name = "category")
    private List<CategoryCreateModel> categories;

    public ListCategoryCreateModel() {
    }

    public List<CategoryCreateModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryCreateModel> categories) {
        this.categories = categories;
    }
}