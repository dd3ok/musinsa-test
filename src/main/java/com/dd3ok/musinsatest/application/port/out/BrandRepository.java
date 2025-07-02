package com.dd3ok.musinsatest.application.port.out;

import com.dd3ok.musinsatest.domain.brand.Brand;

import java.util.Optional;

public interface BrandRepository {
    Optional<Brand> findById(Long id);
}
