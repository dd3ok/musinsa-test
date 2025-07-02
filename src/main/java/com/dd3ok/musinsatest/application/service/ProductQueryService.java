package com.dd3ok.musinsatest.application.service;

import com.dd3ok.musinsatest.application.port.in.BrandLowestPriceSetUseCase;
import com.dd3ok.musinsatest.application.port.in.CategoryLowHighPriceUseCase;
import com.dd3ok.musinsatest.application.port.in.LowestPriceProductsUseCase;
import com.dd3ok.musinsatest.application.port.in.dto.BrandLowestPriceSetResult;
import com.dd3ok.musinsatest.application.port.in.dto.BrandPriceDto;
import com.dd3ok.musinsatest.application.port.in.dto.BrandTotalPriceDto;
import com.dd3ok.musinsatest.application.port.in.dto.CategoryLowestPriceProductDto;
import com.dd3ok.musinsatest.application.port.in.dto.CategoryPriceDto;
import com.dd3ok.musinsatest.application.port.in.dto.CategoryPriceRangeResult;
import com.dd3ok.musinsatest.application.port.in.dto.LowestPriceProductsResult;
import com.dd3ok.musinsatest.application.port.out.BrandRepository;
import com.dd3ok.musinsatest.application.port.out.ProductRepository;
import com.dd3ok.musinsatest.domain.brand.Brand;
import com.dd3ok.musinsatest.domain.product.Category;
import com.dd3ok.musinsatest.domain.product.Product;
import com.dd3ok.musinsatest.global.exception.BaseException;
import com.dd3ok.musinsatest.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryService implements LowestPriceProductsUseCase, BrandLowestPriceSetUseCase,
        CategoryLowHighPriceUseCase {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;


    @Override
    public LowestPriceProductsResult getLowestPriceProductsByCategory() {
        // 1. 최저가 조회
        List<Product> products = productRepository.findLowestPriceProductsGroupByCategory();

        // COMMENT: 같은 가격일 경우 브랜드 이름이 빠른순 선택 했습니다.
        // 2. 카테고리별로 하나의 상품만 선택
        Map<Category, Product> lowestPriceProductMap = products.stream()
                .collect(Collectors.toMap(
                        Product::category,
                        p -> p,
                        (p1, p2) -> p1.brand().name().compareTo(p2.brand().name()) < 0 ? p1 : p2
                ));

        List<CategoryLowestPriceProductDto> productDtos = lowestPriceProductMap.values().stream()
                .map(product -> new CategoryLowestPriceProductDto(
                        product.category(),
                        product.brand().name(),
                        product.price().value()
                ))
                .toList();

        // 3. 총액 계산
        BigDecimal totalPrice = productDtos.stream()
                .map(CategoryLowestPriceProductDto::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new LowestPriceProductsResult(productDtos, totalPrice);
    }

    @Override
    public BrandLowestPriceSetResult getLowestPriceProductsBrandSet() {
        // 1. 브랜드별 최저가 조회
        List<BrandTotalPriceDto> candidates = productRepository.findBrandWithLowestTotalPrice();

        // 2. 최저가 브랜드가 여럿일 경우 빠른 브랜드 선택
        BigDecimal lowestPrice = candidates.getFirst().totalPrice();
        BrandTotalPriceDto winner = candidates.stream()
                .filter(c -> c.totalPrice().compareTo(lowestPrice) == 0)
                .min(Comparator.comparing(BrandTotalPriceDto::brandId))
                .orElseThrow(() -> new BaseException(ErrorCode.BRAND_NOT_FOUND));

        Brand brand = brandRepository.findById(winner.brandId())
                .orElseThrow(() -> new BaseException(ErrorCode.BRAND_NOT_FOUND));

        // 4. 최저가 브랜드 상품 조회
        List<Product> products = productRepository.findAllByBrandId(winner.brandId());

        List<CategoryPriceDto> categoryPriceDtos = products.stream()
                .map(p -> new CategoryPriceDto(p.category(), p.price().value()))
                .toList();

        return new BrandLowestPriceSetResult(
                brand.name(),
                categoryPriceDtos,
                winner.totalPrice()
        );
    }

    @Override
    public CategoryPriceRangeResult getCategoryLowAndHighPrice(String categoryName) {
        Category category = Category.fromString(categoryName.toUpperCase());

        List<Product> lowestProducts = productRepository.findLowestPriceProductsByCategory(category);
        List<Product> highestProducts = productRepository.findHighestPriceProductsByCategory(category);

        List<BrandPriceDto> lowestPriceDtos = lowestProducts.stream()
                .map(p -> new BrandPriceDto(p.brand().name(), p.price().value()))
                .toList();

        List<BrandPriceDto> highestPriceDtos = highestProducts.stream()
                .map(p -> new BrandPriceDto(p.brand().name(), p.price().value()))
                .toList();

        return new CategoryPriceRangeResult(category.getDescription(), lowestPriceDtos, highestPriceDtos);
    }
}