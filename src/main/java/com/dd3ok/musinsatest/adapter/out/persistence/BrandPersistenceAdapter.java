package com.dd3ok.musinsatest.adapter.out.persistence;

import com.dd3ok.musinsatest.adapter.out.persistence.mapper.BrandMapper;
import com.dd3ok.musinsatest.adapter.out.persistence.repository.BrandJpaRepository;
import com.dd3ok.musinsatest.application.port.out.BrandRepository;
import com.dd3ok.musinsatest.domain.brand.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BrandPersistenceAdapter implements BrandRepository {
    private final BrandJpaRepository brandJpaRepository;
    private final BrandMapper brandMapper;

    @Override
    public Optional<Brand> findById(Long id) {
        return brandJpaRepository.findById(id).map(brandMapper::toDomain);
    }
}