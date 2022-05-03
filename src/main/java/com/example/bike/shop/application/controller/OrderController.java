package com.example.bike.shop.application.controller;

import com.bike.shop.application.openapi.model.Order;
import com.bike.shop.application.openapi.model.OrderResponse;
import com.bike.shop.application.openapi.model.ProductResponse;
import com.bike.shop.openapi.api.OrderApi;
import com.example.bike.shop.application.dto.OrderDto;
import com.example.bike.shop.application.dto.ProductDto;
import com.example.bike.shop.application.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<OrderResponse> getOrderById(UUID orderId) {
        OrderDto orderDto = this.orderService.getOrderById(orderId);
        OrderResponse orderResponse = this.mapDtoToOrderResponse(orderDto);
        return ResponseEntity.ok(orderResponse);
    }

    @Override
    public ResponseEntity<List<OrderResponse>> getOrders() {
        List<OrderDto> orderDtos = this.orderService.getOrders();
        List<OrderResponse> orderResponseList = this.mapDtosToOrderResponses(orderDtos);
        return ResponseEntity.ok(orderResponseList);
    }

    @Override
    public ResponseEntity<OrderResponse> updateOrderById(UUID orderId, Order order) {
        OrderDto orderDto = this.mapOrderToDto(order);
        OrderDto updatedOrderDto = this.orderService.updateOrderById(orderId, orderDto);
        OrderResponse updatedOrderResponse = this.mapDtoToOrderResponse(updatedOrderDto);
        return ResponseEntity.ok(updatedOrderResponse);
    }

    private OrderDto mapOrderToDto(Order order) {
        OrderDto orderDto = this.modelMapper.map(order, OrderDto.class);
        List<ProductResponse> productList = order.getProductList();
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
        List<ProductResponse> productResponseList = new java.util.ArrayList<>(Collections.emptyList());
        productDtoList.forEach(productDto -> {
            ProductResponse product = this.modelMapper.map(productDto, ProductResponse.class);
            productResponseList.add(product);
        });
        order.setProductList(productResponseList);
        return order;
    }

    private OrderResponse mapDtoToOrderResponse(OrderDto orderDto) {
        OrderResponse orderResponse = this.modelMapper.map(orderDto, OrderResponse.class);
        List<ProductDto> productDtoList = orderDto.getProductList();
        List<ProductResponse> productResponseList = new java.util.ArrayList<>(Collections.emptyList());
        productDtoList.forEach(productDto -> {
            ProductResponse productResponse = this.modelMapper.map(productDto, ProductResponse.class);
            productResponseList.add(productResponse);
        });
        orderResponse.setProductList(productResponseList);
        return orderResponse;
    }

    private List<OrderResponse> mapDtosToOrderResponses(List<OrderDto> orderDtos) {
        List<OrderResponse> orderResponseList = new java.util.ArrayList<>(Collections.emptyList());
        orderDtos.forEach(orderDto -> {
            OrderResponse orderResponse = this.mapDtoToOrderResponse(orderDto);
            orderResponseList.add(orderResponse);
        });
        return orderResponseList;
    }
}
