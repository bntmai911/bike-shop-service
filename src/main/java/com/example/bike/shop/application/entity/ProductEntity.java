package com.example.bike.shop.application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    //    @ManyToMany(cascade = { CascadeType.ALL })
//    @JoinTable(
//            name = "order_product",
//            joinColumns = { @JoinColumn(name = "order_id") },
//            inverseJoinColumns = { @JoinColumn(name = "product_id") }
//    )
//    private List<OrderEntity> orderList;
}
