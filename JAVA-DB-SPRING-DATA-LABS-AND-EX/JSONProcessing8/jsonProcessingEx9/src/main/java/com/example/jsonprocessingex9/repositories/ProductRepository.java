package com.example.jsonprocessingex9.repositories;

import com.example.jsonprocessingex9.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByBuyerIsNullAndPriceBetween(BigDecimal beginning, BigDecimal end);

    List<Product> findAllByBuyerIsNotNull();
}