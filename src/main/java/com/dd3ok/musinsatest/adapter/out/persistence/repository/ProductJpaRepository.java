package com.dd3ok.musinsatest.adapter.out.persistence.repository;

import com.dd3ok.musinsatest.adapter.out.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    @Query("""
    SELECT p FROM ProductEntity p
    JOIN FETCH p.brand
    WHERE p.price = (
        SELECT MIN(p2.price)
        FROM ProductEntity p2
        WHERE p2.category = p.category
    )
    """)
    List<ProductEntity> findLowestPriceProductsGroupByCategory();
}
