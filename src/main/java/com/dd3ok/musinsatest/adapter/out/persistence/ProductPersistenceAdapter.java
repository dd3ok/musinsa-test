package com.dd3ok.musinsatest.adapter.out.persistence;

import com.dd3ok.musinsatest.adapter.out.persistence.mapper.ProductMapper;
import com.dd3ok.musinsatest.adapter.out.persistence.repository.ProductJpaRepository;
import com.dd3ok.musinsatest.application.port.in.dto.BrandTotalPriceDto;
import com.dd3ok.musinsatest.application.port.out.ProductRepository;
import com.dd3ok.musinsatest.domain.product.Category;
import com.dd3ok.musinsatest.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<BrandTotalPriceDto> findBrandWithLowestTotalPrice() {
        return productJpaRepository.findBrandWithLowestTotalPrice();
    }

    @Override
    public List<Product> findLowestAndHighestProductsByCategory(Category category) {
        return List.of();
    }

    @Override
    public List<Product> findAllByBrandId(Long brandId) {
        return productJpaRepository.findAllByBrandId(brandId).stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }
}