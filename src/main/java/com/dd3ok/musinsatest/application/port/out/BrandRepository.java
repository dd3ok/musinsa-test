package com.dd3ok.musinsatest.application.port.out;

import com.dd3ok.musinsatest.domain.brand.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    Brand save(Brand brand);
    Optional<Brand> findById(Long id);
    Optional<Brand> findByName(String name);
    void delete(Brand brand);

    List<Brand> findAll();
}
