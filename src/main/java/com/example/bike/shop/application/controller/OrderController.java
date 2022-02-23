package com.example.bike.shop.application.controller;

import com.bike.shop.application.openapi.model.Order;
import com.bike.shop.application.openapi.model.Product;
import com.bike.shop.openapi.api.OrderApi;
import com.example.bike.shop.application.dto.OrderDto;
import com.example.bike.shop.application.dto.ProductDto;
import com.example.bike.shop.application.service.OrderService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@RestController
public class OrderController implements OrderApi {
    private final ModelMapper modelMapper;
    private final OrderService orderService;

    @Autowired
    public OrderController(ModelMapper modelMapper, OrderService orderService) {
        this.modelMapper = modelMapper;
        this.orderService = orderService;
    }

    @Override
    public ResponseEntity<Void> addOrder(Order order) {
        OrderDto orderDto = this.mapOrderToDto(order);
        this.orderService.addOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> deleteOrderById(UUID orderId) {
        this.orderService.deleteOrderById(orderId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Order> getOrderById(UUID orderId) {
        OrderDto orderDto = this.orderService.getOrderById(orderId);
        Order order = this.mapDtoToOrder(orderDto);
        return ResponseEntity.ok(order);
    }

    @Override
    public ResponseEntity<List<Order>> getOrders() {
        List<OrderDto> orderDtos = this.orderService.getOrders();
        List<Order> orders = this.mapDtosToOrders(orderDtos);
        return ResponseEntity.ok(orders);
    }

    @Override
    public ResponseEntity<Order> updateOrderById(UUID orderId, Order order) {
        OrderDto orderDto = this.mapOrderToDto(order);
        OrderDto updatedOrderDto = this.orderService.updateOrderById(orderId, orderDto);
        Order updatedOrder = this.mapDtoToOrder(updatedOrderDto);
        return ResponseEntity.ok(updatedOrder);
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

    private Order mapDtoToOrder(OrderDto orderDto) {
        Order order = this.modelMapper.map(orderDto, Order.class);
        List<ProductDto> productDtoList = orderDto.getProductList();
        List<Product> productList = new java.util.ArrayList<>(Collections.emptyList());
        productDtoList.forEach(productDto -> {
            Product product = this.modelMapper.map(productDto, Product.class);
            productList.add(product);
        });
        order.setProductList(productList);
        return order;
    }

    private List<Order> mapDtosToOrders(List<OrderDto> orderDtos) {
        Type listType = new TypeToken<List<Order>>(){}.getType();
        return this.modelMapper.map(orderDtos, listType);
    }
}
