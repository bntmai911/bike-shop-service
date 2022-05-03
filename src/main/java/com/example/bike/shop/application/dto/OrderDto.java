package com.example.bike.shop.application.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private String customerName;
    private String customerPhoneNumber;
    private String code;
    private Long total;
    private List<ProductDto> productList;
}
