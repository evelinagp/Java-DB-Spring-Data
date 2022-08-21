package com.example.jsonprocessingex9.services;

import com.example.jsonprocessingex9.domain.entities.Product;
import com.example.jsonprocessingex9.domain.models.view.productsModels.BoughtProductModel;
import com.example.jsonprocessingex9.domain.models.view.productsModels.SellerProductModel;
import com.example.jsonprocessingex9.domain.models.createModels.ProductCreateModel;
import com.example.jsonprocessingex9.repositories.ProductRepository;
import com.example.jsonprocessingex9.services.validators.ProductValidator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private ModelMapper modelMapper;
    private ProductValidator productValidator;

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper, ProductValidator productValidator) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.productValidator = productValidator;
    }

    public void saveToDb(ProductCreateModel productCreateModel) {
        if (!this.productValidator.isValidProduct(productCreateModel)) {
            return;
        }

        Product product = this.modelMapper.map(productCreateModel, Product.class);

        this.productRepository.save(product);
    }

    public void saveAllToDb(List<ProductCreateModel> products) {
        products.stream()
                .filter(this.productValidator::isValidProduct)
                .map(product -> this.modelMapper.map(product, Product.class))
                .forEach(this.productRepository::save);
    }

    public List<SellerProductModel> getAllProductsWithoutBuyerInPriceRange(BigDecimal rangeBeginning, BigDecimal rangeEnd) {
        List<Product> products = this.productRepository.findAllByBuyerIsNullAndPriceBetween(rangeBeginning, rangeEnd);

        return products.stream()
                .map(product -> this.modelMapper.map(product, SellerProductModel.class))
                .collect(Collectors.toList());
    }

    public List<BoughtProductModel> getAllProductsWithBuyers() {
        List<Product> products = this.productRepository.findAllByBuyerIsNotNull();

        return products.stream()
                .map(product -> this.modelMapper.map(product, BoughtProductModel.class))
                .collect(Collectors.toList());
    }
}
