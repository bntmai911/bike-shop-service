package com.example.bike.shop.application.service;

import com.example.bike.shop.application.dto.OrderDto;
import com.example.bike.shop.application.dto.ProductDto;
import com.example.bike.shop.application.entity.OrderEntity;
import com.example.bike.shop.application.entity.ProductEntity;
import com.example.bike.shop.application.repository.OrderRepository;
import com.example.bike.shop.application.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

public class OrderService {
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(ModelMapper modelMapper, OrderRepository orderRepository, ProductRepository productRepository) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public void addOrder(OrderDto orderDto) {
        OrderEntity orderEntity = this.mapOrderToEnity(orderDto);
        this.saveOrder(orderEntity);
    }

    private OrderEntity mapOrderToEnity(OrderDto orderDto) {
        OrderEntity orderEntity = this.modelMapper.map(orderDto, OrderEntity.class);
        List<ProductDto> productList = orderDto.getProductList();
        List<ProductEntity> productEntityList = new java.util.ArrayList<>(Collections.emptyList());
        productList.forEach(product -> {
            ProductEntity productEntity = this.modelMapper.map(product, ProductEntity.class);
            productEntityList.add(productEntity);
        });
//        orderEntity.setProductList(productEntityList);
        return orderEntity;
    }

    private void saveOrder(OrderEntity orderEntity) {
        OrderEntity savedOrderEntity = this.orderRepository.save(orderEntity);
//        List<ProductEntity> productEntityList = orderEntity.getProductList();
//        productEntityList.forEach(productEntity -> {
//            Optional<ProductEntity> optionalProductEntity = this.productRepository.findById(productEntity.getId());
//            if (optionalProductEntity.isEmpty()) {
//                throw new ApiException(HttpStatus.BAD_REQUEST, "Product is not existed");
//            }
//            if (optionalProductEntity.get().getQuantity() == 0) {
//                throw new ApiException(HttpStatus.BAD_REQUEST, "Product is out of stock");
//            }
//            List<OrderEntity> orderEntityList = optionalProductEntity.get().getOrderList();
//            orderEntityList.add(savedOrderEntity);
//            productEntity.setOrderList(orderEntityList);
//            this.productRepository.save(productEntity);
//        });
    }
}
