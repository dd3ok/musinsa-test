package com.dd3ok.musinsatest.application.port.out;

import com.dd3ok.musinsatest.application.port.in.dto.BrandCategoryPriceDto;
import com.dd3ok.musinsatest.domain.product.Category;
import com.dd3ok.musinsatest.domain.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    // API 1
    List<Product> findLowestPriceProductsGroupByCategory();
    // API 2
    List<BrandCategoryPriceDto> findLowestPriceByCategoryInBrand();
    // API 3
    List<Product> findLowestPriceProductsByCategory(Category category);
    List<Product> findHighestPriceProductsByCategory(Category category);

    // API 4
    boolean existsByBrandId(Long brandId);
    Product save(Product product);
    void delete(Product product);
    Optional<Product> findById(Long id);

    List<Product> findAll();
}
