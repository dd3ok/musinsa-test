package com.dd3ok.musinsatest.adapter.out.persistence.mapper;

import com.dd3ok.musinsatest.adapter.out.persistence.entity.BrandEntity;
import com.dd3ok.musinsatest.adapter.out.persistence.entity.ProductEntity;
import com.dd3ok.musinsatest.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final BrandMapper brandMapper;

    public Product toDomain(ProductEntity entity) {
        return entity.toDomain();
    }

    public ProductEntity toEntity(Product domain, BrandEntity brandEntity) {
        return ProductEntity.from(domain, brandEntity);
    }
}