package com.dd3ok.musinsatest.adapter.out.persistence;

import com.dd3ok.musinsatest.adapter.out.persistence.entity.BrandEntity;
import com.dd3ok.musinsatest.adapter.out.persistence.entity.ProductEntity;
import com.dd3ok.musinsatest.adapter.out.persistence.mapper.ProductMapper;
import com.dd3ok.musinsatest.adapter.out.persistence.repository.BrandJpaRepository;
import com.dd3ok.musinsatest.adapter.out.persistence.repository.ProductJpaRepository;
import com.dd3ok.musinsatest.application.port.in.dto.BrandCategoryPriceDto;
import com.dd3ok.musinsatest.application.port.out.ProductRepository;
import com.dd3ok.musinsatest.common.exception.BaseException;
import com.dd3ok.musinsatest.common.exception.ErrorCode;
import com.dd3ok.musinsatest.domain.product.Category;
import com.dd3ok.musinsatest.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final BrandJpaRepository brandJpaRepository;
    private final ProductMapper productMapper;

    @Override
    public List<Product> findLowestPriceProductsGroupByCategory() {
        return productJpaRepository.findLowestPriceProductsGroupByCategory().stream()
                .map(productMapper::toDomain)
                .toList();
    }

    @Override
    public List<BrandCategoryPriceDto> findLowestPriceByCategoryInBrand() {
        return productJpaRepository.findLowestPriceByCategoryInBrand();
    }

    @Override
    public List<Product> findLowestPriceProductsByCategory(Category category) {
        return productJpaRepository.findLowestPriceProductsByCategory(category).stream()
                .map(productMapper::toDomain)
                .toList();
    }

    @Override
    public List<Product> findHighestPriceProductsByCategory(Category category) {
        return productJpaRepository.findHighestPriceProductsByCategory(category).stream()
                .map(productMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id).map(productMapper::toDomain);
    }

    @Override
    public Product save(Product product) {
        ProductEntity productEntity = convertToEntity(product);
        ProductEntity savedEntity = productJpaRepository.save(productEntity);
        return productMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Product product) {
        productJpaRepository.deleteById(product.getId());
    }

    private ProductEntity convertToEntity(Product product) {
        BrandEntity brandEntity = brandJpaRepository.findById(product.getBrand().getId())
                .orElseThrow(() -> new BaseException(ErrorCode.BRAND_NOT_FOUND));

        if (product.getId() == null) {
            return ProductEntity.from(product, brandEntity);
        } else {
            ProductEntity entity = productJpaRepository.findById(product.getId())
                    .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));
            entity.update(product.getPrice().value(), product.getCategory(), brandEntity);
            return entity;
        }
    }

    @Override
    public boolean existsByBrandId(Long brandId) {
        return productJpaRepository.existsByBrandId(brandId);
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll().stream().map(productMapper::toDomain).toList();
    }
}