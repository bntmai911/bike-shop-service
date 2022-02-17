package com.example.bike.shop.application.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductDto {
    private UUID id;
    private String name;
    private Long quantity;
    private Long price;
}
