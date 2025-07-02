package com.dd3ok.musinsatest.application.port.in;

import com.dd3ok.musinsatest.application.port.in.command.ProductCreateCommand;
import com.dd3ok.musinsatest.application.port.in.command.ProductUpdateCommand;
import com.dd3ok.musinsatest.domain.product.Product;

import java.util.List;

public interface ProductCommandUseCase {
    Product createProduct(ProductCreateCommand command);
    Product updateProduct(ProductUpdateCommand command);
    void deleteProduct(Long productId);

    List<Product> getAllProducts();
}
