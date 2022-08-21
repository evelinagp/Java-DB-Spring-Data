package com.example.jsonprocessingex9.domain.models.createModels;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListProductCreateModel {
    @XmlElement(name = "product")
    private List<ProductCreateModel> products;

    public ListProductCreateModel() {
    }

    public List<ProductCreateModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductCreateModel> products) {
        this.products = products;
    }
}