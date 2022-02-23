package com.example.bike.shop.application.model;

import com.example.bike.shop.application.entity.ProductEntity;
import lombok.Data;

import java.util.List;

@Data
public class ValidatingProducts {
    private boolean isValid;
    private List<ProductEntity> nonExistedProducts;
    private List<ProductEntity> outOfStockProducts;
}
