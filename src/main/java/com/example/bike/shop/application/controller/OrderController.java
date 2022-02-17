package com.example.bike.shop.application.controller;

import com.bike.shop.application.openapi.model.Order;
import com.bike.shop.application.openapi.model.Product;
import com.bike.shop.openapi.api.OrderApi;
import com.example.bike.shop.application.dto.OrderDto;
import com.example.bike.shop.application.dto.ProductDto;
import com.example.bike.shop.application.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

public class OrderController implements OrderApi {
    private final ModelMapper modelMapper;
    private final OrderService orderService;

    @Autowired
    public OrderController(ModelMapper modelMapper, OrderService orderService) {
        this.modelMapper = modelMapper;
        this.orderService = orderService;
    }

    @Override
    public ResponseEntity<String> addOrder(Order order) {
        OrderDto orderDto = this.mapOrderToDto(order);
        this.orderService.addOrder(orderDto);
        return ResponseEntity.ok("Ok");
    }

    private OrderDto mapOrderToDto(Order order) {
        OrderDto orderDto = this.modelMapper.map(order, OrderDto.class);
        List<Product> productList = order.getProductList();
        List<ProductDto> productDtoList = new java.util.ArrayList<>(Collections.emptyList());
        productList.forEach(product -> {
            ProductDto productDto = this.modelMapper.map(product, ProductDto.class);
            productDtoList.add(productDto);
        });
        orderDto.setProductList(productDtoList);
        return orderDto;
    }
}
