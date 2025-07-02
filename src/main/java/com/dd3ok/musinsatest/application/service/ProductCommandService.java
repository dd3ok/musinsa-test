package com.dd3ok.musinsatest.application.service;

import com.dd3ok.musinsatest.application.port.in.ProductCommandUseCase;
import com.dd3ok.musinsatest.application.port.in.command.ProductCreateCommand;
import com.dd3ok.musinsatest.application.port.in.command.ProductUpdateCommand;
import com.dd3ok.musinsatest.application.port.out.BrandRepository;
import com.dd3ok.musinsatest.application.port.out.ProductRepository;
import com.dd3ok.musinsatest.domain.brand.Brand;
import com.dd3ok.musinsatest.domain.product.Category;
import com.dd3ok.musinsatest.domain.product.Price;
import com.dd3ok.musinsatest.domain.product.Product;
import com.dd3ok.musinsatest.global.exception.BaseException;
import com.dd3ok.musinsatest.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCommandService implements ProductCommandUseCase {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    @Override
    public Product createProduct(ProductCreateCommand command) {
        Brand brand = brandRepository.findById(command.brandId())
                .orElseThrow(() -> new BaseException(ErrorCode.BRAND_NOT_FOUND));

        Category category = Category.fromString(command.category());
        Price price = new Price(BigDecimal.valueOf(command.price()));

        Product newProduct = Product.create(price, category, brand);
        return productRepository.save(newProduct);
    }

    @Override
    public Product updateProduct(ProductUpdateCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));

        Brand newBrand = command.brandId() == null ? null : brandRepository.findById(command.brandId())
                .orElseThrow(() -> new BaseException(ErrorCode.BRAND_NOT_FOUND));

        Category newCategory = command.category() == null ? null : parseCategory(command.category());

        Price newPrice = command.price() == null ? null : new Price(BigDecimal.valueOf(command.price()));

        Product updatedProduct = product.update(newPrice, newCategory, newBrand);

        return productRepository.save(updatedProduct);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));
        productRepository.delete(product);
    }

    private Category parseCategory(String categoryName) {
        return Category.fromString(categoryName.toUpperCase());
    }

    // COMMENT: command지만 4번 API 조회 테스트를 위해 조회 추가합니다.
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}