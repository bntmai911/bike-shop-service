package com.example.bike.shop.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private String name;
    private Long quantity;
    private Long price;
}
