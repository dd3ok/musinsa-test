package com.dd3ok.musinsatest.application.port.out;

import com.dd3ok.musinsatest.domain.product.Category;
import com.dd3ok.musinsatest.domain.product.Product;

import java.util.List;

public interface ProductRepository {
    // API 1
    List<Product> findLowestPriceProductsGroupByCategory();
    // API 2
    List<Product> findLowestPriceProductsByBrandNameGroupByCategory();
    // API 3
    List<Product> findLowestAndHighestProductsByCategory(Category category);
}
