package com.dd3ok.musinsatest.adapter.out.persistence;

import com.dd3ok.musinsatest.adapter.out.persistence.entity.BrandEntity;
import com.dd3ok.musinsatest.adapter.out.persistence.mapper.BrandMapper;
import com.dd3ok.musinsatest.adapter.out.persistence.repository.BrandJpaRepository;
import com.dd3ok.musinsatest.application.port.out.BrandRepository;
import com.dd3ok.musinsatest.domain.brand.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Override
    public Optional<Brand> findByName(String name) {
        return brandJpaRepository.findByName(name).map(brandMapper::toDomain);
    }

    @Override
    public Brand save(Brand brand) {
        BrandEntity entity = brandMapper.toEntity(brand);
        BrandEntity savedEntity = brandJpaRepository.save(entity);
        return brandMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Brand brand) {
        brandJpaRepository.deleteById(brand.getId());
    }

    @Override
    public List<Brand> findAll() {
        return brandJpaRepository.findAll().stream()
                .map(brandMapper::toDomain)
                .toList();
    }
}