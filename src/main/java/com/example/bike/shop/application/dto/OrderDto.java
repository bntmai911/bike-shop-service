package com.example.bike.shop.application.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderDto {
    private String customerName;
    private String customerPhoneNumber;
    private String code;
    private Long total;
    private List<ProductDto> productList;
}
