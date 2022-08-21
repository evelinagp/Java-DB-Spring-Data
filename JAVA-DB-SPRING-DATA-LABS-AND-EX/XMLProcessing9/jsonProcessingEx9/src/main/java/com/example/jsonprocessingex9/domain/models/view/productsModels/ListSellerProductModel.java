package com.example.jsonprocessingex9.domain.models.view.productsModels;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListSellerProductModel {
    @XmlElement(name = "product")
    private List<SellerProductModel> products;

    public ListSellerProductModel() {
    }

    public List<SellerProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<SellerProductModel> products) {
        this.products = products;
    }
}
