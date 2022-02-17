package com.example.bike.shop.application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "customer_order")
@Data
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

    //    @ManyToMany(mappedBy = "orderList")
//    private List<ProductEntity> productList;
}
