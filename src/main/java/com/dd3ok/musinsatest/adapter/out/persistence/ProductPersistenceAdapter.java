package com.dd3ok.musinsatest.adapter.out.persistence;

import com.dd3ok.musinsatest.adapter.out.persistence.mapper.ProductMapper;
import com.dd3ok.musinsatest.adapter.out.persistence.repository.ProductJpaRepository;
import com.dd3ok.musinsatest.application.port.out.ProductRepository;
import com.dd3ok.musinsatest.domain.product.Category;
import com.dd3ok.musinsatest.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final ProductMapper productMapper;

    @Override
    public List<Product> findLowestPriceProductsGroupByCategory() {
        return productJpaRepository.findLowestPriceProductsGroupByCategory().stream()
                .map(productMapper::toDomain)
                .toList();
    }

    @Override
    public List<Product> findLowestPriceProductsByBrandNameGroupByCategory() {
        return List.of();
    }

    @Override
    public List<Product> findLowestAndHighestProductsByCategory(Category category) {
        return List.of();
    }
}