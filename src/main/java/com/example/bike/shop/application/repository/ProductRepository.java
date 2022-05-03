package com.example.bike.shop.application.repository;

import com.example.bike.shop.application.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, UUID> {
    Page<ProductEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
