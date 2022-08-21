package com.example.jsonprocessingex9.domain.models.view.categoryModels;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListCategoryStatsModel {
    @XmlElement(name = "category")
    private List<CategoryStatsModel> categories;

    public ListCategoryStatsModel() {
    }

    public List<CategoryStatsModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryStatsModel> categories) {
        this.categories = categories;
    }
}
