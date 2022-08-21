package com.example.jsonprocessingex9.domain.models.view.usersModels.statsModels;

import com.example.jsonprocessingex9.domain.models.view.productsModels.SimpleProductModel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class SoldProducts {
    @XmlAttribute(name = "count")
    private int count;

    @XmlElement(name = "product")
    private List<SimpleProductModel> products;

    public SoldProducts() {
    }

    public List<SimpleProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<SimpleProductModel> products) {
        this.products = products;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
