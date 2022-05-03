package com.example.bike.shop.application.controller;

import com.bike.shop.application.openapi.model.Product;
import com.bike.shop.application.openapi.model.ProductResponse;
import com.bike.shop.openapi.api.ProductApi;
import com.example.bike.shop.application.dto.ProductDto;
import com.example.bike.shop.application.dto.ProductResponseDto;
import com.example.bike.shop.application.service.ProductService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ProductController implements ProductApi {

    private final ModelMapper modelMapper;
    private final ProductService productService;

    @Autowired
    public ProductController(ModelMapper modelMapper, ProductService productService) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<UUID> addProduct(Product body) {
        ProductDto productDto = this.modelMapper.map(body, ProductDto.class);
        UUID productId = this.productService.addProduct(productDto);
        return ResponseEntity.ok(productId);
    }

    @Override
    public ResponseEntity<Void> deleteProductById(UUID productId) {
        this.productService.deleteProductById(productId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(UUID productId) {
        Optional<ProductDto> productDto = this.productService.getProductById(productId);
        if (productDto.isPresent()) {
            ProductResponse productResponse = this.modelMapper.map(productDto.get(), ProductResponse.class);
            return ResponseEntity.ok(productResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getProducts(String search, String sortBy, String sortType, Long page, Long limit) {
        List<ProductResponseDto> productResponseDtos = this.productService.getProducts(search, sortBy, sortType, page, limit);
        List<ProductResponse> productResponseList = this.mapResponseDtos(productResponseDtos);
        return ResponseEntity.ok(productResponseList);
   }

    @Override
    public ResponseEntity<Product> updateProductById(UUID productId, Product body) {
        ProductDto productDto = this.modelMapper.map(body, ProductDto.class);
        Optional<ProductDto> updatedProductDto = this.productService.updateProductById(productId, productDto);
        if (updatedProductDto.isPresent()) {
            return ResponseEntity.ok(body);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private List<ProductResponse> mapResponseDtos(List<ProductResponseDto> productDtos) {
        Type listType = new TypeToken<List<ProductResponse>>(){}.getType();
        return this.modelMapper.map(productDtos, listType);
    }
}
