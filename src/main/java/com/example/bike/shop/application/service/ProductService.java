package com.example.bike.shop.application.service;

import com.example.bike.shop.application.dto.ProductDto;
import com.example.bike.shop.application.dto.ProductResponseDto;
import com.example.bike.shop.application.entity.ProductEntity;
import com.example.bike.shop.application.repository.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.bike.shop.application.constant.ProductConstant.*;

@Component
@Service
public class ProductService {

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final List<String> sortableField = SORTABLE_FIELD;

    @Autowired
    public ProductService(ModelMapper modelMapper, ProductRepository productRepository) {
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
    }

    public UUID addProduct(ProductDto productDto) {
        ProductEntity productEntity = this.modelMapper.map(productDto, ProductEntity.class);
        ProductEntity savedProductEntity = this.productRepository.save(productEntity);
        return savedProductEntity.getId();
    }

    public void deleteProductById(UUID productId) {
        this.productRepository.deleteById(productId);
    }

    public Optional<ProductDto> getProductById(UUID productId) {
        Optional<ProductEntity> productEntity = this.productRepository.findById(productId);
        if (productEntity.isPresent()) {
            ProductDto productDto = this.modelMapper.map(productEntity.get(), ProductDto.class);
            return Optional.of(productDto);
        } else {
            return Optional.empty();
        }
    }

    public List<ProductResponseDto> getProducts(String search, String sortBy, String sortType, Long page, Long limit) {
        PageRequest pageRequest = this.buildPageRequest(sortBy, sortType, page, limit);
        Page<ProductEntity> productEntities = this.productRepository.findByNameContainingIgnoreCase(search, pageRequest);
        return this.mapEntitiesToResponseDtos(productEntities.get().collect(Collectors.toList()));
    }

    public Optional<ProductDto> updateProductById(UUID productId, ProductDto productDto) {
        Optional<ProductEntity> productEntity = this.productRepository.findById(productId);
        if (productEntity.isPresent()) {
            ProductEntity updatedProductEntity = this.modelMapper.map(productDto, ProductEntity.class);
            updatedProductEntity.setId(productEntity.get().getId());
            updatedProductEntity.setOrderList(productEntity.get().getOrderList());
            this.productRepository.save(updatedProductEntity);
            return Optional.of(productDto);
        } else {
            return Optional.empty();
        }
    }

    private Sort buildSort(String sortBy, String sortType) {
        Sort.Direction direction = Sort.Direction.ASC;
        String sortField = DEFAULT_SORT_FIELD;
        if (StringUtils.equalsIgnoreCase(sortType, Sort.Direction.DESC.name())) {
            direction = Sort.Direction.DESC;
        }
        if (sortableField.contains(sortBy)) {
            sortField = sortBy;
        }
        return Sort.by(direction, sortField.toLowerCase());
    }

    private PageRequest buildPageRequest(String sortBy, String sortType, Long page, Long limit) {
        Sort sort = this.buildSort(sortBy, sortType);
        int pageNumber = Objects.isNull(page) ? DEFAULT_PAGE_NUMBER : page.intValue();
        int pageLimit = Objects.isNull(limit) ? DEFAULT_PAGE_LIMIT : limit.intValue();
        return PageRequest.of(pageNumber, pageLimit, sort);
    }

    private List<ProductResponseDto> mapEntitiesToResponseDtos(List<ProductEntity> productEntities) {
        Type listType = new TypeToken<List<ProductResponseDto>>(){}.getType();
        return this.modelMapper.map(productEntities, listType);
    }
}
