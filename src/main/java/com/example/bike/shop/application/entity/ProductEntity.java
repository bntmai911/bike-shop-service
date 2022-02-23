package com.example.bike.shop.application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product")
@Data
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "price")
    private Long price;

//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private OrderEntity order;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "customer_order_product",
            joinColumns = { @JoinColumn(name = "product_id") },
            inverseJoinColumns = { @JoinColumn(name = "customer_order_id") }
    )
    private List<OrderEntity> orderList;
}
