package com.dd3ok.musinsatest.adapter.out.persistence.mapper;

import com.dd3ok.musinsatest.adapter.out.persistence.entity.BrandEntity;
import com.dd3ok.musinsatest.domain.brand.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public Brand toDomain(BrandEntity entity) {
        return entity.toDomain();
    }

    public BrandEntity toEntity(Brand domain) {
        return BrandEntity.from(domain);
    }
}
