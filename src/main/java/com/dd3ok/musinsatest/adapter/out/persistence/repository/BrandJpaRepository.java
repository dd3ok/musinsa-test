package com.dd3ok.musinsatest.adapter.out.persistence.repository;

import com.dd3ok.musinsatest.adapter.out.persistence.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandJpaRepository extends JpaRepository<BrandEntity, Long> {
    Optional<BrandEntity> findByName(String name);
}

