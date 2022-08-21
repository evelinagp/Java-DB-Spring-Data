package com.example.jsonprocessingex9.services.validators;

import com.example.jsonprocessingex9.domain.models.createModels.ProductCreateModel;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {
    public ProductValidator() {
    }

    public boolean isValidProduct(ProductCreateModel product) {
        return this.isValidName(product.getName());
    }

    private boolean isValidName(String name) {
        return name != null && name.length() >= 3;
    }

}