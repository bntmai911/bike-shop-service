package com.example.bike.shop.application.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customer_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone_number")
    private String customerPhoneNumber;

    @Column(name = "code")
    private String code;

    @Column(name = "total")
    private Long total;

    @ManyToMany(mappedBy = "orderList")
    private List<ProductEntity> productList;
}
