package com.example.bike.shop.application.service;

import com.example.bike.shop.application.constant.ErrorMessage;
import com.example.bike.shop.application.dto.OrderDto;
import com.example.bike.shop.application.dto.ProductDto;
import com.example.bike.shop.application.entity.OrderEntity;
import com.example.bike.shop.application.entity.ProductEntity;
import com.example.bike.shop.application.exception.ApiException;
import com.example.bike.shop.application.model.ValidatingProducts;
import com.example.bike.shop.application.repository.OrderRepository;
import com.example.bike.shop.application.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.bike.shop.application.constant.ErrorMessage.CREATE_ORDER_PRODUCT_LIST_NOT_EXIST;
import static com.example.bike.shop.application.constant.ErrorMessage.CREATE_ORDER_PRODUCT_OUT_OF_STOCK;

@Component
@Service
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
        OrderEntity orderEntity = this.mapDtoToEntity(orderDto);
        this.saveOrderToRepository(orderEntity);
    }

    private void saveOrderToRepository(OrderEntity orderEntity) {
        this.isOrderValid(orderEntity.getProductList());
        this.orderRepository.save(orderEntity);
    }

    private void isOrderValid(List<ProductEntity> productEntities) {
        if (productEntities.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorMessage.CREATE_ORDER_PRODUCT_LIST_EMPTY);
        } else {
            ValidatingProducts validatingProducts = this.checkIfProductsValid(productEntities);
            if (!validatingProducts.isValid()) {
                String errorMessage = "";
                if (!validatingProducts.getNonExistedProducts().isEmpty()) {
                    errorMessage += MessageFormat.format(CREATE_ORDER_PRODUCT_LIST_NOT_EXIST, validatingProducts.getNonExistedProducts());
                }
                if (!validatingProducts.getOutOfStockProducts().isEmpty()) {
                    errorMessage += MessageFormat.format(CREATE_ORDER_PRODUCT_OUT_OF_STOCK, validatingProducts.getOutOfStockProducts());
                }
                throw new ApiException(HttpStatus.BAD_REQUEST, errorMessage);
            }
        }
    }

    private ValidatingProducts checkIfProductsValid(List<ProductEntity> productEntities) {
        ValidatingProducts validatingProducts = new ValidatingProducts();
        validatingProducts.setValid(true);
        List<ProductEntity> nonExistedProducts = new java.util.ArrayList<>(Collections.emptyList());
        List<ProductEntity> outOfStockProducts = new java.util.ArrayList<>(Collections.emptyList());
        productEntities.forEach(productEntity -> {
            Optional<ProductEntity> optionalProductEntity = this.productRepository.findById(productEntity.getId());
            if (optionalProductEntity.isEmpty()) {
                nonExistedProducts.add(productEntity);
            } else {
                if (optionalProductEntity.get().getQuantity() == 0) {
                    outOfStockProducts.add(productEntity);
                }
            }
        });
        if (!nonExistedProducts.isEmpty() || !outOfStockProducts.isEmpty()) {
            validatingProducts.setValid(false);
        }
        return validatingProducts;
    }

    public void deleteOrderById(UUID orderId) {
        this.orderRepository.deleteById(orderId);
    }

    public OrderDto getOrderById(UUID orderId) {
        Optional<OrderEntity> orderEntity = this.orderRepository.findById(orderId);
        return orderEntity.map(entity -> this.modelMapper.map(entity, OrderDto.class)).orElse(null);
    }

    public List<OrderDto> getOrders() {
        List<OrderEntity> orderEntities = this.orderRepository.findAll();
        return this.mapEntitiesToDtos(orderEntities);
    }

    public OrderDto updateOrderById(UUID orderId, OrderDto orderDto) {
        Optional<OrderEntity> orderEntity = this.orderRepository.findById(orderId);
        if (orderEntity.isPresent()) {
            OrderEntity updatedOrderEntity = this.mapDtoToEntity(orderDto);
            updatedOrderEntity.setId(orderEntity.get().getId());
            this.orderRepository.save(updatedOrderEntity);
            return orderDto;
        }
        return null;
    }

    private OrderDto mapEntityToDto(OrderEntity orderEntity) {
        OrderDto orderDto = this.modelMapper.map(orderEntity, OrderDto.class);
        List<ProductEntity> productEntities = orderEntity.getProductList();
        List<ProductDto> productDtos = new java.util.ArrayList<>(Collections.emptyList());
        productEntities.forEach(productEntity -> {
            ProductDto productDto = this.modelMapper.map(productEntity, ProductDto.class);
            productDtos.add(productDto);
        });
        orderDto.setProductList(productDtos);
        return orderDto;
    }

    private OrderEntity mapDtoToEntity(OrderDto orderDto) {
        OrderEntity orderEntity = this.modelMapper.map(orderDto, OrderEntity.class);
        List<ProductDto> productDtos = orderDto.getProductList();
        List<ProductEntity> productEntities = new java.util.ArrayList<>(Collections.emptyList());
        productDtos.forEach(productDto -> {
            ProductEntity productEntity = this.modelMapper.map(productDto, ProductEntity.class);
            productEntities.add(productEntity);
        });
        orderEntity.setProductList(productEntities);
        return orderEntity;
    }

    private List<OrderDto> mapEntitiesToDtos(List<OrderEntity> orderEntities) {
        Type listType = new TypeToken<List<OrderDto>>(){}.getType();
        return this.modelMapper.map(orderEntities, listType);
    }
}
