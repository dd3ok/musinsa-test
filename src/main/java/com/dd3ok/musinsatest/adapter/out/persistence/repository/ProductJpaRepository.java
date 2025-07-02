package com.dd3ok.musinsatest.adapter.out.persistence.repository;

import com.dd3ok.musinsatest.adapter.out.persistence.entity.ProductEntity;
import com.dd3ok.musinsatest.application.port.in.dto.BrandTotalPriceDto;
import com.dd3ok.musinsatest.domain.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    // API 1 카테고리별 최저가 조회
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

    // API 2 브랜드별 총액 계산하고 최저가 브랜드 찾음
    @Query("SELECT new com.dd3ok.musinsatest.application.port.in.dto.BrandTotalPriceDto(p.brand.id, SUM(p.price)) " +
            "FROM ProductEntity p " +
            "GROUP BY p.brand.id " +
            "ORDER BY SUM(p.price) ASC")
    List<BrandTotalPriceDto> findBrandWithLowestTotalPrice();
    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.brand WHERE p.brand.id = :brandId")
    List<ProductEntity> findAllByBrandId(Long brandId);

    // API 3 카테고리 최저가 최고가 브랜드, 가격 조회
    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.brand " +
            "WHERE p.category = :category AND p.price = " +
            "(SELECT MIN(p2.price) FROM ProductEntity p2 WHERE p2.category = :category)")
    List<ProductEntity> findLowestPriceProductsByCategory(@Param("category") Category category);
    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.brand " +
            "WHERE p.category = :category AND p.price = " +
            "(SELECT MAX(p2.price) FROM ProductEntity p2 WHERE p2.category = :category)")
    List<ProductEntity> findHighestPriceProductsByCategory(@Param("category") Category category);
}
